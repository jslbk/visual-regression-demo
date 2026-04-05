package com.example.visual.core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ImageDiffUtils {
    private static final int PIXEL_TOLERANCE_PER_CHANNEL = 8;

    public static ImageComparisonResult compare(Path expectedPath, Path actualPath, Path diffPath) {
        try {
            BufferedImage expected = ImageIO.read(expectedPath.toFile());
            BufferedImage actual = ImageIO.read(actualPath.toFile());

            if (expected == null || actual == null) {
                throw new IllegalStateException("Unable to read one of the images for comparison.");
            }

            if (expected.getWidth() != actual.getWidth() || expected.getHeight() != actual.getHeight()) {
                throw new IllegalStateException(
                        "Image dimensions do not match. Expected: %dx%d, actual: %dx%d"
                                .formatted(expected.getWidth(), expected.getHeight(), actual.getWidth(), actual.getHeight())
                );
            }

            int width = expected.getWidth();
            int height = expected.getHeight();
            long totalPixels = (long) width * height;
            long mismatchedPixels = 0;

            BufferedImage diffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = diffImage.createGraphics();
            graphics.drawImage(actual, 0, 0, null);
            graphics.setColor(new Color(255, 0, 80, 160));

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int expectedRgb = expected.getRGB(x, y);
                    int actualRgb = actual.getRGB(x, y);
                    if (!isWithinTolerance(expectedRgb, actualRgb)) {
                        mismatchedPixels++;
                        graphics.fillRect(x, y, 1, 1);
                    }
                }
            }

            graphics.dispose();
            Files.createDirectories(diffPath.getParent());
            ImageIO.write(diffImage, "png", diffPath.toFile());

            double mismatchPercent = totalPixels == 0 ? 0.0 : (mismatchedPixels * 100.0) / totalPixels;
            return new ImageComparisonResult(width, height, mismatchedPixels, totalPixels, mismatchPercent, diffPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to compare images.", e);
        }
    }

    private static boolean isWithinTolerance(int expectedRgb, int actualRgb) {
        Color expected = new Color(expectedRgb, true);
        Color actual = new Color(actualRgb, true);

        return Math.abs(expected.getRed() - actual.getRed()) <= PIXEL_TOLERANCE_PER_CHANNEL
                && Math.abs(expected.getGreen() - actual.getGreen()) <= PIXEL_TOLERANCE_PER_CHANNEL
                && Math.abs(expected.getBlue() - actual.getBlue()) <= PIXEL_TOLERANCE_PER_CHANNEL
                && Math.abs(expected.getAlpha() - actual.getAlpha()) <= PIXEL_TOLERANCE_PER_CHANNEL;
    }
}
