package com.kuit.chatdiary.service;

import com.kuit.chatdiary.domain.Chat;
import com.kuit.chatdiary.domain.ChatType;
import com.kuit.chatdiary.domain.Sender;
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

    public String getCompletion(Long userId, String content, ChatType chatType) {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPEN_AI_KEY);

        List<Map<String, Object>> messages = new ArrayList<>();
        List<Map<String, Object>> previousMessages = getRecentChats(userId);
        messages.addAll(previousMessages);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);
        requestBody.put("model", "gpt-4-vision-preview");
        requestBody.put("max_tokens", 200);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error calling OpenAI API: ", e);
            throw new RuntimeException("Error calling OpenAI API", e);
        }
    }

    public List<Map<String, Object>> getRecentChats(Long userId) {
        List<Chat> recentChats = chatRepository.findTop10ByMember_UserIdOrderByChatIdAsc(userId);

        List<Map<String, Object>> previousMessages = new ArrayList<>();
        for (Chat chat : recentChats) {
            Map<String, Object> message = new HashMap<>();

            if (Sender.USER.equals(chat.getSender())) {
                message.put("role", "user");

                if (ChatType.CHAT.equals(chat.getChatType())) {
                    message.put("content", List.of(getTextPart(chat.getContent())));
                } else if (ChatType.IMG.equals(chat.getChatType())) {
                    log.info("OpenAIService.getRecentChats, IMG");
                    message.put("content", List.of(getImagePart(chat.getContent())));
                }
            } else {
                message.put("role", "assistant");
                message.put("content", List.of(getTextPart(chat.getContent())));
            }

            previousMessages.add(message);
        }
        return previousMessages;
    }

    private Map<String, Object> getTextPart(String content) {
        Map<String, Object> textPart = new HashMap<>();
        textPart.put("type", "text");
        textPart.put("text", content);
        return textPart;
    }

    private Map<String, Object> getImagePart(String content) {
        Map<String, Object> imagePart = new HashMap<>();
        Map<String, String> imageUrl = new HashMap<>();
        imagePart.put("type", "image_url");
        imageUrl.put("url", content);
        imagePart.put("image_url", imageUrl);
        return imagePart;
    }

}
