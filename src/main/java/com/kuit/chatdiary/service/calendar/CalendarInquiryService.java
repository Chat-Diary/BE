package com.kuit.chatdiary.service.calendar;

import com.kuit.chatdiary.domain.Sender;
import com.kuit.chatdiary.repository.CalendarInquiryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class CalendarInquiryService {

    private final CalendarInquiryRepository calendarInquiryRepository;

    public CalendarInquiryService(CalendarInquiryRepository calendarInquiryRepository) {
        this.calendarInquiryRepository = calendarInquiryRepository;
    }

    public Map<Sender,Boolean> existsChatByDate(long memberId, LocalDate day){
        return calendarInquiryRepository.existsChatByDate(memberId,day);
    }
}
