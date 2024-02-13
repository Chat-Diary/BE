package com.kuit.chatdiary.service.calendar;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.domain.Sender;
import com.kuit.chatdiary.dto.CalendarInquiryResponseDTO;
import com.kuit.chatdiary.repository.diary.DiaryListRepository;
import com.kuit.chatdiary.repository.statics.SenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarInquiryService {

    private final DiaryListRepository diaryListRepository;
    private final SenderRepository senderRepository;

    public boolean existsDiary(long memberId, java.util.Date diaryDate) {
        List<Diary> diaries = diaryListRepository.getDiaries(memberId, diaryDate, diaryDate);
        return !diaries.isEmpty();
    }

    public List<CalendarInquiryResponseDTO> getCalendarInquiryResponses(long memberId, LocalDate startDate, LocalDate endDate) {
        List<CalendarInquiryResponseDTO> responses = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            Date diaryDate = Date.valueOf(date);
            boolean exists =  existsDiary(memberId, diaryDate);
            Sender sender =  senderRepository.findMostActiveSender(memberId, diaryDate);
            responses.add(new CalendarInquiryResponseDTO(diaryDate.toString(), sender, exists));
        }

        return responses;
    }


    public List<CalendarInquiryResponseDTO> getCalendarInquiryResponses(long memberId, YearMonth month) {
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();
        return getCalendarInquiryResponses(memberId, startDate, endDate);
    }
}
