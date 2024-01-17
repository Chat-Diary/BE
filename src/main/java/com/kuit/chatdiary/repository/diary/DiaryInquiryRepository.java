package com.kuit.chatdiary.repository.diary;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.domain.Photo;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DiaryInquiryRepository implements DiaryRepository {
    private final EntityManager em;
    public  DiaryInquiryRepository(EntityManager em){
        this.em=em;
    }


    public List<Diary> inquiryDiary(Long userId, LocalDate diaryDate) {
        return em.createQuery("SELECT d FROM diary d LEFT JOIN FETCH d.diaryTagList WHERE d.member.userId = :userId AND d.diaryDate = :diaryDate", Diary.class)
                .setParameter("userId", userId)
                .setParameter("diaryDate", diaryDate.toString())
                .getResultList();
    }

    public List<Diary> inquiryDiaryRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return em.createQuery("SELECT d FROM diary d LEFT JOIN FETCH d.diaryTagList WHERE d.member.userId = :userId AND d.diaryDate BETWEEN :startDate AND :endDate", Diary.class)
                .setParameter("userId", userId)
                .setParameter("startDate", startDate.toString())
                .setParameter("endDate", endDate.toString())
                .getResultList();
    }

}
