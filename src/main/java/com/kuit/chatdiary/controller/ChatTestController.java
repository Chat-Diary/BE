package com.kuit.chatdiary.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class ChatTestController {

    @Value("${OPEN_AI_KEY}")
    private String OPEN_AI_KEY;
    @PostMapping("/testAsk")
    public ResponseEntity<String> ask(@RequestBody Map<String, String> prompt) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://api.openai.com/v1/chat/completions";

        // OpenAI 요청 본문 구성
        JSONObject body = new JSONObject();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", new JSONObject[] {
                new JSONObject().put("role", "system").put("content", "You are a playful and curious teenager. No matter the topic, you find everything fascinating and are always eager to ask lots of questions. korean"),
                new JSONObject().put("role", "user").put("content", prompt.get("prompt"))
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPEN_AI_KEY);


        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
            System.out.println("API Response: " + response.getBody());
            return response;
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}