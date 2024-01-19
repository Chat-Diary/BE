package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.domain.Chat;
import com.kuit.chatdiary.domain.Sender;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CalendarInquiryRepository {
    private final EntityManager em;

    public CalendarInquiryRepository(EntityManager em) {
        this.em = em;
    }

    public Map<Sender, Boolean> existsChatByDate(long memberId, LocalDate day){
        List<Object[]> results = em.createQuery("select c.sender, count(c) from chat c where c.member.userId = :userId and c.createAt between :startOfDay and :endOfDay group by c.sender", Object[].class)
                .setParameter("userId", memberId)
                .setParameter("startOfDay", day.atStartOfDay())
                .setParameter("endOfDay", day.plusDays(1).atStartOfDay())
                .getResultList();

        Map<Sender, Boolean> senderExistsMap = new HashMap<>();
        for (Sender sender : Sender.values()) {
            senderExistsMap.put(sender, false);
        }

        for (Object[] result : results) {
            senderExistsMap.put((Sender) result[0], (Long) result[1] > 0);
        }

        return senderExistsMap;
    }
}


