package com.example.visual.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class VisualTestConfig {
    private static final String DEFAULT_RELATIVE_PAGE = "src/test/resources/demo-page/index.html";

    public static int viewportWidth() {
        return Integer.parseInt(System.getProperty("visual.viewport.width", "1440"));
    }

    public static int viewportHeight() {
        return Integer.parseInt(System.getProperty("visual.viewport.height", "1100"));
    }

    public static double mismatchThresholdPercent() {
        return Double.parseDouble(System.getProperty("visual.threshold.percent", "0.15"));
    }

    public static Path baselineDir() {
        return pathFromProperty("visual.baseline.dir", "artifacts/visual/baseline");
    }

    public static Path actualDir() {
        return pathFromProperty("visual.actual.dir", "artifacts/visual/actual");
    }

    public static Path diffDir() {
        return pathFromProperty("visual.diff.dir", "artifacts/visual/diff");
    }

    public static boolean updateBaseline() {
        return Boolean.parseBoolean(System.getProperty("visual.updateBaseline", "false"));
    }

    public static String demoPageUrl() {
        String explicitUrl = System.getProperty("visual.demo.url");
        if (explicitUrl != null && !explicitUrl.isBlank()) {
            return explicitUrl;
        }

        Path absolute = Paths.get(DEFAULT_RELATIVE_PAGE).toAbsolutePath().normalize();
        return absolute.toUri().toString();
    }

    private static Path pathFromProperty(String propertyName, String defaultValue) {
        return Paths.get(System.getProperty(propertyName, defaultValue)).toAbsolutePath().normalize();
    }
}
