package net.simforge.networkview.core.report.compact;

import net.simforge.networkview.core.Network;
import net.simforge.networkview.core.Position;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class CompactifiedStorageTest {
    @Test
    @Ignore
    public void test() throws IOException {
        final CompactifiedStorage storage = CompactifiedStorage.getStorage(
                System.getProperty("java.io.tmpdir") + "/storage/" + System.currentTimeMillis(),
                Network.VATSIM);
        assertNull(storage.getFirstReport());

        final List<Position> originalPositions = loadSamplePositions();

        final String report1 = "20241201000000";
        final String report2 = "20241201010000";

        storage.savePositions(report1, originalPositions);

        assertArrayEquals(new String[] {report1}, storage.listAllReports().toArray());

        storage.savePositions(report2, originalPositions);

        assertArrayEquals(new String[] {report1, report2}, storage.listAllReports().toArray());
    }

    private List<Position> loadSamplePositions() throws IOException {
        final InputStream in = V1OpsTest.class.getResourceAsStream("/net/simforge/networkview/core/report/compact/20241211205134");
        return V1Ops.loadFromStream(in);
    }
}