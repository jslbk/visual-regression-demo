package com.example.visual.core;

import com.example.visual.config.TestConfig;
import com.example.visual.config.TestConfigProvider;
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

    @BeforeAll
    static void setUpRuntime() {
        try {
            playwright = Playwright.create();
            browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(true)
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @BeforeEach
    void setUpTest() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(CONFIG.viewportWidth(), CONFIG.viewportHeight())
                .setDeviceScaleFactor(1.0)
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
}
