package com.example.visual.core;

import org.junit.jupiter.api.TestInfo;

public final class SnapshotNameResolver {

    public static String from(TestInfo testInfo, String suffix) {
        String className = testInfo.getTestClass()
                .map(Class::getSimpleName).orElse("UnknownClass");
        String methodName = testInfo.getTestMethod()
                .map(method -> method.getName()).orElse("unknownMethod");

        return className + "-" + methodName + (suffix == null || suffix.isBlank() ? "" : "-" + suffix);
    }
}
