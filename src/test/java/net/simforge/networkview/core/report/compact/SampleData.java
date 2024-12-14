package net.simforge.networkview.core.report.compact;

import net.simforge.networkview.core.Position;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SampleData {
    static List<Position> loadSamplePositions() throws IOException {
        final InputStream in = V1OpsTest.class.getResourceAsStream("/net/simforge/networkview/core/report/compact/20241210173434");
        return V1Ops.loadFromStream(in);
    }
}
