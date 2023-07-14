package net.simforge.networkview.core.report.snapshot;

import net.simforge.commons.io.Csv;
import net.simforge.commons.io.IOHelper;
import net.simforge.networkview.core.Network;
import net.simforge.networkview.core.report.persistence.Report;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestCsvSnapshotReportOpsService {
    @Test
    public void test_loadAllReports() throws IOException {
        InputStream is = Network.class.getResourceAsStream("/net/simforge/networkview/core/report/snapshot/pilot-811636_from-1000000_amount-127321.csv");
        String csvContent = IOHelper.readInputStream(is);
        Csv csv = Csv.fromContent(csvContent);
        CsvSnapshotReportOpsService reportOpsService = new CsvSnapshotReportOpsService(csv);

        List<Report> reports = reportOpsService.loadAllReports();
        assertEquals(127321, reports.size());
        Iterator<Report> it = reports.iterator();
        Report prev = it.next();
        while (it.hasNext()) {
            Report curr = it.next();
            if (curr.getReport().compareTo(prev.getReport()) <= 0) {
                fail();
            }
            prev = curr;
        }
    }
}
