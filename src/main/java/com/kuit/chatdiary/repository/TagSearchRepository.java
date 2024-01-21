package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.domain.DiaryTag;
import com.kuit.chatdiary.dto.TagSearchResponse;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TagSearchRepository {
    private final EntityManager em;
    public TagSearchRepository(EntityManager em) {
        this.em = em;
    }
    public List<TagSearchResponse> findByTag(String tagName){
        List<Diary> diaries = em.createQuery("select d from diary d join d.diaryTagList dt where dt.tag.tagName = :tagName", Diary.class)
                .setParameter("tagName",tagName)
                .getResultList();
        return diaries.stream().map(diary -> {
            TagSearchResponse response = new TagSearchResponse(diary); // 생성자를 사용하여 객체 생성
            return response;
        }).collect(Collectors.toList());
    }
}
