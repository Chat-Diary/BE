package com.kuit.chatdiary.service.chat;

import com.kuit.chatdiary.domain.Sender;
import com.kuit.chatdiary.dto.chat.ChatSenderDetailResponseDTO;
import com.kuit.chatdiary.dto.chat.ChatSenderStatisticsResponseDTO;
import com.kuit.chatdiary.dto.diary.DateRangeDTO;
import com.kuit.chatdiary.repository.statics.SenderRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SenderService {

    private final SenderRepository senderRepository;

    public SenderService(SenderRepository senderRepository) {
        this.senderRepository = senderRepository;
    }
    public ChatSenderStatisticsResponseDTO calculateSenderStatistics(Long memberId, Date startDate, Date endDate) {
        List<Object[]> chatStatistics = senderRepository.countChatsBySenderAndDate(memberId, startDate, endDate);

        Map<Sender, Long> chatCountMap = new EnumMap<>(Sender.class);
        long totalChats = 0;
        for (Object[] result : chatStatistics) {
            Sender sender = Sender.valueOf((String) result[0]);
            Long count = (Long) result[1];
            if (sender != Sender.USER) { /** USER가 아닌 경우에만 처리 */
                chatCountMap.merge(sender, count, Long::sum);
                totalChats += count;
            }
        }

        final long finalTotalChats = totalChats;
        List<ChatSenderDetailResponseDTO> details = chatCountMap.entrySet().stream()
                .filter(entry -> entry.getKey() != Sender.USER)
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) /** 정렬 */
                .map(entry -> new ChatSenderDetailResponseDTO(entry.getKey(), entry.getValue(), calculatePercent(entry.getValue(), finalTotalChats)))
                .collect(Collectors.toList());

        return new ChatSenderStatisticsResponseDTO(startDate, endDate, details);
    }

    private double calculatePercent(long count, long totalChats) {
        if (totalChats == 0) return 0.0;
        return Math.round((double) count / totalChats * 1000) / 10.0;
    }

    public DateRangeDTO staticsType(String type) {
        LocalDate now = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate;

        switch (type) {
            case "weekly":
                startDate = now.with(java.time.DayOfWeek.MONDAY);
                endDate = now.with(java.time.DayOfWeek.SUNDAY);
                break;
            case "monthly":
                startDate = now.withDayOfMonth(1);
                endDate = now.withDayOfMonth(now.lengthOfMonth());
                break;
            case "yearly":
                startDate = now.withDayOfYear(1);
                endDate = now.withDayOfYear(now.lengthOfYear());
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }

        return new DateRangeDTO(Date.valueOf(startDate), Date.valueOf(endDate));
    }
}
