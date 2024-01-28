package com.kuit.chatdiary.service.chat;

import com.kuit.chatdiary.domain.Sender;
import com.kuit.chatdiary.dto.chat.ChatSenderStaticResponseDTO;
import com.kuit.chatdiary.dto.diary.DateRangeDTO;
import com.kuit.chatdiary.repository.SenderRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class SenderService {

    public final SenderRepository senderRepository;

    public SenderService(SenderRepository senderRepository) {
        this.senderRepository = senderRepository;
    }
    public List<ChatSenderStaticResponseDTO> calculateSenderStatistics(Long memberId, Date startDate, Date endDate) {
        List<Object[]> chatStatistics = senderRepository.countChatsBySenderAndDate(memberId, startDate, endDate);
        /** USER가 Sender이면 계산 안하기  */
        long totalChats = chatStatistics.stream()
                .filter(e -> !Sender.USER.name().equals(e[0]))
                .mapToLong(e -> (Long) e[1])
                .sum();

        List<ChatSenderStaticResponseDTO> statisticsList = new ArrayList<>();
        for (Object[] result : chatStatistics) {
            Sender sender;
            if (result[0] instanceof String) {
                sender = Sender.valueOf((String) result[0]);//enum 으로 변환해서 사용
            } else {
                continue;
            }
            Long count = (Long) result[1];
            double percentage = calculatePercent(count, totalChats);
            statisticsList.add(new ChatSenderStaticResponseDTO(sender, count, percentage, startDate, endDate));
        }
        return statisticsList;
    }

    /** 나중에 메서드 합쳐서 리펙토링 필요 */
    public double calculatePercent(long count,long totalTags){
        double percentage = (double) count / totalTags * 100;
        return Math.round(percentage*10)/10.0;
    }

    /** 나중에 메서드 합쳐서 리펙토링 필요 */
    public DateRangeDTO staticsType(String type, LocalDate localDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(java.sql.Date.valueOf(localDate));
        Date startDate, endDate;
        switch (type) {
            case "weekly":
                /** 주간 -> 해당 주의 시작일과 종료일을 계산 */
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                startDate = new Date(calendar.getTimeInMillis());
                calendar.add(Calendar.DAY_OF_WEEK, 6);
                endDate = new Date(calendar.getTimeInMillis());
                break;
            case "monthly":
                /** 월간 -> 해당 월의 시작일과 종료일을 계산 */
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startDate = new Date(calendar.getTimeInMillis());
                calendar.add(Calendar.MONTH, 1);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                endDate = new Date(calendar.getTimeInMillis());
                break;
            case "yearly":
                /** 연간 -> 해당 연도의 시작일과 종료일을 계산 */
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                startDate = new Date(calendar.getTimeInMillis());
                calendar.add(Calendar.YEAR, 1);
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                endDate = new Date(calendar.getTimeInMillis());
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
        return new DateRangeDTO(startDate, endDate);
    }
}
