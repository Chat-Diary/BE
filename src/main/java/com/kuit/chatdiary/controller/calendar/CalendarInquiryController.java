package com.kuit.chatdiary.controller.calendar;

import com.kuit.chatdiary.domain.Sender;
import com.kuit.chatdiary.dto.CalendarInquiryResponse;
import com.kuit.chatdiary.service.calendar.CalendarInquiryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("chat")
@Slf4j
public class CalendarInquiryController {
    private final CalendarInquiryService calendarInquiryService;
    public CalendarInquiryController(CalendarInquiryService calendarInquiryService) {
        this.calendarInquiryService = calendarInquiryService;
    }
    @GetMapping("/chat-exists-month")
    public ResponseEntity<Map<LocalDate, List<CalendarInquiryResponse>>> getChatExistsByMonth(
            @RequestParam("memberId") long memberId,
            @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        Map<LocalDate, List<CalendarInquiryResponse>> chatExistsByMonth = calendarInquiryService.existsChatByMonth(memberId, month);
//        log.info("Result: {}", chatExistsByMonth);
        return ResponseEntity.ok(chatExistsByMonth);
    }
}