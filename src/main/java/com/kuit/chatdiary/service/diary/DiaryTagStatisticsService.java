package com.kuit.chatdiary.service.diary;

import com.kuit.chatdiary.dto.diary.*;
import com.kuit.chatdiary.repository.diary.DiaryTagRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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


    public TagDetailStatisticsResponseDTO calculateTagDetailStatistics(Long memberId, String type) {
        LocalDate localDate = LocalDate.now();
        DateRangeDTO dateRange = calculateDateRangeBasedOnType(type, localDate);
        List<Object[]> tagStatistics = diaryTagRepository.findTagStatisticsByMember(memberId, dateRange.getStartDate(), dateRange.getEndDate());

        Map<String, Map<Long, Set<String>>> categoryTagsMap = new HashMap<>();

        String[] categories = {"전체", "인물", "행동", "장소", "감정"};
        for (String category : categories) {
            categoryTagsMap.put(category, new HashMap<>());
        }

        for (Object[] row : tagStatistics) {
            String category = (String) row[0];
            String tagName = (String) row[1];
            Long count = (Long) row[2];

            categoryTagsMap.get(category).computeIfAbsent(count, k -> new HashSet<>()).add(tagName);
            categoryTagsMap.get("전체").computeIfAbsent(count, k -> new HashSet<>()).add(tagName);
        }

        Map<String, List<TagDetailStatisticsDTO>> statisticsMap = new HashMap<>();
        for (String category : categories) {
            List<TagDetailStatisticsDTO> categoryStats = categoryTagsMap.get(category).entrySet().stream()
                    .map(entry -> new TagDetailStatisticsDTO(entry.getKey(), entry.getValue().toArray(new String[0])))
                    .sorted(Comparator.comparingLong(TagDetailStatisticsDTO::getCount).reversed())
                    .collect(Collectors.toList());
            statisticsMap.put(category, categoryStats);
        }

        return new TagDetailStatisticsResponseDTO(dateRange.getStartDate(),dateRange.getEndDate(), statisticsMap);
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

    /** 리스안에 리스트안에 리스트 와 같은 구조로 가공 (맵안에 맵)
     * 카테고리로 한번 묶고, 횟수로 그 안에서 그릅화 하기 위함
     * */
    private List<TagDetailStatisticsDTO> buildDetailStatisticsList(Map<Long, List<String>> countMap) {
        return countMap.entrySet().stream()
                .map(entry -> new TagDetailStatisticsDTO(entry.getKey(), entry.getValue().toArray(new String[0])))
                .sorted(Comparator.comparingLong(TagDetailStatisticsDTO::getCount).reversed())
                .collect(Collectors.toList());
    }


    /** 정렬 메서드 */
    private void sortStatisticsListByCount(List<TagStatisticResponseDTO> statisticsList) {
        statisticsList.sort(Comparator.comparingLong(TagStatisticResponseDTO::getCount).reversed());
    }
    /** 정렬 메서드 재탕
     * 반환하는 자료형이 달라서 분리됬지만.. 리펙시 묶는것 가능해보임
     * */
    private void sortDetailStatisticsListByCount(List<TagDetailStatisticsDTO> statisticsList) {
        statisticsList.sort(Comparator.comparingLong(TagDetailStatisticsDTO::getCount).reversed());
    }

    public double calculatePercent(long count,long totalTags){
        double percentage = (double) count / totalTags * 100;
        return Math.round(percentage*10)/10.0;
    }

    /** 통계에서 공통으로 사용하는 메서드라 나중에 합칠수 있을듯*/
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
