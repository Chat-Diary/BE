package com.kuit.chatdiary.controller.diary;

import com.kuit.chatdiary.dto.diary.TagStatisticsWithDateResponseDTO;
import com.kuit.chatdiary.service.diary.DiaryTagStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diary")
public class DiaryTagStatisticsController {

    private final DiaryTagStatisticsService diaryTagStatisticsService;

    public DiaryTagStatisticsController(DiaryTagStatisticsService diaryTagStatisticsService) {
        this.diaryTagStatisticsService = diaryTagStatisticsService;
    }

    @GetMapping("/tags")
    public ResponseEntity<TagStatisticsWithDateResponseDTO> getTagStatistics(
            @RequestParam("memberId") Long memberId,
            @RequestParam("type") String type) {
        TagStatisticsWithDateResponseDTO tagStatistics = diaryTagStatisticsService.calculateTagStatistics(memberId, type);
        return ResponseEntity.ok(tagStatistics);
    }
}
