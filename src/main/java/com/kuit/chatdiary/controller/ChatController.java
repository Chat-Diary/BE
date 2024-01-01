package com.kuit.chatdiary.controller;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class ChatController {

    private String apiKey="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";

    @PostMapping("/askTest")
    public ResponseEntity<String> test(@RequestBody Map<String, String> prompt) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://api.openai.com/v1/chat/completions";

        // OpenAI 요청 본문 구성
        JSONObject body = new JSONObject();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", new JSONObject[] {
                new JSONObject().put("role", "system").put("content", "You are a helpful assistant."),
                new JSONObject().put("role", "user").put("content", prompt.get("prompt"))
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        System.out.println("hello");
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
            return response;
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
