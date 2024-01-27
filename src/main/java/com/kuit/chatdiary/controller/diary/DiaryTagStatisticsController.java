package com.kuit.chatdiary.controller.diary;

import com.kuit.chatdiary.dto.diary.DateRangeDTO;
import com.kuit.chatdiary.dto.diary.TagStatisticResponseDTO;
import com.kuit.chatdiary.service.diary.DiaryTagStatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/diary")
public class DiaryTagStatisticsController {

    private final DiaryTagStatisticsService diaryTagStatisticsService;

    public DiaryTagStatisticsController(DiaryTagStatisticsService diaryTagStatisticsService) {
        this.diaryTagStatisticsService = diaryTagStatisticsService;
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagStatisticResponseDTO>> getTagStatistics(
            @RequestParam("memberId") Long memberId,
            @RequestParam("type") String type,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate localDate) {
        DateRangeDTO dateRange = diaryTagStatisticsService.staticsType(type,localDate);
        List<TagStatisticResponseDTO> tagStatistics = diaryTagStatisticsService.calculateTagStatistics(memberId, dateRange.getStartDate()
                ,dateRange.getEndDate());
        return ResponseEntity.ok(tagStatistics);
    }
}
