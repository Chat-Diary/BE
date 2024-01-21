package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.domain.Chat;
import com.kuit.chatdiary.domain.Sender;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CalendarInquiryRepository {
    private final EntityManager em;
    public CalendarInquiryRepository(EntityManager em) {
        this.em = em;
    }
    public Map<LocalDate, Map<Sender, Boolean>> existsChatByMonth(long memberId, YearMonth month) {
        LocalDate startOfMonth = month.atDay(1);
        LocalDate endOfMonth = month.atEndOfMonth();

        /**
         * 쿼리 넘 길어져서 + 로 붙임요
         * */
        List<Object[]> results = em.createQuery(
                        "SELECT CAST(c.createAt AS date), c.sender " +
                                "FROM chat c WHERE c.member.userId = :userId " +
                                "AND c.createAt BETWEEN :startOfMonth AND :endOfMonth " +
                                "GROUP BY CAST(c.createAt AS date), c.sender", Object[].class)
                .setParameter("userId", memberId)
                .setParameter("startOfMonth", startOfMonth.atStartOfDay())
                .setParameter("endOfMonth", endOfMonth.atTime(LocalTime.MAX))// 하루 끝 포함 자바 time 라이브러리 굿 ㅋㅋ
                .getResultList();

        Map<LocalDate, Map<Sender, Boolean>> chatExistsByMonth = new HashMap<>();

        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
            Map<Sender, Boolean> dailySenderExists = new HashMap<>();
            for (Sender sender : Sender.values()) {
                dailySenderExists.put(sender, false);
            }
            chatExistsByMonth.put(date, dailySenderExists);
        }

        for (Object[] result : results) {
            LocalDate date = ((LocalDateTime) result[0]).toLocalDate();
            Sender sender = (Sender) result[1];
            Long count = (Long) result[2];
            chatExistsByMonth.get(date).put(sender, count > 0);
        }

        return chatExistsByMonth;
    }


}

