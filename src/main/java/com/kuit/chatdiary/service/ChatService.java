package com.kuit.chatdiary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.kuit.chatdiary.domain.*;
import com.kuit.chatdiary.dto.chat.ChatGetResponseDTO;
import com.kuit.chatdiary.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private ObjectMapper objectMapper;

    public int processUserMessage(Long userId, String content, ChatType chatType) {
        try {
            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            Chat userChat = new Chat(member, Sender.USER, content, chatType);
            chatRepository.save(userChat);

            return HttpStatus.OK.value();
        } catch (Exception e) {
            log.error("Error saving user chat", e);
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }

    public Chat processGptMessage(Long userId, String content, Integer model, ChatType chatType) throws JsonProcessingException {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        String gptResponse = extractGptResponse(openAIService.getCompletion(userId, model, member));

        log.info("gptResponse: {}", gptResponse);
        Chat gptChat = new Chat(member, Sender.getByIndex(model), gptResponse, ChatType.CHAT);
        chatRepository.save(gptChat);

        return gptChat;
    }

    public String extractGptResponse(String jsonResponse) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode choicesNode = rootNode.path("choices");

        if (!choicesNode.isEmpty() && choicesNode.isArray()) {
            JsonNode firstChoice = choicesNode.get(0);
            JsonNode messageNode = firstChoice.path("message");
            return messageNode.path("content").asText();
        }

        return "";
    }

    public List<ChatGetResponseDTO> getChats(Long userId, Long lastChatId) {
        List<Chat> chats = chatRepository.findTop10ByUserIdAndChatIdGreaterThanOrderByChatIdDesc(userId, lastChatId);
        System.out.println(chats);
        if (chats.isEmpty()) {
            return new ArrayList<>();
        }
        return chats.stream()
                .map(ChatGetResponseDTO::new)
                .collect(Collectors.toList());
    }

    public void deleteUserMessage(Long userId) {
        try {
            List<Chat> userChats = chatRepository.findTopByMember_UserIdOrderByChatIdDesc(userId);
            if (!userChats.isEmpty()) {
                Chat chatToDelete = userChats.get(0);
                log.info("Deleting user chat with chatId: {}", chatToDelete.getChatId());
                chatRepository.delete(chatToDelete);
            }
        } catch (Exception e) {
            log.error("Error deleting user chat", e);
            throw new RuntimeException("Error deleting user chat", e);
        }
    }

}
