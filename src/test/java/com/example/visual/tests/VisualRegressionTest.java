package com.example.visual.tests;

import com.example.visual.core.BaseTest;
import com.example.visual.core.SnapshotNameResolver;
import com.example.visual.core.VisualAssertions;
import com.example.visual.pages.DemoPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.List;

@Epic("Visual Regression")
@Feature("Demo Page")
class VisualRegressionTest extends BaseTest {

    @Test
    @DisplayName("Full page should match baseline")
    void shouldMatchFullPageBaseline(TestInfo testInfo) {
        DemoPage demoPage = new DemoPage(page);
        demoPage.open();
        demoPage.waitForStableUi();

        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions()
                .setFullPage(true)
        );

        VisualAssertions.assertScreenshotMatches(
                SnapshotNameResolver.from(testInfo, "full-page"),
                screenshot
        );
    }

    @Test
    @DisplayName("Hero card should match baseline")
    void shouldMatchHeroCardBaseline(TestInfo testInfo) {
        DemoPage demoPage = new DemoPage(page);
        demoPage.open();
        demoPage.waitForStableUi();

        Locator heroCard = demoPage.heroCard();
        byte[] screenshot = heroCard.screenshot(new Locator.ScreenshotOptions());

        VisualAssertions.assertScreenshotMatches(
                SnapshotNameResolver.from(testInfo, "hero-card"),
                screenshot
        );
    }

    @Test
    @DisplayName("Masked dynamic badge should not break baseline")
    void shouldAllowMaskingDynamicArea(TestInfo testInfo) {
        DemoPage demoPage = new DemoPage(page);
        demoPage.open();
        demoPage.injectDynamicBuildValue();
        demoPage.waitForStableUi();

        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions()
                .setFullPage(true)
                .setMask(List.of(demoPage.featureBadge()))
        );

        VisualAssertions.assertScreenshotMatches(
                SnapshotNameResolver.from(testInfo, "masked-full-page"),
                screenshot
        );
    }
}