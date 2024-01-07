package com.kuit.chatdiary.controller;

import com.kuit.chatdiary.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequiredArgsConstructor
public class OpenAIController {

    @Autowired
    private final OpenAIService openAIService;

    @GetMapping("/testGpt")
    public ResponseEntity<?> openAI() {
        try {
            return ResponseEntity.ok().body(openAIService.getCompletion("안녕"));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
