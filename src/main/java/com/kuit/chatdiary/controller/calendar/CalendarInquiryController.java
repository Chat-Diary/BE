package com.kuit.chatdiary.controller.calendar;

import com.kuit.chatdiary.dto.CalendarInquiryResponseDTO;
import com.kuit.chatdiary.service.calendar.CalendarInquiryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("chat")
@Slf4j
public class CalendarInquiryController {
    private final CalendarInquiryService calendarInquiryService;
    public CalendarInquiryController(CalendarInquiryService calendarInquiryService) {
        this.calendarInquiryService = calendarInquiryService;
    }
    @GetMapping("/chat")
    public ResponseEntity<List<CalendarInquiryResponseDTO>> getChatExistsByMonth(
            @RequestParam("memberId") long memberId,
            @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {

        List<CalendarInquiryResponseDTO> chatExistsByMonth = calendarInquiryService.getCalendarInquiryResponses(memberId,month);
        return ResponseEntity.ok(chatExistsByMonth);
    }
}