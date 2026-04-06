package com.example.visual.config;

import org.aeonbits.owner.ConfigFactory;

public final class TestConfigProvider {
    private static final TestConfig CONFIG = ConfigFactory.create(TestConfig.class);

    public static TestConfig getConfig() {
        return CONFIG;
    }
}