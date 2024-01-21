package com.kuit.chatdiary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.kuit.chatdiary.domain.*;
import com.kuit.chatdiary.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OpenAIService openAIService;

    public Chat processMessage(Long userId, String content, Integer model) throws JsonProcessingException {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Chat userChat = new Chat(member, Sender.USER, content);
        chatRepository.save(userChat);

        String gptResponse = extractGptResponse(openAIService.getCompletion(userId, content));

        Chat gptChat = new Chat(member, Sender.getByIndex(model), gptResponse);
        chatRepository.save(gptChat);

        return gptChat;
    }

    public int processUserMessage(Long userId, String content, Integer model) throws JsonProcessingException {
        try {
            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            Chat userChat = new Chat(member, Sender.USER, content);
            chatRepository.save(userChat);

            return HttpStatus.OK.value();
        } catch (Exception e) {
            log.error("Error saving user chat", e);
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }

    public Chat processGptMessage(Long userId, String content, Integer model) throws JsonProcessingException {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        String gptResponse = extractGptResponse(openAIService.getCompletion(userId, content));

        log.info("gptResponse: {}", gptResponse);
        Chat gptChat = new Chat(member, Sender.getByIndex(model), gptResponse);
        chatRepository.save(gptChat);

        return gptChat;
    }

    public String extractGptResponse(String jsonResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode choicesNode = rootNode.path("choices");

        if (!choicesNode.isEmpty() && choicesNode.isArray()) {
            JsonNode firstChoice = choicesNode.get(0);
            JsonNode messageNode = firstChoice.path("message");
            return messageNode.path("content").asText();
        }

        return "";
    }

}
