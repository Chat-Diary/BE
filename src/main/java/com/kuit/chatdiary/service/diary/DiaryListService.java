package com.kuit.chatdiary.service.diary;

import com.kuit.chatdiary.dto.diary.DiaryListResponseDTO;
import com.kuit.chatdiary.repository.diary.DiaryListRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class DiaryListService {

    private final DiaryListRepository diaryListRepository;

    public DiaryListService(DiaryListRepository diaryListRepository){
        this.diaryListRepository = diaryListRepository;
    }

    public List<DiaryListResponseDTO> getMonthlyDiaryPhotos(Long userId, int year, int month){
        LocalDate firstDayOfMonthLocal = LocalDate.of(year, month, 1);
        LocalDate lastDayOfMonthLocal = firstDayOfMonthLocal.plusMonths(1).minusDays(1);

        Date firstDayOfMonth = Date.valueOf(firstDayOfMonthLocal);
        Date lastDayOfMonth = Date.valueOf(lastDayOfMonthLocal);

        return diaryListRepository.inquiryDiaryRange(userId, firstDayOfMonth, lastDayOfMonth);
    }
}
