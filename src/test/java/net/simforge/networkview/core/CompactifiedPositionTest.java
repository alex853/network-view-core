package net.simforge.networkview.core;

import junit.framework.TestCase;
import net.simforge.commons.io.Csv;
import net.simforge.commons.io.IOHelper;
import net.simforge.networkview.core.report.persistence.Report;
import net.simforge.networkview.core.report.persistence.ReportPilotPosition;
import net.simforge.networkview.core.report.snapshot.CsvSnapshotReportOpsService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CompactifiedPositionTest extends TestCase {
    public void test() throws IOException {
        int pilotNumber = 811636;

        InputStream is = CompactifiedPositionTest.class.getResourceAsStream("/net/simforge/networkview/core/report/snapshot/pilot-811636_from-1000000_amount-127321.csv");
        String csvContent = IOHelper.readInputStream(is);
        Csv csv = Csv.fromContent(csvContent);
        CsvSnapshotReportOpsService reportOpsService = new CsvSnapshotReportOpsService(csv);

        List<Report> reports = reportOpsService.loadAllReports();

//        ReportPilotPosition firstRpp = null;
//        Report firstReport = null;

        for (Report report : reports) {
            System.out.println("Report " + report.getReport());

            ReportPilotPosition rpp = reportOpsService.loadPilotPosition(pilotNumber, report);

//            if (firstRpp == null) {
//                firstRpp = rpp;
//            }

//            if (firstReport == null) {
//                firstReport = report;
//            }

            Position position = rpp != null ? Position.create(rpp) : Position.createOfflinePosition(report);
            Position compact = Position.compactify(position);

            assertEqualPositions(position, compact);
        }

//        List<Position1> positions = new LinkedList<>();
//        while (true) {
            //Position1 position = Position.create(firstRpp);
//            Position1 position = Position.createOfflinePosition(firstReport);
//
            //positions.add(position);
//            positions.add(PositionCompact.from(position));
//
//            if (positions.size() % 1000 == 0) {
//                System.out.println(positions.size());
//            }
//        }
    }

    private void assertEqualPositions(Position expected, Position actual) {
        assertEquals(expected.isPositionKnown(), actual.isPositionKnown());
        assertEquals(expected.getReportInfo().getId(), actual.getReportInfo().getId());
        assertEquals(expected.getReportInfo().getReport(), actual.getReportInfo().getReport());

        if (!expected.isPositionKnown()) {
            return;
        }

        assertEquals(expected.getCoords().getLat(), actual.getCoords().getLat());
        assertEquals(expected.getCoords().getLon(), actual.getCoords().getLon());
        assertEquals(expected.getActualAltitude(), actual.getActualAltitude());
        assertEquals(expected.getActualFL(), actual.getActualFL());
        assertEquals(expected.isOnGround(), actual.isOnGround());
        assertEquals(expected.isInAirport(), actual.isInAirport());
        assertEquals(expected.getAirportIcao(), actual.getAirportIcao());
        assertEquals(expected.getGroundspeed(), actual.getGroundspeed());
        assertEquals(expected.hasFlightplan(), actual.hasFlightplan());
        assertEquals(expected.getCallsign(), actual.getCallsign());
        assertEquals(expected.getRegNo(), actual.getRegNo());
        assertEquals(expected.getFpAircraftType(), actual.getFpAircraftType());
        assertEquals(expected.getFpDeparture(), actual.getFpDeparture());
        assertEquals(expected.getFpDestination(), actual.getFpDestination());
        assertEquals(expected.getStatus(), actual.getStatus());
    }
}
