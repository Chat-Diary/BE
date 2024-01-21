package com.kuit.chatdiary.controller.diary;


import com.kuit.chatdiary.dto.diary.DiaryInquiryResponse;
import com.kuit.chatdiary.service.diary.DiaryInquiryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("diary")
public class DiaryInquiryController {

    private final DiaryInquiryService diaryInquiryService;

    public DiaryInquiryController(DiaryInquiryService diaryInquiryService){
        this.diaryInquiryService = diaryInquiryService;
    }

    @GetMapping("/get_monthly_diary")
    public ResponseEntity<List<DiaryInquiryResponse>> getMonthlyDiaryList(@RequestParam("user_id") Long user_id,
                                                                          @RequestParam("year") int year, @RequestParam("month") int month){
        List<DiaryInquiryResponse> monthlyEvents = diaryInquiryService.getMonthlyDiaryPhotos(user_id,year,month);
        return ResponseEntity.ok(monthlyEvents);
    }


}