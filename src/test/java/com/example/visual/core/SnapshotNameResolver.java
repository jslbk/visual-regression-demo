package com.example.visual.core;

import org.junit.jupiter.api.TestInfo;

public final class SnapshotNameResolver {

    private SnapshotNameResolver() {
    }

    public static String from(TestInfo testInfo, String suffix) {
        String className = testInfo.getTestClass()
                .map(Class::getSimpleName)
                .orElse("UnknownClass");

        String methodName = testInfo.getTestMethod()
                .map(method -> method.getName())
                .orElse("unknownMethod");

        String runtimePart = BaseTest.browserName()
                + "-" + BaseTest.profileName()
                + "-" + BaseTest.effectiveViewportWidth()
                + "x" + BaseTest.effectiveViewportHeight();

        String suffixPart = (suffix == null || suffix.isBlank()) ? "" : "-" + suffix;

        return className + "-" + methodName + "-" + runtimePart + suffixPart;
    }
}