package com.kuit.chatdiary.controller.calendar;

import com.kuit.chatdiary.domain.Sender;
import com.kuit.chatdiary.service.calendar.CalendarInquiryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
public class CalendarInquiryController {
    private final CalendarInquiryService calendarInquiryService;
    public CalendarInquiryController(CalendarInquiryService calendarInquiryService) {
        this.calendarInquiryService = calendarInquiryService;
    }
    @GetMapping("/chat-exists")
    public ResponseEntity<Map<Sender, Boolean>> getChatExistsByDate(
            @RequestParam("memberId") long memberId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate day){
        Map<Sender, Boolean> chatExists =calendarInquiryService.existsChatByDate(memberId,day);
        return ResponseEntity.ok(chatExists);
    }


}
