package com.kuit.chatdiary.service.diary;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.domain.Photo;
import com.kuit.chatdiary.repository.diary.DiaryInquiryRepository;
import com.kuit.chatdiary.repository.diary.DiaryRepository;
import org.springframework.stereotype.Service;

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
    public List<Diary> getDiaryWithPhotos(Long userId, LocalDate date){
        List<Diary> diaries = diaryInquiryRepository.inquiryDiary(userId, date);
        diaries.forEach(diary -> {
                diary.getPhotoList().size();
        });
        return diaries;
    }

    public List<Diary> getMonthlyDiaryPhotos(Long userId, int year,int month){
        LocalDate firstDayOfMonth =LocalDate.of(year,month,1);
        LocalDate lastDayOfMonth =firstDayOfMonth.plusMonths(1).minusDays(1);

        List<Diary> diaries = diaryInquiryRepository.inquiryDiaryRange(userId,firstDayOfMonth,lastDayOfMonth);
        diaries.forEach(diary -> diary.getPhotoList().size());
        return diaries;
    }

}
