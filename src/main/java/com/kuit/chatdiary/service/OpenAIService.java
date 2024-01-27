package com.kuit.chatdiary.service;

import com.kuit.chatdiary.domain.Chat;
import com.kuit.chatdiary.repository.ChatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class OpenAIService {

    @Value("${OPEN_AI_KEY}")
    private String OPEN_AI_KEY;

    @Autowired
    private ChatRepository chatRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getCompletion(Long userId, String content) {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPEN_AI_KEY);

        List<Map<String, String>> messages = new ArrayList<>();

        List<Map<String, String>> previousMessages = getRecentMessages(userId);
        messages.addAll(previousMessages);

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", content);
        messages.add(userMessage);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("max_tokens", 60);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error calling OpenAI API: ", e);
            throw new RuntimeException("Error calling OpenAI API", e);
        }
    }

    public List<Map<String, String>> getRecentMessages(Long userId) {
        List<Chat> recentChats = chatRepository.findTop10ByMember_UserIdOrderByChatIdDesc(userId);

        List<Map<String, String>> previousAssistantMessages = new ArrayList<>();
        for (Chat chat : recentChats) {
            Map<String, String> message = new HashMap<>();
            message.put("role", "assistant");
            message.put("content", chat.getContent());
            previousAssistantMessages.add(message);
        }

        return previousAssistantMessages;
    }
}
