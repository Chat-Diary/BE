package com.kuit.chatdiary.controller;

import com.kuit.chatdiary.dto.diary.DiaryDeleteRequestDTO;
import com.kuit.chatdiary.dto.diary.DiaryDeleteResponseDTO;
import com.kuit.chatdiary.dto.diary.DiaryModifyRequestDTO;
import com.kuit.chatdiary.dto.diary.DiaryModifyResponseDTO;
import com.kuit.chatdiary.dto.diary.DiaryShowDetailResponseDTO;
import com.kuit.chatdiary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.text.ParseException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;


    @GetMapping("/detail")
    public ResponseEntity<DiaryShowDetailResponseDTO> showDiary(@RequestParam(name="user_id") Long userId, @RequestParam(name="diary_date") Date diaryDate) throws Exception {
        log.info("[DiaryController.showDiary]");

        return ResponseEntity.ok().body(diaryService.showDiary(userId,diaryDate));
    }




    @PostMapping("/modify")
    public ResponseEntity<DiaryModifyResponseDTO> modifyDiary(@RequestPart(value="image") List<MultipartFile> multipartFiles, @RequestPart(value="request") DiaryModifyRequestDTO diaryModifyRequestDTO) throws IOException, ParseException {
        log.info("[DiaryController.modifyDiary]");

        List<String> newImageUrls =  diaryService.FileUpload(multipartFiles);

        diaryModifyRequestDTO.setNewImgUrls(newImageUrls);

        return ResponseEntity.ok().body(diaryService.modifyDiary(diaryModifyRequestDTO));
    }

    @PostMapping("/delete")
    public ResponseEntity<DiaryDeleteResponseDTO> deleteDiary(@RequestBody DiaryDeleteRequestDTO diaryDeleteRequestDTO) throws ParseException {
        log.info("[DiaryController.modifyDiary]");

        return ResponseEntity.ok().body(diaryService.deleteDiary(diaryDeleteRequestDTO));
    }


}