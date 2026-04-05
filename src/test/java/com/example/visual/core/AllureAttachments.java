package com.example.visual.core;

import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class AllureAttachments {

    public static void attachIfExists(String name, Path path) {
        if (path == null || !Files.exists(path)) {
            return;
        }

        try {
            byte[] bytes = Files.readAllBytes(path);
            attachPng(name, bytes);
        } catch (IOException e) {
            attachText(name + " (attachment error)", e.getMessage());
        }
    }

    public static void attachPng(String name, byte[] content) {
        if (content == null || content.length == 0) {
            return;
        }

        Allure.addAttachment(
                name,
                "image/png",
                new ByteArrayInputStream(content),
                ".png"
        );
    }

    public static void attachText(String name, String content) {
        Allure.addAttachment(
                name,
                "text/plain",
                content == null ? "" : content
        );
    }
}