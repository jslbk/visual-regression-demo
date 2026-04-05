package com.example.visual.core;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;

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

    @Attachment(value = "{name}", type = "image/png")
    public static byte[] attachPng(String name, byte[] content) {
        return content;
    }

    @Attachment(value = "{name}", type = "text/plain")
    public static String attachText(String name, String content) {
        return content;
    }

    public static void addArtifactLink(String label, Path path) {
        if (path != null) {
            Allure.link(label, path.toUri().toString());
        }
    }

    public static void attachImage(String name, Path imagePath) {
        try {
            byte[] content = Files.readAllBytes(imagePath);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(content), ".png");
        } catch (IOException e) {
            throw new RuntimeException("Failed to attach image: " + imagePath, e);
        }
    }
}
