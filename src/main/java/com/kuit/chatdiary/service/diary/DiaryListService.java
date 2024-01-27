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
        this.diaryListRepository=diaryListRepository;
    }

    /**
     * diary.getPhotoList().size(); 를 통해 jpa가 각 객체의 사진 목록 로드
     * */
    public List<DiaryListResponseDTO> getDiaryWithPhotos(Long userId, LocalDate date){
        List<DiaryListResponseDTO> diaries = diaryListRepository.inquiryDiary(userId, date);
        diaries.forEach(diary -> {
            diary.getPhotoList().size();
        });
        return diaries;
    }

    public List<DiaryListResponseDTO> getMonthlyDiaryPhotos(Long userId, int year, int month){
        LocalDate firstDayOfMonthLocal =LocalDate.of(year,month,1);
        LocalDate lastDayOfMonthLocal =firstDayOfMonthLocal.plusMonths(1).minusDays(1);

        Date firstDayOfMonth = Date.valueOf(firstDayOfMonthLocal);
        Date lastDayOfMonth = Date.valueOf(lastDayOfMonthLocal);

        List<DiaryListResponseDTO> diaries = diaryListRepository.inquiryDiaryRange(userId, firstDayOfMonth, lastDayOfMonth);
        diaries.forEach(diary -> diary.getPhotoList().size());
        return diaries;
    }

}