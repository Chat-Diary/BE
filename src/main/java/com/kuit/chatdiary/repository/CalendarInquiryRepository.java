package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.domain.Sender;
import com.kuit.chatdiary.dto.CalendarInquiryResponse;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class CalendarInquiryRepository {
    private final EntityManager em;

    public CalendarInquiryRepository(EntityManager em) {
        this.em = em;
    }

    public Map<LocalDate, List<CalendarInquiryResponse>> existsChatByMonth(long memberId, YearMonth month) {
        LocalDate startOfMonth = month.atDay(1);
        LocalDate endOfMonth = month.atEndOfMonth();
        Map<LocalDate, List<CalendarInquiryResponse>> chatExistsByMonth = new HashMap<>();

        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
            List<CalendarInquiryResponse> dailyResponses = new ArrayList<>();
            for (Sender sender : Sender.values()) {
                dailyResponses.add(new CalendarInquiryResponse(sender, false));
            }
            chatExistsByMonth.put(date, dailyResponses);
        }

        List<Object[]> results = em.createQuery(
                        "SELECT CAST(c.createAt AS date), c.sender, COUNT(*) " +
                                "FROM chat c WHERE c.member.userId = :userId " +
                                "AND c.createAt BETWEEN :startOfMonth AND :endOfMonth " +
                                "GROUP BY CAST(c.createAt AS date), c.sender", Object[].class)
                .setParameter("userId", memberId)
                .setParameter("startOfMonth", startOfMonth.atStartOfDay())
                .setParameter("endOfMonth", endOfMonth.atTime(LocalTime.MAX))
                .getResultList();

        log.info("Query results: {}", results);
        for (Object[] result : results) {
            LocalDate date = ((java.sql.Date) result[0]).toLocalDate();
            Sender sender = (Sender) result[1];
            Long count = (Long) result[2];

            List<CalendarInquiryResponse> dailyResponses = chatExistsByMonth.get(date);

            if (dailyResponses != null) {
                for (CalendarInquiryResponse response : dailyResponses) {
                    if (response.getSender() == sender) {
                        response.setExists(count > 0);
                        break;
                    }
                }
            }
        }
        return chatExistsByMonth;
    }
}
