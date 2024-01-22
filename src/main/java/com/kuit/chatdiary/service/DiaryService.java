package com.kuit.chatdiary.service;

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

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;

    @Transactional
    public DiaryShowDetailResponseDTO showDiary(Long userId, Date diaryDate) throws ParseException {
        log.info("[DiaryService.showDiary]");
        return diaryRepository.showDiaryDetail(userId, diaryDate);
    }
}

    public DiaryModifyResponseDTO modifyDiary(DiaryModifyRequestDTO diaryModifyRequestDTO)  {
        log.info("[DiaryService.modifyDiary]");
        return diaryRepository.modifyDiary(diaryModifyRequestDTO);
    }

    @Transactional
    public DiaryDeleteResponseDTO deleteDiary(DiaryDeleteRequestDTO diaryDeleteRequestDTO) {
        log.info("[DiaryService.deleteDiary]");
        return diaryRepository.deleteDiary(diaryDeleteRequestDTO);
    }
}