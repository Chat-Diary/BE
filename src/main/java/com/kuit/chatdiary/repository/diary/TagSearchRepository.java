package com.kuit.chatdiary.repository.diary;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.dto.diary.TagInfoDTO;
import com.kuit.chatdiary.dto.diary.TagSearchResponseDTO;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagSearchRepository {
    private final EntityManager em;

    public TagSearchRepository(EntityManager em) {
        this.em = em;
    }

    public List<TagSearchResponseDTO> findByTag(List<String> tagName, Long userId) {
        String diaryJpql = "SELECT new com.kuit.chatdiary.dto.diary.TagSearchResponseDTO(d.diaryId, d.title, d.diaryDate) " +
                "FROM diary d " +
                "JOIN diarytag dt ON d.diaryId = dt.diary.diaryId " +
                "JOIN tag t ON dt.tag.tagId = t.tagId " +
                "WHERE t.tagName IN :tagNames AND d.member.id = :userId " +
                "GROUP BY d.diaryId, d.title, d.diaryDate " +
                "HAVING COUNT(DISTINCT t.tagId) = :tagCount" ;

        List<TagSearchResponseDTO> responses = em.createQuery(diaryJpql, TagSearchResponseDTO.class)
                .setParameter("tagNames", tagName)
                .setParameter("userId", userId)
                .setParameter("tagCount", Long.valueOf(tagName.size()))
                .getResultList();

        responses.forEach(response -> {
            String photoJpql = "SELECT p.imageUrl FROM diaryphoto dp JOIN dp.photo p WHERE dp.diary.diaryId = :diaryId";
            List<String> photoUrls = em.createQuery(photoJpql, String.class)
                    .setParameter("diaryId", response.getDiaryId())
                    .getResultList();
            response.setPhotoUrls(photoUrls);

            String tagJpql = "SELECT new com.kuit.chatdiary.dto.diary.TagInfoDTO(t.tagId, t.tagName) " +
                    "FROM diarytag dt JOIN dt.tag t WHERE dt.diary.diaryId = :diaryId";
            List<TagInfoDTO> tagList = em.createQuery(tagJpql, TagInfoDTO.class)
                    .setParameter("diaryId", response.getDiaryId())
                    .getResultList();
            response.setTagList(tagList);
        });

        return responses;
    }

}
