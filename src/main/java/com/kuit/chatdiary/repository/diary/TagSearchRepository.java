package com.kuit.chatdiary.repository.diary;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.dto.diary.TagInfoDTO;
import com.kuit.chatdiary.dto.diary.TagSearchResponseDTO;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TagSearchRepository {
    private final EntityManager em;

    public TagSearchRepository(EntityManager em) {
        this.em = em;
    }

    public List<TagSearchResponseDTO> findByTag(List<String> tagName, Long userId) {
        // 다이어리 검색 쿼리
        String diarySql = "SELECT d.diary_id, d.title, d.diary_date " +
                "FROM diary d " +
                "INNER JOIN diarytag dt ON d.diary_id = dt.diary_id " +
                "INNER JOIN tag t ON dt.tag_id = t.tag_id " +
                "WHERE t.tag_name IN :tagNames AND d.user_id = :userId " +
                "GROUP BY d.diary_id, d.title, d.diary_date " +
                "HAVING COUNT(DISTINCT t.tag_id) = :tagCount";

        List<Object[]> diaryResults = em.createNativeQuery(diarySql)
                .setParameter("tagNames", tagName)
                .setParameter("userId", userId)
                .setParameter("tagCount", Long.valueOf(tagName.size()))
                .getResultList();

        List<TagSearchResponseDTO> responses = new ArrayList<>();
        for (Object[] diaryResult : diaryResults) {
            Long diaryId = ((Number) diaryResult[0]).longValue();
            String title = (String) diaryResult[1];
            Date diaryDate = (Date) diaryResult[2];

            // 사진 URL 검색 쿼리
            String photoSql = "SELECT p.image_url " +
                    "FROM diaryphoto dp " +
                    "INNER JOIN photo p ON dp.photo_id = p.photo_id " +
                    "WHERE dp.diary_id = :diaryId";
            List<String> photoUrls = em.createNativeQuery(photoSql)
                    .setParameter("diaryId", diaryId)
                    .getResultList();

            // 태그 리스트 검색 쿼리
            String tagSql = "SELECT t.tag_id, t.tag_name " +
                    "FROM diarytag dt " +
                    "INNER JOIN tag t ON dt.tag_id = t.tag_id " +
                    "WHERE dt.diary_id = :diaryId";
            List<Object[]> tagResults = em.createNativeQuery(tagSql)
                    .setParameter("diaryId", diaryId)
                    .getResultList();

            List<TagInfoDTO> tagList = tagResults.stream()
                    .map(tagResult -> new TagInfoDTO(
                            ((Number) tagResult[0]).longValue(),
                            (String) tagResult[1]))
                    .collect(Collectors.toList());

            TagSearchResponseDTO response = new TagSearchResponseDTO();
            response.setDiaryId(diaryId);
            response.setTitle(title);
            response.setDiaryDate(diaryDate);
            response.setPhotoUrls(photoUrls);
            response.setTagList(tagList);

            responses.add(response);
        }

        return responses;
    }
}
