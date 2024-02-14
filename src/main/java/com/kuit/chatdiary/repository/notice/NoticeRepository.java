package com.kuit.chatdiary.repository.notice;

import com.kuit.chatdiary.domain.Notice;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoticeRepository {
    private final EntityManager em;

    public NoticeRepository(EntityManager em) {
        this.em = em;
    }

    public List<Notice> findAll() {
        return em.createQuery("select n from notice n", Notice.class)
                .getResultList();
    }

    public Notice findOne(Long id) {
        return em.find(Notice.class, id);
    }


}
