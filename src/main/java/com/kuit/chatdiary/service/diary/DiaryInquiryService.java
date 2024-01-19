package com.kuit.chatdiary.service.diary;

import com.kuit.chatdiary.dto.diary.DiaryInquiryResponse;
import com.kuit.chatdiary.repository.diary.DiaryInquiryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class DiaryInquiryService {

    private final DiaryInquiryRepository diaryInquiryRepository;

    public DiaryInquiryService(DiaryInquiryRepository diaryInquiryRepository){
        this.diaryInquiryRepository=diaryInquiryRepository;
    }

    /**
     * diary.getPhotoList().size(); 를 통해 jpa가 각 객체의 사진 목록 로드
     * */
    public List<DiaryInquiryResponse> getDiaryWithPhotos(Long userId, LocalDate date){
        List<DiaryInquiryResponse> diaries = diaryInquiryRepository.inquiryDiary(userId, date);
        diaries.forEach(diary -> {
            diary.getPhotoList().size();
        });
        return diaries;
    }

    public List<DiaryInquiryResponse> getMonthlyDiaryPhotos(Long userId, int year,int month){
        LocalDate firstDayOfMonthLocal =LocalDate.of(year,month,1);
        LocalDate lastDayOfMonthLocal =firstDayOfMonthLocal.plusMonths(1).minusDays(1);

        Date firstDayOfMonth = Date.valueOf(firstDayOfMonthLocal);
        Date lastDayOfMonth = Date.valueOf(lastDayOfMonthLocal);

        List<DiaryInquiryResponse> diaries = diaryInquiryRepository.inquiryDiaryRange(userId, firstDayOfMonth, lastDayOfMonth);
        diaries.forEach(diary -> diary.getPhotoList().size());
        return diaries;
    }

}