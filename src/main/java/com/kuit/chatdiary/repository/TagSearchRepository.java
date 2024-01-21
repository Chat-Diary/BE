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

    /**
     * HAVING COUNT 안쓰면 일부만 포함해도 반환이 되어서..
     * */
    public List<TagSearchResponse> findByTag(List<String> tagNames) {
        String jpql = "SELECT d FROM diary d WHERE d.id IN " +
                "(SELECT dt.diary.id FROM diarytag dt WHERE dt.tag.tagName IN :tagNames " +
                "GROUP BY dt.diary.id HAVING COUNT(DISTINCT dt.tag) = :tagCount)";

        List<Diary> diaries = em.createQuery(jpql, Diary.class)
                .setParameter("tagNames", tagNames)
                .setParameter("tagCount", (long) tagNames.size())
                .getResultList();

        return diaries.stream()
                .map(TagSearchResponse::new)
                .collect(Collectors.toList());
    }

}
