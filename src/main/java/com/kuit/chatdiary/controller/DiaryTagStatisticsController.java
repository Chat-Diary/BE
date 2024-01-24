package com.kuit.chatdiary.controller;

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

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(java.sql.Date.valueOf(localDate));

        Date startDate;
        Date endDate;

        switch (type.toLowerCase()) {
            case "weekly":
                // 주간 -> 해당 주의 시작일과 종료일을 계산
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                startDate = new Date(calendar.getTimeInMillis());
                calendar.add(Calendar.DAY_OF_WEEK, 6);
                endDate = new Date(calendar.getTimeInMillis());
                break;
            case "monthly":
                // 월간 -> 해당 월의 시작일과 종료일을 계산
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startDate = new Date(calendar.getTimeInMillis());
                calendar.add(Calendar.MONTH, 1);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                endDate = new Date(calendar.getTimeInMillis());
                break;
            case "yearly":
                // 연간 -> 해당 연도의 시작일과 종료일을 계산
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                startDate = new Date(calendar.getTimeInMillis());
                calendar.add(Calendar.YEAR, 1);
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                endDate = new Date(calendar.getTimeInMillis());
                break;
            default:
                return ResponseEntity.badRequest().body(null);
        }

        List<TagStatisticResponse> tagStatistics = diaryTagStatisticsService.calculateTagStatistics(memberId, startDate, endDate);
        return ResponseEntity.ok(tagStatistics);
    }
}
