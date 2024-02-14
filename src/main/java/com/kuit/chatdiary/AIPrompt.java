package com.kuit.chatdiary;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public enum AIPrompt {
    DADA("DADA", "AIPrompt/DADA.txt"),
    CHICHI("CHICHI", "AIPrompt/CHICHI.txt"),
    LULU("LULU", "AIPrompt/LULU.txt");

    private final String model;
    private final String path;

    AIPrompt(String model, String path) {
        this.model = model;
        this.path = path;
    }

    public String getPrompt(String nickname, String gender, String age) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Prompt file not found: " + path);
            }
            String prompt = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            prompt = prompt.replace("{nickname}", nickname)
                           .replace("{gender}", defaultValue(gender, "Unknown"))
                           .replace("{age}", defaultValue(age, "Unknown"));
            return prompt;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read prompt file: " + path, e);
        }
    }

    private String defaultValue(String value, String defaultValue) {
        return (value != null) ? value : defaultValue;
    }

}
