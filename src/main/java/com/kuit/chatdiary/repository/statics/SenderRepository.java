package com.kuit.chatdiary.repository.statics;

import com.kuit.chatdiary.domain.Sender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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

    public Sender findMostActiveSender(long userId, Date diaryDate) {
        String jpql = "SELECT c.sender FROM chat c " +
                "WHERE c.member.id = :memberId AND c.sender != :user " +
                "AND c.createAt >= :startDate AND c.createAt < :endDate " +
                "GROUP BY c.sender ORDER BY COUNT(c) DESC";
        TypedQuery<Sender> query = em.createQuery(jpql, Sender.class)
                .setParameter("memberId", userId)
                .setParameter("user", Sender.USER)
                .setParameter("startDate", diaryDate.toLocalDate().atStartOfDay())
                .setParameter("endDate", diaryDate.toLocalDate().plusDays(1).atStartOfDay())
                .setMaxResults(1);
        List<Sender> resultList = query.getResultList();
        /** USER 제외 */
        if (!resultList.isEmpty() && resultList.get(0) != Sender.USER) {
            return resultList.get(0);
        } else {
            return null;
        }
    }
}