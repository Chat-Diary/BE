package com.kuit.chatdiary.service.calendar;

import com.kuit.chatdiary.domain.Sender;
import com.kuit.chatdiary.dto.CalendarInquiryResponse;
import org.springframework.stereotype.Service;
import com.kuit.chatdiary.repository.CalendarInquiryRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalendarInquiryService {

    private final CalendarInquiryRepository calendarInquiryRepository;

    public CalendarInquiryService(CalendarInquiryRepository calendarInquiryRepository) {
        this.calendarInquiryRepository = calendarInquiryRepository;
    }

    public Map<LocalDate, List<CalendarInquiryResponse>> existsChatByMonth(long memberId, YearMonth month) {
        Map<LocalDate, List<CalendarInquiryResponse>> chatExistsByMonth = calendarInquiryRepository.existsChatByMonth(memberId,month);
        return chatExistsByMonth;
    }



}