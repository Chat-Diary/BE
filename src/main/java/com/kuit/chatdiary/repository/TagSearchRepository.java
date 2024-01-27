package com.kuit.chatdiary.repository;

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

    /**
     * HAVING COUNT 안쓰면 일부만 포함해도 반환이 되어서..
     * */
    public List<TagSearchResponseDTO> findByTag(List<String> tagNames) {
        String jpql = "SELECT d FROM diary d WHERE d.id IN " +
                "(SELECT dt.diary.id FROM diarytag dt WHERE dt.tag.tagName IN :tagNames " +
                "GROUP BY dt.diary.id HAVING COUNT(DISTINCT dt.tag) = :tagCount)";

        List<Diary> diaries = em.createQuery(jpql, Diary.class)
                .setParameter("tagNames", tagNames)
                .setParameter("tagCount", (long) tagNames.size())
                .getResultList();

        return diaries.stream()
                .map(TagSearchResponseDTO::new)
                .collect(Collectors.toList());
    }

}
