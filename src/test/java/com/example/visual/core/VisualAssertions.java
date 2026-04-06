package com.example.visual.core;

import com.example.visual.config.TestConfig;
import com.example.visual.config.TestConfigProvider;
import io.qameta.allure.Allure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.fail;

public final class VisualAssertions {

    private static final TestConfig CONFIG = TestConfigProvider.getConfig();

    public static void assertScreenshotMatches(String snapshotName, byte[] actualScreenshotBytes) {
        Path baselinePath = Paths.get(CONFIG.baselineDir()).resolve(snapshotName + ".png");
        Path actualPath = Paths.get(CONFIG.actualDir()).resolve(snapshotName + ".png");
        Path diffPath = Paths.get(CONFIG.diffDir()).resolve(snapshotName + ".png");

        long allowedMismatchPixels = 2;

        writeBytes(actualPath, actualScreenshotBytes);

        if (!Files.exists(baselinePath) || CONFIG.updateBaseline()) {
            writeBytes(baselinePath, actualScreenshotBytes);

            Allure.step("Baseline prepared: " + snapshotName, () -> {
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

        Allure.step("Visual comparison passed: " + snapshotName, () -> {
            AllureAttachments.attachIfExists("Actual", actualPath);
        });
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