package com.kuit.chatdiary.service.calendar;

import com.kuit.chatdiary.domain.Sender;
import com.kuit.chatdiary.dto.CalendarInquiryResponse;
import com.kuit.chatdiary.repository.CalendarInquiryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CalendarInquiryService {

    private final CalendarInquiryRepository calendarInquiryRepository;

    public CalendarInquiryService(CalendarInquiryRepository calendarInquiryRepository) {
        this.calendarInquiryRepository = calendarInquiryRepository;
    }

    public List<CalendarInquiryResponse> existsChatByDate(long memberId, LocalDate day){
        Map<Sender, Boolean> senderExistsMap = calendarInquiryRepository.existsChatByDate(memberId, day);
        List<CalendarInquiryResponse> senderChatExistsList = new ArrayList<>();

        for (Map.Entry<Sender, Boolean> entry : senderExistsMap.entrySet()) {
            senderChatExistsList.add(new CalendarInquiryResponse(entry.getKey(), entry.getValue()));
        }

        return senderChatExistsList;
    }

}
