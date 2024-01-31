package com.kuit.chatdiary.controller.diary;

import com.kuit.chatdiary.dto.diary.DiaryStreakResponseDTO;
import com.kuit.chatdiary.service.diary.DiaryStreakService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("diary")
public class DiaryStreakController {

    private final DiaryStreakService diaryStreakService;

    public DiaryStreakController(DiaryStreakService diaryStreakService) {
        this.diaryStreakService = diaryStreakService;
    }

    @GetMapping("/streak")
    public ResponseEntity<DiaryStreakResponseDTO> getStreakDate(
            @RequestParam("memberId") Long userId,
            @RequestParam("date") LocalDate today
    ){
        DiaryStreakResponseDTO response=diaryStreakService.streakDate(userId,today);
        return ResponseEntity.ok(response);
    }
}
