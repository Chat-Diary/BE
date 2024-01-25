package com.kuit.chatdiary.controller;

import com.kuit.chatdiary.dto.DateRange;
import com.kuit.chatdiary.dto.TagStatisticResponse;
import com.kuit.chatdiary.service.DiaryTagStatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/diary")
public class DiaryTagStatisticsController {

    private final DiaryTagStatisticsService diaryTagStatisticsService;

    public DiaryTagStatisticsController(DiaryTagStatisticsService diaryTagStatisticsService) {
        this.diaryTagStatisticsService = diaryTagStatisticsService;
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagStatisticResponse>> getTagStatistics(
            @RequestParam("memberId") Long memberId,
            @RequestParam("type") String type,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate localDate) {
        DateRange dateRange = diaryTagStatisticsService.staticsType(type,localDate);
        List<TagStatisticResponse> tagStatistics = diaryTagStatisticsService.calculateTagStatistics(memberId, dateRange.getStartDate()
                ,dateRange.getEndDate());
        return ResponseEntity.ok(tagStatistics);
    }
}
