package com.kuit.chatdiary.controller.diary;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.dto.diary.DiaryInquiryRequest;
import com.kuit.chatdiary.repository.diary.DiaryInquiryRepository;
import com.kuit.chatdiary.repository.diary.DiaryRepository;
import com.kuit.chatdiary.service.diary.DiaryInquiryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class DiaryInquiryController {

    private final DiaryInquiryService diaryInquiryService;

    public DiaryInquiryController(DiaryInquiryService diaryInquiryService){
        this.diaryInquiryService = diaryInquiryService;
    }


    @GetMapping("/getDailyEvents")
    public ResponseEntity<List<Diary>> getDiaryList(@RequestParam("user_id") Long user_id, @RequestParam("day") String day) {
            LocalDate selectedDate = LocalDate.parse(day);
            List<Diary> events = diaryInquiryService.getDiaryWithPhotos(user_id, selectedDate);
            return ResponseEntity.ok(events);

    }
}
