package com.kuit.chatdiary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuit.chatdiary.dto.chat.ChatRequestDTO;
import com.kuit.chatdiary.domain.Chat;
import com.kuit.chatdiary.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/post")
    public ResponseEntity<Chat> postMessage(@RequestBody ChatRequestDTO request) throws JsonProcessingException {
        log.info(String.valueOf(request));
        log.info("request.getUserId(): ", request.getUserId());
        Chat response = chatService.processMessage(request.getUserId(), request.getContent(), request.getSelectedModel());
        return ResponseEntity.ok(response);
    }
}