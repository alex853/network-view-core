package net.simforge.networkview.core;

import net.simforge.atmosphere.ActualAltitude;
import net.simforge.atmosphere.AltimeterMode;
import net.simforge.atmosphere.AltimeterRules;
import net.simforge.atmosphere.Atmosphere;
import net.simforge.commons.misc.Geo;
import net.simforge.networkview.core.report.ParsingLogics;
import net.simforge.networkview.core.report.ReportInfo;
import net.simforge.networkview.core.report.persistence.Report;
import net.simforge.networkview.core.report.persistence.ReportPilotPosition;
import net.simforge.refdata.airports.Airport;
import net.simforge.refdata.airports.Airports;
import net.simforge.refdata.airports.DistanceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BasicPosition implements Position {

    private static final Logger log = LoggerFactory.getLogger(BasicPosition.class.getName());

    private final long reportId;
    private final String report;
    private ReportInfo reportInfo;

    private Geo.Coords coords;
    private int actualAltitude;
    private String actualFL;
    private boolean onGround;
    private boolean inAirport;
    private String airportIcao;

    private short groundspeed;

    private String callsign;
    private String regNo;
    private String fpAircraftType;
    private String fpDeparture;
    private String fpDestination;

    BasicPosition(ReportPilotPosition reportPilotPosition) {
        this.reportId = reportPilotPosition.getReport().getId();
        this.report = reportPilotPosition.getReport().getReport();
        this.coords = new Geo.Coords(reportPilotPosition.getLatitude(), reportPilotPosition.getLongitude());

        Airport nearestAirport = Airports.get().findNearest(this.coords, DistanceType.DegreeDifference);

        if (reportPilotPosition.getQnhMb() != null) { // VATSIM
            double correctedQnh = reportPilotPosition.getQnhMb();
            if (correctedQnh < 900 || correctedQnh > 1100) {
                correctedQnh = Atmosphere.QNH_STD_PRECISE;
                log.warn("Pilot {} - Report {} - Reported QNH {} is out of expected range 900..1100, STD QNH will be used instead",
                        reportPilotPosition.getPilotNumber(),
                        reportPilotPosition.getReport().getReport(),
                        reportPilotPosition.getQnhMb());
            }

            AltimeterRules altimeterRules = AltimeterRules.get(nearestAirport, correctedQnh);

            if (altimeterRules.isValid() && nearestAirport != null) {
                this.actualAltitude = altimeterRules.getActualAltitude(reportPilotPosition.getAltitude());
                this.actualFL = altimeterRules.formatAltitude(this.actualAltitude);
                this.onGround = this.actualAltitude < nearestAirport.getElevation() + 200;
            } else {
                this.actualAltitude = ActualAltitude.get(reportPilotPosition.getAltitude(), correctedQnh).getActualAltitude();
                this.actualFL = ActualAltitude.formatAltitude(this.actualAltitude, AltimeterMode.STD);
                this.onGround = false;
            }
        } else { // IVAO
            this.onGround = reportPilotPosition.getOnGround();
        }

        this.inAirport = this.onGround
                && nearestAirport != null
                && nearestAirport.isWithinBoundary(this.coords);
        this.airportIcao = this.inAirport ? nearestAirport.getIcao() : null;

        this.groundspeed = reportPilotPosition.getGroundspeed().shortValue();

        this.callsign = limit10(reportPilotPosition.getCallsign());
        this.regNo = limit10(reportPilotPosition.getParsedRegNo());
        this.fpAircraftType = limit10(ParsingLogics.parseAircraftType(reportPilotPosition.getFpAircraft()));
        this.fpDeparture = limit10(reportPilotPosition.getFpOrigin());
        this.fpDestination = limit10(reportPilotPosition.getFpDestination());
    }

    BasicPosition(Report report) {
        this.reportId = report.getId();
        this.report = report.getReport();
    }

    @Override
    public ReportInfo getReportInfo() {
        if (reportInfo == null) {
            reportInfo = new ReportInfo() {
                @Override
                public Long getId() {
                    return reportId;
                }

                @Override
                public String getReport() {
                    return report;
                }
            };
        }
        return reportInfo;
    }

    @Override
    public Geo.Coords getCoords() {
        checkPositionKnown();
        return coords;
    }

    @Override
    public int getActualAltitude() {
        checkPositionKnown();
        return actualAltitude;
    }

    @Override
    public String getActualFL() {
        checkPositionKnown();
        return actualFL;
    }

    @Override
    public boolean isOnGround() {
        checkPositionKnown();
        return onGround;
    }

    @Override
    public boolean isInAirport() {
        checkPositionKnown();
        return inAirport;
    }

    @Override
    public String getAirportIcao() {
        checkPositionKnown();
        return airportIcao;
    }

    @Override
    public short getGroundspeed() {
        checkPositionKnown();
        return groundspeed;
    }

    private void checkPositionKnown() {
        if (!isPositionKnown()) {
            throw new IllegalStateException("Position is unknown");
        }
    }

    @Override
    public boolean isPositionKnown() {
        return coords != null;
    }

    @Override
    public String getCallsign() {
        checkPositionKnown();
        return callsign;
    }

    @Override
    public String getRegNo() {
        checkPositionKnown();
        return regNo;
    }

    @Override
    public String getFpAircraftType() {
        checkPositionKnown();
        return fpAircraftType;
    }

    @Override
    public String getFpDeparture() {
        checkPositionKnown();
        return fpDeparture;
    }

    @Override
    public String getFpDestination() {
        checkPositionKnown();
        return fpDestination;
    }

    @Override
    public String toString() {
        return "{" + getStatus() + ", rId=" + reportId + "}";
    }

    private static String limit10(String s) {
        if (s == null) {
            return null;
        }
        s = s.trim();
        if (s.length() == 0) {
            return null;
        }
        if (s.length() <= 10) {
            return s;
        }
        s = s.substring(0, 10);
        s = s.trim();
        return s.length() > 0 ? s : null;
    }

}
