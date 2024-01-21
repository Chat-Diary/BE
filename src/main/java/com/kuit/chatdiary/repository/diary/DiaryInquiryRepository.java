package com.kuit.chatdiary.repository.diary;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.dto.diary.DiaryInquiryResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TemporalType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DiaryInquiryRepository {
    private final EntityManager em;
    public  DiaryInquiryRepository(EntityManager em){
        this.em=em;
    }


    public List<DiaryInquiryResponse> inquiryDiary(Long userId, LocalDate diaryDate) {
        return em.createQuery("SELECT d FROM diary d LEFT JOIN FETCH d.diaryTagList WHERE d.member.userId = :userId AND d.diaryDate = :diaryDate", DiaryInquiryResponse.class)
                .setParameter("userId", userId)
                .setParameter("diaryDate", diaryDate.toString())
                .getResultList();
    }

    public List<DiaryInquiryResponse> inquiryDiaryRange(Long userId, Date startDate, Date endDate) {
        List<Diary> diaries=em.createQuery("SELECT d FROM diary d LEFT JOIN FETCH d.diaryTagList WHERE d.member.userId = :userId AND d.diaryDate BETWEEN :startDate AND :endDate", Diary.class)
                .setParameter("userId", userId)
                .setParameter("startDate", startDate, TemporalType.DATE)
                .setParameter("endDate", endDate,TemporalType.DATE)
                .getResultList();
        
        return diaries.stream()
                .map(DiaryInquiryResponse::new)
                .collect(Collectors.toList());

    }


}