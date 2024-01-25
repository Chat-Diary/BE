package com.kuit.chatdiary.service;

import com.kuit.chatdiary.dto.DateRange;
import com.kuit.chatdiary.dto.TagStatisticResponse;
import com.kuit.chatdiary.repository.DiaryTagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class DiaryTagStatisticsService {

    private final DiaryTagRepository diaryTagRepository;

    public DiaryTagStatisticsService(DiaryTagRepository diaryTagRepository) {
        this.diaryTagRepository = diaryTagRepository;
    }

    public List<TagStatisticResponse> calculateTagStatistics(Long memberId, Date startDate, Date endDate) {
        List<Object[]> tagStatistics = diaryTagRepository.findTagStatisticsByMember(memberId, startDate, endDate);
        /** 쿼리 3번째 결과를 추출해서 스트림으로 탐색해서 다 더해서 전체 카운트 계산..! */
        long totalTags = tagStatistics.stream().mapToLong(e -> (Long) e[2]).sum();
        List<TagStatisticResponse> statisticsList = new ArrayList<>();
        for (Object[] result : tagStatistics) {
            String category = (String) result[0];
            String tagName = (String) result[1];
            Long count = (Long) result[2];
            statisticsList.add(new TagStatisticResponse(category, tagName, count, calculatePercent(count,totalTags)));
        }
        return statisticsList;
    }

    public double calculatePercent(long count,long totalTags){
        double percentage = (double) count / totalTags * 100;
        return Math.round(percentage*10)/10.0;
    }

    /** 코드 더 줄이고 싶은데.. */
    public DateRange staticsType(String type, LocalDate localDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(java.sql.Date.valueOf(localDate));
        Date startDate, endDate;
        switch (type) {
            case "weekly":
                /** 주간 -> 해당 주의 시작일과 종료일을 계산 */
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
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
        return new DateRange(startDate, endDate);
    }
}
