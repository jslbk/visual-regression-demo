package com.example.visual.core;

import com.example.visual.config.TestConfig;
import com.example.visual.config.TestConfigProvider;
import com.example.visual.config.ViewportProfile;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    private static final TestConfig CONFIG = TestConfigProvider.getConfig();
    private static ViewportProfile profile;

    @BeforeAll
    static void setUpRuntime() {
        playwright = Playwright.create();
        profile = ViewportProfile.from(CONFIG.profile());

        BrowserType browserType = resolveBrowserType(CONFIG.browser());

        browser = browserType.launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(CONFIG.headless())
        );
    }

    @BeforeEach
    void setUpTest() {
        int viewportWidth = CONFIG.viewportWidth() > 0
                ? CONFIG.viewportWidth()
                : profile.defaultWidth();

        int viewportHeight = CONFIG.viewportHeight() > 0
                ? CONFIG.viewportHeight()
                : profile.defaultHeight();

        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(viewportWidth, viewportHeight)
                .setScreenSize(viewportWidth, viewportHeight)
                .setDeviceScaleFactor(1.0)
                .setHasTouch(profile.hasTouch())
                .setIsMobile(profile.isMobile())
        );

        page = context.newPage();
    }

    @AfterEach
    void tearDownTest() {
        if (page != null) {
            page.close();
        }

        if (context != null) {
            context.close();
        }
    }

    @AfterAll
    static void tearDownRuntime() {
        if (browser != null) {
            browser.close();
        }

        if (playwright != null) {
            playwright.close();
        }
    }

    protected static String browserName() {
        return CONFIG.browser().trim().toLowerCase();
    }

    protected static String profileName() {
        return profile.nameValue();
    }

    protected static int effectiveViewportWidth() {
        return CONFIG.viewportWidth() > 0 ? CONFIG.viewportWidth() : profile.defaultWidth();
    }

    protected static int effectiveViewportHeight() {
        return CONFIG.viewportHeight() > 0 ? CONFIG.viewportHeight() : profile.defaultHeight();
    }

    private static BrowserType resolveBrowserType(String browserName) {
        return switch (browserName.trim().toLowerCase()) {
            case "chromium" -> playwright.chromium();
            case "firefox" -> playwright.firefox();
            default -> throw new IllegalArgumentException(
                    "Unsupported visual.browser: " + browserName
            );
        };
    }
}