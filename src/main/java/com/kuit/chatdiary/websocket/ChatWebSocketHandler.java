package com.kuit.chatdiary.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuit.chatdiary.dto.chat.ChatWebSocketRequestDTO;
import com.kuit.chatdiary.domain.Chat;
import com.kuit.chatdiary.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatWebSocketRequestDTO request = objectMapper.readValue(message.getPayload(), ChatWebSocketRequestDTO.class);

        CompletableFuture.runAsync(() -> {
            try {
                int status = chatService.processUserMessage(request.getUserId(), request.getContent(), request.getChatType());
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(status)));
            } catch (Exception e) {
                log.error("User message processing failed", e);
                sendErrorMessage(session, "User message processing error");
            }
        });

        CompletableFuture.runAsync(() -> {
            try {
                Chat gptResponse = chatService.processGptMessage(request.getUserId(), request.getContent(), request.getSelectedModel(), request.getChatType());
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(gptResponse)));
            } catch (Exception e) {
                log.error("GPT message processing failed", e);
                sendErrorMessage(session, "GPT message processing error");
            }
        });
    }

    private void sendErrorMessage(WebSocketSession session, String errorMessage) {
        try {
            session.sendMessage(new TextMessage(errorMessage));
        } catch (IOException e) {
            log.error("Error sending error message", e);
        }
    }
}
