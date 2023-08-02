package net.simforge.networkview.core;

import net.simforge.commons.misc.Geo;
import net.simforge.commons.misc.Str;
import net.simforge.networkview.core.report.ReportInfo;
import net.simforge.networkview.core.report.persistence.Report;
import net.simforge.networkview.core.report.persistence.ReportPilotPosition;

public interface Position {
    ReportInfo getReportInfo();

    Geo.Coords getCoords();

    int getActualAltitude();

    String getActualFL();

    boolean isOnGround();

    boolean isInAirport();

    String getAirportIcao();

    short getGroundspeed();

    boolean isPositionKnown();

    default boolean hasFlightplan() {
        return !Str.isEmpty(getFpAircraftType())
                || !Str.isEmpty(getFpDeparture())
                || !Str.isEmpty(getFpDestination());
    }

    String getCallsign();

    String getRegNo();

    String getFpAircraftType();

    String getFpDeparture();

    String getFpDestination();

    default String getStatus() {
        String status;

        if (isPositionKnown()) {
            if (isInAirport()) {
                status = getAirportIcao();
            } else if (isOnGround()) {
                status = "On Ground";
            } else {
                status = "Flying";
            }
        } else {
            status = "Unknown";
        }

        return status;
    }

    static Position create(ReportPilotPosition reportPilotPosition) {
        return new BasicPosition(reportPilotPosition);
    }

    static Position createOfflinePosition(Report report) {
        return new BasicPosition(report);
    }

    static Position compactify(Position position) {
        return CompactifiedPosition.from(position);
    }
}
