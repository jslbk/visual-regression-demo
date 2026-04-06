package com.example.visual.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "system:properties",
        "classpath:visual.properties"
})
public interface TestConfig extends Config {

    @Key("visual.profile")
    @DefaultValue("desktop")
    String profile();

    @Key("visual.browser")
    @DefaultValue("chromium")
    String browser();

    @Key("visual.headless")
    @DefaultValue("true")
    boolean headless();

    @Key("visual.viewport.width")
    @DefaultValue("-1")
    int viewportWidth();

    @Key("visual.viewport.height")
    @DefaultValue("-1")
    int viewportHeight();

    @Key("visual.threshold.percent")
    @DefaultValue("0.15")
    double mismatchThresholdPercent();

    @Key("visual.baseline.dir")
    @DefaultValue("artifacts/visual/baseline")
    String baselineDir();

    @Key("visual.actual.dir")
    @DefaultValue("artifacts/visual/actual")
    String actualDir();

    @Key("visual.diff.dir")
    @DefaultValue("artifacts/visual/diff")
    String diffDir();

    @Key("visual.updateBaseline")
    @DefaultValue("false")
    boolean updateBaseline();

    @Key("visual.demo.url")
    String demoPageUrl();

    @Key("visual.demo.relative.page")
    @DefaultValue("src/test/resources/demo-page/index.html")
    String demoRelativePage();
}