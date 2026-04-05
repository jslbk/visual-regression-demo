package com.example.visual.pages;

import com.example.visual.config.VisualTestConfig;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class DemoPage {
    private final Page page;
    private final Locator heroCard;
    private final Locator featureBadge;

    public DemoPage(Page page) {
        this.page = page;
        this.heroCard = page.locator(".hero .card").first();
        this.featureBadge = page.locator(".feature-badge").first();
    }

    public void open() {
        page.navigate(VisualTestConfig.demoPageUrl());
    }

    public void waitForStableUi() {
        page.waitForLoadState();
        page.locator("body").waitFor();
    }

    public Locator heroCard() {
        return heroCard;
    }

    public Locator featureBadge() {
        return featureBadge;
    }

    public void injectDynamicBuildValue() {
        featureBadge.evaluate("element => element.textContent = 'Build: ' + Date.now()");
    }
}
