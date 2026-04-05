package com.example.visual.core;

import com.example.visual.config.VisualTestConfig;
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

    @BeforeAll
    static void setUp() {
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
                .setViewportSize(VisualTestConfig.viewportWidth(), VisualTestConfig.viewportHeight())
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
    static void tearDown() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
