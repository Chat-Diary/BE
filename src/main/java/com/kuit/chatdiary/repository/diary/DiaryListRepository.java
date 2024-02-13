package com.kuit.chatdiary.repository.diary;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.domain.DiaryPhoto;
import com.kuit.chatdiary.domain.DiaryTag;
import com.kuit.chatdiary.dto.diary.DiaryListResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TemporalType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class DiaryListRepository {

    private final EntityManager em;
    public DiaryListRepository(EntityManager em) {
            this.em = em;
        }

    /**
     * 중복되는 쿼리 따로 분리
     * 재사용성 높이기
     */
    public List<Diary> getDiaries(Long userId, Date startDate, Date endDate) {
        return em.createQuery("SELECT d FROM diary d WHERE d.member.userId = :userId AND d.diaryDate BETWEEN :startDate AND :endDate", Diary.class)
                .setParameter("userId", userId)
                .setParameter("startDate", startDate, TemporalType.DATE)
                .setParameter("endDate", endDate, TemporalType.DATE)
                .getResultList();
    }

    public List<DiaryListResponseDTO> inquiryDiaryRange(Long userId, Date startDate, Date endDate) {
        return getDiaries(userId, startDate, endDate).stream()
                .map(diary -> {
                    List<DiaryPhoto> diaryPhotos = em.createQuery("SELECT dp FROM diaryphoto dp WHERE dp.diary = :diary", DiaryPhoto.class)
                            .setParameter("diary", diary)
                            .getResultList();
                    List<DiaryTag> diaryTags = em.createQuery("SELECT dt FROM diarytag dt WHERE dt.diary = :diary", DiaryTag.class)
                            .setParameter("diary", diary)
                            .getResultList();

                    return new DiaryListResponseDTO(diary, diaryPhotos, diaryTags);
                }).collect(Collectors.toList());
    }


    }


