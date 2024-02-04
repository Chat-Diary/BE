package com.kuit.chatdiary.controller;

import com.kuit.chatdiary.dto.chat.ChatSenderStatisticsResponseDTO;
import com.kuit.chatdiary.dto.diary.DateRangeDTO;
import com.kuit.chatdiary.service.chat.SenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class SenderController {

    private final SenderService senderService;

    public SenderController(SenderService senderService) {
        this.senderService = senderService;
    }

    @GetMapping("/sender")
    public ResponseEntity<ChatSenderStatisticsResponseDTO> getSenderStatistics(
            @RequestParam("memberId") Long memberId,
            @RequestParam("type") String type) {
        DateRangeDTO dateRange = senderService.staticsType(type);
        ChatSenderStatisticsResponseDTO senderStatistics = senderService.calculateSenderStatistics(memberId, dateRange.getStartDate(), dateRange.getEndDate());
        return ResponseEntity.ok(senderStatistics);
    }

}
