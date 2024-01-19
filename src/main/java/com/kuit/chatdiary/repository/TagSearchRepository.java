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
            TagSearchResponse response = new TagSearchResponse();
            response.setDiaryId(diary.getDiaryId());
            response.setTitle(diary.getTitle());
            response.setContent(diary.getContent());
            response.setDiaryDate(diary.getDiaryDate());
            response.setCreateAt(diary.getCreateAt());
            response.setUpdateAt(diary.getUpdateAt());
            response.setStatus(diary.getStatus());
            response.setTagList(diary.getDiaryTagList().stream().map(diaryTag -> diaryTag.getTag().getTagName()).collect(Collectors.toList()));
            response.setPhotoList(diary.getPhotoList());
            return response;
        }).collect(Collectors.toList());
    }

}
