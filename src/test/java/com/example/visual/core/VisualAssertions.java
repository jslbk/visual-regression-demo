package com.example.visual.core;

import com.example.visual.config.VisualTestConfig;
import io.qameta.allure.Allure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.fail;

public final class VisualAssertions {

    public static void assertScreenshotMatches(String snapshotName, byte[] actualScreenshotBytes) {
        Path baselinePath = VisualTestConfig.baselineDir().resolve(snapshotName + ".png");
        Path actualPath = VisualTestConfig.actualDir().resolve(snapshotName + ".png");
        Path diffPath = VisualTestConfig.diffDir().resolve(snapshotName + ".png");

        long allowedMismatchPixels = 2;

        writeBytes(actualPath, actualScreenshotBytes);

        if (!Files.exists(baselinePath) || VisualTestConfig.updateBaseline()) {
            writeBytes(baselinePath, actualScreenshotBytes);

            Allure.step("Baseline prepared: " + snapshotName, () -> {
                AllureAttachments.attachText("Visual result",
                        "Baseline created or refreshed for snapshot: " + snapshotName
                );
                AllureAttachments.attachIfExists("Baseline screenshot", baselinePath);
            });
            return;
        }

        ImageComparisonResult result = ImageDiffUtils.compare(baselinePath, actualPath, diffPath);
        long actualMismatchPixels = result.mismatchedPixels();

        if (actualMismatchPixels > allowedMismatchPixels) {
            AllureAttachments.attachIfExists("Expected", baselinePath);
            AllureAttachments.attachIfExists("Actual", actualPath);
            AllureAttachments.attachIfExists("Difference", diffPath);

            fail(String.format("Visual mismatch for '%s': %d pixels differ, allowed %d pixels",
                    snapshotName,
                    actualMismatchPixels,
                    allowedMismatchPixels
            ));
        }
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