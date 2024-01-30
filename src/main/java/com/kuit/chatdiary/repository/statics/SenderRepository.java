package com.kuit.chatdiary.repository.statics;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class SenderRepository {

    private final EntityManager em;
    public SenderRepository(EntityManager em) {
        this.em = em;
    }

    public List<Object[]> countChatsBySenderAndDate(Long userId, Date startDate, Date endDate) {
        String sql = "SELECT c.sender, COUNT(*) " +
                "FROM chat c " +
                "WHERE c.user_id = :userId " +
                "AND c.create_at BETWEEN :startDate AND :endDate " +
                "GROUP BY c.sender";

        return em.createNativeQuery(sql)
                .setParameter("userId", userId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}
