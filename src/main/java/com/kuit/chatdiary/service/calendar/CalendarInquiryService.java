package com.kuit.chatdiary.service.calendar;

import com.kuit.chatdiary.domain.Sender;
import com.kuit.chatdiary.dto.CalendarInquiryResponse;
import org.springframework.stereotype.Service;
import com.kuit.chatdiary.repository.CalendarInquiryRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CalendarInquiryService {

    private final CalendarInquiryRepository calendarInquiryRepository;

    public CalendarInquiryService(CalendarInquiryRepository calendarInquiryRepository) {
        this.calendarInquiryRepository = calendarInquiryRepository;
    }

    public Map<LocalDate, Map<Sender, Boolean>> existsChatByMonth(long memberId, YearMonth month) {
        return calendarInquiryRepository.existsChatByMonth(memberId, month);
    }



}