package com.kuit.chatdiary.repository.diary;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.dto.diary.DiaryListResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TemporalType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DiaryListRepository {
    private final EntityManager em;
    public DiaryListRepository(EntityManager em){
        this.em=em;
    }


    public List<DiaryListResponseDTO> inquiryDiary(Long userId, LocalDate diaryDate) {
        return em.createQuery("SELECT d FROM diary d LEFT JOIN FETCH d.diaryTagList WHERE d.member.userId = :userId AND d.diaryDate = :diaryDate", DiaryListResponseDTO.class)
                .setParameter("userId", userId)
                .setParameter("diaryDate", diaryDate.toString())
                .getResultList();
    }

    public List<DiaryListResponseDTO> inquiryDiaryRange(Long userId, Date startDate, Date endDate) {
        List<Diary> diaries=em.createQuery("SELECT d FROM diary d LEFT JOIN FETCH d.diaryTagList WHERE d.member.userId = :userId AND d.diaryDate BETWEEN :startDate AND :endDate", Diary.class)
                .setParameter("userId", userId)
                .setParameter("startDate", startDate, TemporalType.DATE)
                .setParameter("endDate", endDate,TemporalType.DATE)
                .getResultList();
        
        return diaries.stream()
                .map(DiaryListResponseDTO::new)
                .collect(Collectors.toList());

    }


}