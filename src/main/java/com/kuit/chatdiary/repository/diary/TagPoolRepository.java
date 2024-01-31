package com.kuit.chatdiary.repository.diary;

import com.kuit.chatdiary.domain.DiaryPhoto;
import com.kuit.chatdiary.domain.DiaryTag;
import com.kuit.chatdiary.domain.Tag;
import com.kuit.chatdiary.dto.diary.DiaryListResponseDTO;
import com.kuit.chatdiary.dto.diary.TagPoolResponseDTO;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TagPoolRepository {

    private final EntityManager em;

    public TagPoolRepository(EntityManager em) {
        this.em = em;
    }

    public List<TagPoolResponseDTO> findAllTags() {
        String jpql = "SELECT t FROM tag t";
        List<Tag> tags = em.createQuery(jpql, Tag.class).getResultList();
        return tags.stream().map(tag -> new TagPoolResponseDTO(
                tag.getTagId(),
                tag.getCategory(),
                tag.getTagName()
        )).collect(Collectors.toList());
    }
}
