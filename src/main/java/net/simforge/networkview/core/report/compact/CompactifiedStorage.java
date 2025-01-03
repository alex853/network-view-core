package net.simforge.networkview.core.report.compact;

import net.simforge.commons.legacy.BM;
import net.simforge.networkview.core.Network;
import net.simforge.networkview.core.Position;
import net.simforge.networkview.core.report.ReportUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompactifiedStorage {
    private final Path root;

    private CompactifiedStorage(final String rootPath, final Network network) {
        root = Paths.get(rootPath, network.name(), "compactified");
    }

    public static CompactifiedStorage getStorage(final String storageRoot, final Network network) {
        return new CompactifiedStorage(storageRoot, network);
    }

    public void savePositions(final String report, final List<Position> positions) throws IOException {
        BM.start("CompactifiedStorage.savePositions");
        try {
            final Path reportPathTmp = root.resolve(report + ".tmp");
            final Path reportPath = root.resolve(report);

            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            try (final FileOutputStream fos = new FileOutputStream(reportPathTmp.toFile())) {
                V1Ops.saveToStream(positions, fos);
            }

            Files.move(reportPathTmp, reportPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            BM.stop();
        }
    }

    public List<Position> loadPositions(final String report) throws IOException {
        BM.start("CompactifiedStorage.loadPositions");
        try {
            final Path reportPath = root.resolve(report);

            if (!Files.exists(reportPath)) {
                throw new IllegalArgumentException("unable to find report " + report);
            }

            try (final FileInputStream fis = new FileInputStream(reportPath.toFile())) {
                return V1Ops.loadFromStream(fis);
            }
        } finally {
            BM.stop();
        }
    }

    public String getFirstReport() throws IOException {
        BM.start("CompactifiedStorage.getFirstReport");
        try {
            final List<String> allReports = listAllReports();
            if (allReports.size() == 0) {
                return null;
            }
            return allReports.get(0);
        } finally {
            BM.stop();
        }
    }

    public String getNextReport(String previousReport) throws IOException {
        BM.start("CompactifiedStorage.getNextReport");
        try {
            final List<String> allReports = listAllReports();
            if (allReports.size() == 0) {
                return null;
            }
            final int index = allReports.indexOf(previousReport);
            if (index == -1) {
                return null;
            }
            if (index == allReports.size() - 1) {
                return null;
            }
            return allReports.get(index + 1);
        } finally {
            BM.stop();
        }
    }

    public String getLastReport() throws IOException {
        BM.start("CompactifiedStorage.getLastReport");
        try {
            final List<String> allReports = listAllReports();
            if (allReports.size() == 0) {
                return null;
            }
            return allReports.get(allReports.size() - 1);
        } finally {
            BM.stop();
        }
    }

    public List<String> listAllReports() throws IOException {
        BM.start("CompactifiedStorage.listAllReports");
        try {
            final List<String> reports = new ArrayList<>();

            Files.walkFileTree(root, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) {
                    if (!attrs.isRegularFile()) {
                        return FileVisitResult.CONTINUE;
                    }

                    final String filename = file.getFileName().toString();
                    if (!ReportUtils.isTimestamp(filename)) {
                        return FileVisitResult.CONTINUE;
                    }

                    reports.add(filename);

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(final Path file, final IOException exc) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });

            Collections.sort(reports);

            return reports;
        } finally {
            BM.stop();
        }
    }

    public void removeReport(final String report) throws IOException {
        BM.start("CompactifiedStorage.removeReport");
        try {
            Files.deleteIfExists(root.resolve(report));
        } finally {
            BM.stop();
        }
    }
}
