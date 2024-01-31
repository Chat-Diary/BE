package com.kuit.chatdiary.service.diary;

import com.kuit.chatdiary.dto.diary.DiaryStreakResponseDTO;
import com.kuit.chatdiary.repository.diary.DiaryStreakRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DiaryStreakService {
    public final DiaryStreakRepository diaryStreakRepository;

    public DiaryStreakService(DiaryStreakRepository diaryStreakRepository) {
        this.diaryStreakRepository = diaryStreakRepository;
    }

    public DiaryStreakResponseDTO streakDate(long userId, LocalDate today){
        long streakDate=diaryStreakRepository.streakDate(userId,today);
        DiaryStreakResponseDTO response = new DiaryStreakResponseDTO(streakDate);
        return response;
    }

}
