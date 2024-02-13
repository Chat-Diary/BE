package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.domain.Sender;
import com.kuit.chatdiary.dto.CalendarInquiryResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class CalendarInquiryRepository {
    private final EntityManager em;

    public CalendarInquiryRepository(EntityManager em) {
        this.em = em;
    }


    public boolean existsDiary(long memberId, Date diaryDate) {
        String jpql = "SELECT COUNT(d) FROM diary d WHERE d.member.id = :memberId AND d.diaryDate = :diaryDate";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class)
                .setParameter("memberId", memberId)
                .setParameter("diaryDate", diaryDate);
        Long count = query.getSingleResult();
        return count > 0;
    }

    public Sender findMostActiveSender(long memberId, Date diaryDate) {
        String jpql = "SELECT c.sender FROM chat c " +
                "WHERE c.member.id = :memberId AND c.createAt >= :startDate AND c.createAt < :endDate " +
                "GROUP BY c.sender ORDER BY COUNT(c) DESC";
        TypedQuery<Sender> query = em.createQuery(jpql, Sender.class)
                .setParameter("memberId", memberId)
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
