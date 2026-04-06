package com.example.visual.config;

public enum ViewportProfile {
    DESKTOP("desktop", 1440, 1100),
    TABLET("tablet", 834, 1112),
    MOBILE("mobile", 390, 844);

    public int defaultWidth() {
        return width;
    }
    private final String name;
    private final int width;

    private final int height;

    ViewportProfile(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public String nameValue() {
        return name;
    }

    public int defaultHeight() {
        return height;
    }

    public static ViewportProfile from(String value) {
        if (value == null || value.isBlank()) {
            return DESKTOP;
        }

        return switch (value.trim().toLowerCase()) {
            case "desktop" -> DESKTOP;
            case "tablet" -> TABLET;
            case "mobile" -> MOBILE;
            default -> throw new IllegalArgumentException(
                    "Unsupported visual.profile: " + value +
                            ". Supported values: desktop, tablet, mobile"
            );
        };
    }

}