package com.kuit.chatdiary.controller;

import com.kuit.chatdiary.dto.diary.DiaryShowDetailResponse;
import com.kuit.chatdiary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/detail")
    public ResponseEntity<List<DiaryShowDetailResponse>> showDiary(@RequestParam(name="user_id") Long userId, @RequestParam(name="diary_date") Date diaryDate) throws ParseException, ParseException {
        log.info("[DiaryController.showDiary]");

        return ResponseEntity.ok().body(diaryService.showDiary(userId,diaryDate));
    }


}

