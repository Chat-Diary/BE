package com.kuit.chatdiary.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuit.chatdiary.dto.chat.ChatRequestDTO;
import com.kuit.chatdiary.domain.Chat;
import com.kuit.chatdiary.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CompletableFuture;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatRequestDTO request = objectMapper.readValue(message.getPayload(), ChatRequestDTO.class);

        CompletableFuture.runAsync(() -> {
            try {
                int status = chatService.processUserMessage(request.getUserId(), request.getContent(), request.getSelectedModel());
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(status)));
            } catch (Exception e) {
                // 예외 처리 로직
            }
        });

        CompletableFuture.runAsync(() -> {
            try {
                Chat gptResponse = chatService.processGptMessage(request.getUserId(), request.getContent(), request.getSelectedModel());
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(gptResponse)));
            } catch (Exception e) {
                // 예외 처리 로직
            }
        });
    }
}
