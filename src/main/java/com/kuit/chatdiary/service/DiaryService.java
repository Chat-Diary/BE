package com.kuit.chatdiary.service;


import com.kuit.chatdiary.aws.S3Uploader;
import com.kuit.chatdiary.dto.diary.DiaryDeleteRequestDTO;
import com.kuit.chatdiary.dto.diary.DiaryDeleteResponseDTO;
import com.kuit.chatdiary.dto.diary.DiaryModifyRequestDTO;
import com.kuit.chatdiary.dto.diary.DiaryModifyResponseDTO;
import com.kuit.chatdiary.dto.diary.DiaryShowDetailResponseDTO;
import com.kuit.chatdiary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public DiaryShowDetailResponseDTO showDiary(Long userId, Date diaryDate) throws ParseException {
        log.info("[DiaryService.showDiary]");
        return diaryRepository.showDiaryDetail(userId, diaryDate);
    }

    @Transactional
    public DiaryModifyResponseDTO modifyDiary(DiaryModifyRequestDTO diaryModifyRequestDTO) throws ParseException {

        log.info("[DiaryService.modifyDiary]");

        //0. 다이어리 ID 구하기
        Long diaryId = diaryRepository.findDiaryId(diaryModifyRequestDTO);

        //1. title, content 변경
        diaryRepository.modifyDiaryTitleContent(diaryId, diaryModifyRequestDTO);

        //2. DiaryPhoto에서 사진 삭제
        diaryRepository.deleteImgFromDP(diaryId,diaryModifyRequestDTO.getDeleteImgUrls());

        //3. 새로운 사진 추가
        diaryRepository.addNewImg(diaryId, diaryModifyRequestDTO.getNewImgUrls());

        //4. 태그 수정
        diaryRepository.modifyTag(diaryId, diaryModifyRequestDTO.getTagNames());

        return new DiaryModifyResponseDTO(true);
    }

    @Transactional
    public DiaryDeleteResponseDTO deleteDiary(DiaryDeleteRequestDTO diaryDeleteRequestDTO) throws ParseException {
        log.info("[DiaryService.deleteDiary]");
        return diaryRepository.deleteDiary(diaryDeleteRequestDTO);
    }


    private final S3Uploader s3Uploader;

    public List<String> FileUpload(List<MultipartFile> multipartFiles) throws IOException {
        log.info("[DiaryService.FileUpload]");
        List<String> newImgUrls = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFiles) {
            String imgUrl = s3Uploader.upload(multipartFile, "test_images");
            newImgUrls.add(imgUrl);
        }
        System.out.println("imgurl: "+newImgUrls);
        return newImgUrls;
    }


}