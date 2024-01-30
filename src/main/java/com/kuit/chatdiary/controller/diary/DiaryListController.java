package com.kuit.chatdiary.controller.diary;


import com.kuit.chatdiary.dto.diary.DiaryListResponseDTO;
import com.kuit.chatdiary.service.diary.DiaryListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("diary")
public class DiaryListController {

    private final DiaryListService diaryListService;

    public DiaryListController(DiaryListService diaryListService){
        this.diaryListService = diaryListService;
    }

    @GetMapping("/monthly/list")
    public ResponseEntity<List<DiaryListResponseDTO>> getMonthlyDiaryList(@RequestParam("user_id") Long user_id,
                                                                          @RequestParam("year") int year, @RequestParam("month") int month){
        List<DiaryListResponseDTO> monthlyEvents = diaryListService.getMonthlyDiaryPhotos(user_id,year,month);
        return ResponseEntity.ok(monthlyEvents);
    }


}