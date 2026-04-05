package com.example.visual.core;

import java.nio.file.Path;

public record ImageComparisonResult(
        int width,
        int height,
        long mismatchedPixels,
        long totalPixels,
        double mismatchPercent,
        Path diffImagePath
) {
    public boolean exceeds(double allowedPercent) {
        return mismatchPercent > allowedPercent;
    }
}
