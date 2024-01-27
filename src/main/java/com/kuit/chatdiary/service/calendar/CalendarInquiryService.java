package com.kuit.chatdiary.service.calendar;

import com.kuit.chatdiary.domain.Sender;
import com.kuit.chatdiary.dto.CalendarInquiryResponse;
import com.kuit.chatdiary.dto.DateInquiryResponse;
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

    public List<DateInquiryResponse> existsChatByMonth(long memberId, YearMonth month) {
        Map<LocalDate, List<CalendarInquiryResponse>> chatExistsByMonth = calendarInquiryRepository.existsChatByMonth(memberId, month);

        List<DateInquiryResponse> list = new ArrayList<>();
        /**각 요소들에 대해 반복*/
        for(Map.Entry<LocalDate, List<CalendarInquiryResponse>> entry : chatExistsByMonth.entrySet()) {
            List<LocalDate> dates = new ArrayList<>();
            /**키 값 받아서 리스트에 저장*/
            dates.add(entry.getKey());
            DateInquiryResponse response = new DateInquiryResponse(dates, entry.getValue());
            list.add(response);
        }
        return list;
    }




}