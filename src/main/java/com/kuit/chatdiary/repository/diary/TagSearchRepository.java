package com.kuit.chatdiary.repository.diary;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.domain.DiaryPhoto;
import com.kuit.chatdiary.domain.DiaryTag;
import com.kuit.chatdiary.dto.diary.DiaryListResponseDTO;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TagSearchRepository {
    private final EntityManager em;

    public TagSearchRepository(EntityManager em) {
        this.em = em;
    }

    public List<DiaryListResponseDTO> findByTag(List<String> tagNames, Long userId) {
        String diaryJpql = "SELECT DISTINCT d FROM diarytag dt " +
                "JOIN dt.diary d " +
                "JOIN dt.tag t " +
                "WHERE t.tagName IN :tagNames AND d.member.userId = :userId GROUP BY d HAVING COUNT(t) = :tagCount";

        List<Diary> diaries = em.createQuery(diaryJpql, Diary.class)
                .setParameter("userId", userId)
                .setParameter("tagNames", tagNames)
                .setParameter("tagCount", tagNames.size())
                .getResultList();


        return diaries.stream().map(diary -> {
            List<DiaryPhoto> diaryPhotos = em.createQuery("SELECT dp FROM diaryphoto dp WHERE dp.diary = :diary", DiaryPhoto.class)
                    .setParameter("diary", diary)
                    .getResultList();
            List<DiaryTag> diaryTags = em.createQuery("SELECT dt FROM diarytag dt WHERE dt.diary = :diary", DiaryTag.class)
                    .setParameter("diary", diary)
                    .getResultList();

            return new DiaryListResponseDTO(diary, diaryPhotos, diaryTags);
        }).collect(Collectors.toList());
    }

}
