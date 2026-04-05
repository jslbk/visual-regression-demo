package com.example.visual.core;

import com.example.visual.config.VisualTestConfig;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class VisualAssertions {

    public static void assertScreenshotMatches(String snapshotName, byte[] actualScreenshotBytes) {
        Path baselinePath = VisualTestConfig.baselineDir().resolve(snapshotName + ".png");
        Path actualPath = VisualTestConfig.actualDir().resolve(snapshotName + ".png");
        Path diffPath = VisualTestConfig.diffDir().resolve(snapshotName + ".png");

        writeBytes(actualPath, actualScreenshotBytes);
        AllureAttachments.attachPng("Actual screenshot", actualScreenshotBytes);
        AllureAttachments.addArtifactLink("Actual file", actualPath);

        if (!Files.exists(baselinePath) || VisualTestConfig.updateBaseline()) {
            writeBytes(baselinePath, actualScreenshotBytes);
            AllureAttachments.attachText("Visual result", "Baseline created or refreshed for snapshot: " + snapshotName);
            AllureAttachments.attachIfExists("Baseline screenshot", baselinePath);
            AllureAttachments.addArtifactLink("Baseline file", baselinePath);
            return;
        }

        ImageComparisonResult result = ImageDiffUtils.compare(baselinePath, actualPath, diffPath);
        AllureAttachments.attachIfExists("Baseline screenshot", baselinePath);
        AllureAttachments.attachIfExists("Diff screenshot", diffPath);
        AllureAttachments.addArtifactLink("Baseline file", baselinePath);
        AllureAttachments.addArtifactLink("Diff file", diffPath);
        AllureAttachments.attachText("Visual summary", visualSummary(snapshotName, baselinePath, actualPath, diffPath, result));

        Assertions.assertFalse(
                result.exceeds(VisualTestConfig.mismatchThresholdPercent()),
                () -> ("Visual mismatch detected for '%s'. " +
                        "Allowed <= %.2f%%, actual %.4f%%. " +
                        "Expected: %s, " +
                        "Actual: %s, diff: %s")
                        .formatted(
                                snapshotName,
                                VisualTestConfig.mismatchThresholdPercent(),
                                result.mismatchPercent(),
                                baselinePath,
                                actualPath,
                                diffPath
                        )
        );
    }

    private static String visualSummary(String snapshotName, Path baselinePath, Path actualPath, Path diffPath, ImageComparisonResult result) {
        return """
                Snapshot: %s
                Mismatch: %.4f%%
                Pixels mismatched: %d / %d
                Baseline: %s
                Actual: %s
                Diff: %s
                """.formatted(
                snapshotName,
                result.mismatchPercent(),
                result.mismatchedPixels(),
                result.totalPixels(),
                baselinePath,
                actualPath,
                diffPath
        );
    }

    private static void writeBytes(Path targetPath, byte[] bytes) {
        try {
            Files.createDirectories(targetPath.getParent());
            Files.write(targetPath, bytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write image bytes to " + targetPath, e);
        }
    }

    public static void copyActualToBaseline(String snapshotName) {
        Path baselinePath = VisualTestConfig.baselineDir().resolve(snapshotName + ".png");
        Path actualPath = VisualTestConfig.actualDir().resolve(snapshotName + ".png");
        try {
            Files.createDirectories(baselinePath.getParent());
            Files.copy(actualPath, baselinePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to promote actual screenshot to baseline for " + snapshotName, e);
        }
    }
}
