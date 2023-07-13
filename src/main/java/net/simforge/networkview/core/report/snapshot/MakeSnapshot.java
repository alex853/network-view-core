package net.simforge.networkview.core.report.snapshot;

import net.simforge.commons.io.Csv;
import net.simforge.commons.io.IOHelper;
import net.simforge.networkview.core.Network;
import net.simforge.networkview.core.report.persistence.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MakeSnapshot {
    public static void main(String[] args) throws IOException {
        System.out.print("Enter pilot number  : ");
        Scanner in = new Scanner(System.in);
        int pilotNumber = in.nextInt();

        System.out.print("[a]ll/[d]ate/[r]ange: ");
        String rangeCode = in.next().toLowerCase();

        String fromDate = null;
        String toDate = null;

        if ("d".equals(rangeCode)) {
            System.out.print("Date                : ");
            fromDate = toDate = in.next();
        } else if ("r".equals(rangeCode)) {
            System.out.print("From date           : ");
            fromDate = in.next();
            System.out.print("To date             : ");
            toDate = in.next();
        }

        Csv csv = new Csv();
        CsvSnapshotReportOpsService.addColumns(csv);

        ReportSessionManager reportSessionManager = new ReportSessionManager();
        ReportOpsService reportOpsService = new BaseReportOpsService(reportSessionManager, Network.VATSIM);

        Report fromReport = reportOpsService.loadFirstReport();
        Report currentReport = fromReport;

        int reportsAmount = 0;
        while (currentReport != null) {
            final String reportTimestamp = currentReport.getReport();
            final boolean addPosition;
            if (fromDate != null) {
                if (reportTimestamp.compareTo(fromDate) < 0) {
                    addPosition = false;
                } else if (reportTimestamp.substring(0, toDate.length()).compareTo(toDate) > 0) {
                    addPosition = false;
                } else {
                    addPosition = true;
                }
            } else { // we are in All mode
                addPosition = true;
            }

            System.out.println("    Report " + reportTimestamp + "        " + (addPosition ? "ADDED" : "skipped"));

            if (addPosition) {
                reportsAmount++;
                ReportPilotPosition reportPilotPosition = reportOpsService.loadPilotPosition(pilotNumber, currentReport);
                CsvSnapshotReportOpsService.addRow(csv, currentReport, reportPilotPosition);
            }
            currentReport = reportOpsService.loadNextReport(reportTimestamp);
        }
        reportSessionManager.dispose();

        String filename = String.format("./pilot-%s_from-%s_amount-%s.csv", pilotNumber, fromReport.getId(), reportsAmount);
        IOHelper.saveFile(new File(filename), csv.getContent());
    }
}
