package com.kuit.chatdiary.controller;

import com.kuit.chatdiary.dto.chat.ChatSenderStaticResponseDTO;
import com.kuit.chatdiary.dto.diary.DateRangeDTO;
import com.kuit.chatdiary.service.chat.SenderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class SenderController {

    private final SenderService senderService;

    public SenderController(SenderService senderService) {
        this.senderService = senderService;
    }

    @GetMapping("/sender")
    public ResponseEntity<List<ChatSenderStaticResponseDTO>> getTagStatistics(
            @RequestParam("memberId") Long memberId,
            @RequestParam("type") String type,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate localDate) {
        DateRangeDTO dateRange = senderService.staticsType(type,localDate);
        List<ChatSenderStaticResponseDTO> tagStatistics = senderService.calculateSenderStatistics(memberId, dateRange.getStartDate()
                ,dateRange.getEndDate());
        return ResponseEntity.ok(tagStatistics);
    }

}
