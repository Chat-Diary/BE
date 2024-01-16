package com.kuit.chatdiary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.kuit.chatdiary.domain.*;
import com.kuit.chatdiary.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        String gptResponse = extractGptResponse(openAIService.getCompletion(userChat.getContent()));

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
