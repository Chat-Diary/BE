package com.kuit.chatdiary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuit.chatdiary.request.ChatRequest;
import com.kuit.chatdiary.domain.Chat;
import com.kuit.chatdiary.service.ChatService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping
    public ResponseEntity<Chat> postMessage(@RequestBody ChatRequest request) throws JsonProcessingException {
        Chat response = chatService.processMessage(request.getUserId(), request.getContent(), request.getSelectedModel());
        return ResponseEntity.ok(response);
    }
}