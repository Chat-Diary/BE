package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.dto.GetDiaryDetailResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Repository
public class DiaryRepository {

    private final EntityManager em;


    public List<GetDiaryDetailResponse> showDiaryDetail(Long userId, String day){

        log.info("[DiaryRepository.showDiaryDetail]");

        return em.createQuery("SELECT d.da, d.title, p.imageUrl, d.content, dt.tag.tagId FROM diary d LEFT OUTER JOIN d.diaryTagList dt LEFT OUTER JOIN d.photoList p WHERE d.member.userId = :user_id AND d.day = :day", GetDiaryDetailResponse.class)
                .setParameter("user_id", userId).setParameter("day", day).getResultList();
    }

}