package com.example.visual.core;

import com.example.visual.config.VisualTestConfig;
import io.qameta.allure.Allure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.fail;

public final class VisualAssertions {

    private VisualAssertions() {
    }

    public static void assertScreenshotMatches(String snapshotName, byte[] actualScreenshotBytes) {
        Path baselinePath = VisualTestConfig.baselineDir().resolve(snapshotName + ".png");
        Path actualPath = VisualTestConfig.actualDir().resolve(snapshotName + ".png");
        Path diffPath = VisualTestConfig.diffDir().resolve(snapshotName + ".png");

        double allowedDiffPercent = VisualTestConfig.mismatchThresholdPercent();

        writeBytes(actualPath, actualScreenshotBytes);

        if (!Files.exists(baselinePath) || VisualTestConfig.updateBaseline()) {
            writeBytes(baselinePath, actualScreenshotBytes);

            Allure.step("Baseline prepared: " + snapshotName, () -> {
                AllureAttachments.attachText(
                        "Visual result",
                        "Baseline created or refreshed for snapshot: " + snapshotName
                );
                AllureAttachments.attachIfExists("Baseline screenshot", baselinePath);
            });
            return;
        }

        ImageComparisonResult result = ImageDiffUtils.compare(baselinePath, actualPath, diffPath);
        double actualDiffPercent = result.mismatchPercent();

        if (actualDiffPercent > allowedDiffPercent) {
            Allure.step("Visual mismatch: " + snapshotName, () -> {
                AllureAttachments.attachText(
                        "Mismatch summary",
                        visualSummary(snapshotName, baselinePath, actualPath, diffPath, result, allowedDiffPercent)
                );

                AllureAttachments.attachIfExists("Expected screenshot", baselinePath);
                AllureAttachments.attachIfExists("Actual screenshot", actualPath);
                AllureAttachments.attachIfExists("Diff screenshot", diffPath);
            });

            fail(String.format(
                    "Visual mismatch for '%s': actual diff %.4f%% exceeds allowed %.4f%%",
                    snapshotName,
                    actualDiffPercent,
                    allowedDiffPercent
            ));
        } else {
            Allure.step("Visual comparison passed: " + snapshotName, () -> {
                AllureAttachments.attachText(
                        "Comparison summary",
                        visualSummary(snapshotName, baselinePath, actualPath, diffPath, result, allowedDiffPercent)
                );
                AllureAttachments.attachIfExists("Expected screenshot", baselinePath);
                AllureAttachments.attachIfExists("Actual screenshot", actualPath);
            });
        }
    }

    private static String visualSummary(
            String snapshotName,
            Path baselinePath,
            Path actualPath,
            Path diffPath,
            ImageComparisonResult result,
            double allowedDiffPercent
    ) {
        return """
                Snapshot: %s
                Allowed mismatch: %.4f%%
                Actual mismatch: %.4f%%
                Pixels mismatched: %d / %d
                Baseline: %s
                Actual: %s
                Diff: %s
                """.formatted(
                snapshotName,
                allowedDiffPercent,
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

}