package com.kuit.chatdiary.repository.diary;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.dto.diary.TagSearchResponseDTO;
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


    /** 지식 한계로 쿼리문 세개를 나눠서.. */
    public List<TagSearchResponseDTO> findByTag(List<String> tagName) {
        int tagCount = tagName.size();
        List<Diary> diaries = em.createQuery(
                        "SELECT d FROM diary d " +
                                "JOIN diarytag dt ON d.diaryId = dt.diary.diaryId " +
                                "JOIN tag t ON dt.tag.tagId = t.tagId " +
                                "WHERE t.tagName IN :tagNames " +
                                "GROUP BY d " +
                                "HAVING COUNT(DISTINCT t) = :tagCount", Diary.class)
                .setParameter("tagNames", tagName)
                .setParameter("tagCount", (long) tagCount)
                .getResultList();

        return diaries.stream().map(diary -> {
            TagSearchResponseDTO response = new TagSearchResponseDTO();
            response.setDiaryId(diary.getDiaryId());
            response.setTitle(diary.getTitle());
            response.setContent(diary.getContent());
            response.setDiaryDate(diary.getDiaryDate());
            response.setCreateAt(diary.getCreateAt());
            response.setUpdateAt(diary.getUpdateAt());
            response.setStatus(diary.getStatus());

            List<String> photoUrls = em.createQuery(
                            "SELECT dp.photo.imageUrl FROM diaryphoto dp WHERE dp.diary.diaryId = :diaryId", String.class)
                    .setParameter("diaryId", diary.getDiaryId())
                    .getResultList();
            response.setPhotoUrls(photoUrls);

            List<String> tagNames = em.createQuery(
                            "SELECT t.tagName FROM diarytag dt JOIN dt.tag t WHERE dt.diary.diaryId = :diaryId", String.class)
                    .setParameter("diaryId", diary.getDiaryId())
                    .getResultList();
            response.setTagList(tagNames);

            return response;
        }).collect(Collectors.toList());
    }


}