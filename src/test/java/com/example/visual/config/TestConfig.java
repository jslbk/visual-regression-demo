package com.example.visual.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "system:properties",
        "system:env",
        "classpath:visual.properties",
        "classpath:${visual.env}.properties"
})
public interface TestConfig extends Config {

    @Key("visual.viewport.width")
    @DefaultValue("1440")
    int viewportWidth();

    @Key("visual.viewport.height")
    @DefaultValue("1100")
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

    @Key("visual.browser")
    @DefaultValue("chromium")
    String browser();

    @Config.Key("visual.headless")
    @DefaultValue("true")
    boolean headless();

    @Key("visual.snapshot.full.page")
    @DefaultValue("true")
    boolean fullPageScreenshot();

    @Key("visual.env")
    @DefaultValue("local")
    String env();
}