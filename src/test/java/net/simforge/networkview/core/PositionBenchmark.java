package net.simforge.networkview.core;

import net.simforge.networkview.core.report.persistence.Report;
import net.simforge.networkview.core.report.persistence.ReportPilotPosition;

import java.util.ArrayList;
import java.util.List;

// Base time, Avg time, nano
//      WORLDWIDE:      5216
//      EUROPE:        15805, 19688, 20119

// AltimeterRules moved to atmosphere and it accepts Airport instead of Geo.Coords now
//      WORLDWIDE:      2621
//      EUROPE:         7876, 10500, 14523

public class PositionBenchmark {
    public static void main(String[] args) {
        System.out.println("Warming up Airports storage");
        Position.create(randomReportPilotPosition()); // to allow Airports to load up

        int calculationCount = 10000000;

        System.out.println("Preparing random positions");
        List<ReportPilotPosition> positions = new ArrayList<>(calculationCount);
        for (int i = 0; i < calculationCount; i++) {
            positions.add(randomReportPilotPosition());
        }

        System.out.println("Calculating Position objects");
        long started = System.currentTimeMillis();
        for (int i = 0; i < calculationCount; i++) {
            //Position.create(positions.get(i));
            Position.compactify(Position.create(positions.get(i)));
        }
        long finished = System.currentTimeMillis();

        System.out.println("Avg time, nano: " + (finished - started)*1000000 / calculationCount);
    }

    private static ReportPilotPosition randomReportPilotPosition() {
        Report report = new Report();
        report.setId(1L);
        report.setReport("20230101000000");

        ReportPilotPosition p = new ReportPilotPosition();
        p.setReport(report);

        // worldwide
//        p.setLatitude(180*Math.random() - 90);
//        p.setLongitude(360*Math.random() - 180);

        // europe
        p.setLatitude(48 + (2*15*Math.random() - 15));
        p.setLongitude(8 + (2*30*Math.random() - 30));

        p.setQnhMb(1013);
        p.setAltitude(10000);
        p.setGroundspeed(100);

        return p;
    }
}
