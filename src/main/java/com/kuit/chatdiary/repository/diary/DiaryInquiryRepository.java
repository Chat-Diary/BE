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

    public List<Diary> inquiryDiary(Long userId, LocalDate day) {
        return em.createQuery("SELECT d FROM diary d LEFT JOIN FETCH d.diaryTagList WHERE d.member.user_id = :userId AND d.day = :day", Diary.class)
                .setParameter("userId", userId)
                .setParameter("day", day.toString())
                .getResultList();
    }

}
