package com.kuit.chatdiary.controller;

import com.kuit.chatdiary.dto.chat.ChatGetResponseDTO;
import com.kuit.chatdiary.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/get")
    public ResponseEntity<List<ChatGetResponseDTO>> getChats(@RequestParam Long userId, @RequestParam Long chatId) {
        List<ChatGetResponseDTO> chats = chatService.getChats(userId, chatId);
        if (chats == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(chatService.getChats(userId, chatId));
    }
}