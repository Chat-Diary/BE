package com.kuit.chatdiary.repository.diary;

import com.kuit.chatdiary.domain.Tag;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagPoolRepository {

    private final EntityManager em;

    public TagPoolRepository(EntityManager em) {
        this.em = em;
    }

    public List<Tag> findAllTags() {
        String jpql = "SELECT t FROM tag t";
        return em.createQuery(jpql, Tag.class).getResultList();
    }
}
