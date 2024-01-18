package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.dto.GetDiaryDetailResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TemporalType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Repository
public class DiaryRepository {

    private final EntityManager em;


    public List<GetDiaryDetailResponse> showDiaryDetail (Long userId, Date diaryDate) throws ParseException {

        log.info("[DiaryRepository.showDiaryDetail]");

        return em.createQuery("SELECT d.diaryDate, p.imageUrl, d.title, d.content, t.tagName"+
                " FROM diary d LEFT"+
                " OUTER JOIN d.diaryTagList dt"+
                " LEFT OUTER JOIN d.photoList p" +
                " LEFT OUTER JOIN dt.tag t"+
                " WHERE d.member.userId = :user_id AND d.diaryDate = :diary_date", GetDiaryDetailResponse.class)
                .setParameter("user_id", userId).setParameter("diary_date", diaryDate).getResultList();

    }

}