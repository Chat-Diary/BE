package com.kuit.chatdiary.service;

import com.kuit.chatdiary.dto.TagStatisticResponse;
import com.kuit.chatdiary.repository.DiaryTagRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiaryTagStatisticsService {

    private final DiaryTagRepository diaryTagRepository;

    public DiaryTagStatisticsService(DiaryTagRepository diaryTagRepository) {
        this.diaryTagRepository = diaryTagRepository;
    }

    public List<TagStatisticResponse> calculateTagStatistics(Long memberId, Date startDate, Date endDate) {
        List<Object[]> tagStatistics = diaryTagRepository.findTagStatisticsByMember(memberId, startDate, endDate);
        long totalTags = tagStatistics.stream().mapToLong(e -> (Long) e[1]).sum();

        List<TagStatisticResponse> statisticsList = new ArrayList<>();

        for (Object[] result : tagStatistics) {
            String tagName = (String) result[0];
            Long count = (Long) result[1];
            double percentage = (double) count / totalTags * 100;
            percentage=Math.round(percentage*10)/10.0;
            statisticsList.add(new TagStatisticResponse(tagName, count, percentage));
        }

        return statisticsList;
    }
}
