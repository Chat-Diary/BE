package com.kuit.chatdiary.service.diary;

import com.kuit.chatdiary.dto.diary.DateRangeDTO;
import com.kuit.chatdiary.dto.diary.TagStatisticResponseDTO;
import com.kuit.chatdiary.dto.diary.TagStatisticsWithDateResponseDTO;
import com.kuit.chatdiary.repository.diary.DiaryTagRepository;
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

    /** TagStatisticsWithDateDTO로 수정 */
    public TagStatisticsWithDateResponseDTO calculateTagStatistics(Long memberId, String type) {
        LocalDate localDate = LocalDate.now();
        DateRangeDTO dateRange = calculateDateRangeBasedOnType(type, localDate);
        List<Object[]> tagStatistics = diaryTagRepository.findTagStatisticsByMember(memberId, dateRange.getStartDate(), dateRange.getEndDate());
        long totalTags = calculateTotalTags(tagStatistics);
        List<TagStatisticResponseDTO> statisticsList = buildStatisticsList(tagStatistics, totalTags);
        sortStatisticsListByCount(statisticsList);
        return new TagStatisticsWithDateResponseDTO(dateRange.getStartDate(), dateRange.getEndDate(), statisticsList);
    }

    /** 타입별로 나눠서 계산 */
    private DateRangeDTO calculateDateRangeBasedOnType(String type, LocalDate date) {
        return staticsType(type, date);
    }

    /** 전체 계산 다 더하기 */
    private long calculateTotalTags(List<Object[]> tagStatistics) {
        return tagStatistics.stream().mapToLong(e -> (Long) e[2]).sum();
    }

    private List<TagStatisticResponseDTO> buildStatisticsList(List<Object[]> tagStatistics, long totalTags) {
        List<TagStatisticResponseDTO> statisticsList = new ArrayList<>();
        for (Object[] result : tagStatistics) {
            String category = (String) result[0];
            String tagName = (String) result[1];
            Long count = (Long) result[2];
            statisticsList.add(new TagStatisticResponseDTO(category, tagName, count, calculatePercent(count, totalTags)));
        }
        return statisticsList;
    }

    /** 정렬 메서드 */
    private void sortStatisticsListByCount(List<TagStatisticResponseDTO> statisticsList) {
        statisticsList.sort(Comparator.comparingLong(TagStatisticResponseDTO::getCount).reversed());
    }

    public double calculatePercent(long count,long totalTags){
        double percentage = (double) count / totalTags * 100;
        return Math.round(percentage*10)/10.0;
    }

    /** 코드 더 줄이고 싶은데.. */
    public DateRangeDTO staticsType(String type, LocalDate localDate){
        LocalDate startDate;
        LocalDate endDate;

        switch (type) {
            case "weekly":
                startDate = localDate.with(java.time.DayOfWeek.MONDAY);
                endDate = localDate.with(java.time.DayOfWeek.SUNDAY);
                break;
            case "monthly":
                startDate = localDate.withDayOfMonth(1);
                endDate = localDate.withDayOfMonth(localDate.lengthOfMonth());
                break;
            case "yearly":
                startDate = localDate.withDayOfYear(1);
                endDate = localDate.withDayOfYear(localDate.lengthOfYear());
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }

        return new DateRangeDTO(Date.valueOf(startDate), Date.valueOf(endDate));
    }
}
