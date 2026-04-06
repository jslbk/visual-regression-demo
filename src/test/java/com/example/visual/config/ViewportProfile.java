package com.example.visual.config;

public enum ViewportProfile {
    DESKTOP("desktop", 1440, 1100, false, false),
    TABLET("tablet", 834, 1112, true, false),
    MOBILE("mobile", 390, 844, true, true);

    private final String name;
    private final int defaultWidth;
    private final int defaultHeight;
    private final boolean hasTouch;
    private final boolean isMobile;

    ViewportProfile(String name, int defaultWidth, int defaultHeight, boolean hasTouch, boolean isMobile) {
        this.name = name;
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.hasTouch = hasTouch;
        this.isMobile = isMobile;
    }

    public String nameValue() {
        return name;
    }

    public int defaultWidth() {
        return defaultWidth;
    }

    public int defaultHeight() {
        return defaultHeight;
    }

    public boolean hasTouch() {
        return hasTouch;
    }

    public boolean isMobile() {
        return isMobile;
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