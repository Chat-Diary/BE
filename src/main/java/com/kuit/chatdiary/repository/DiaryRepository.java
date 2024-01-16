package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.dto.diary.DiaryShowDetailResponseDTO;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Repository
public class DiaryRepository {

    private final EntityManager em;

    public DiaryShowDetailResponseDTO showDiaryDetail (Long userId, Date diaryDate) throws ParseException {

        log.info("[DiaryRepository.showDiaryDetail]");

        Long diaryId = 0L;
        String title = "";
        String content = "";

        List<Object[]> resultList = em.createQuery("SELECT d.diaryId, d.title, d.content FROM diary d WHERE d.member.userId = :user_id AND d.diaryDate = :diary_date")
                        .setParameter("user_id", userId).setParameter("diary_date", diaryDate).getResultList();

        for(Object[] result : resultList){
            diaryId = (Long) result[0];
            title = (String) result[1];
            content = (String) result[2];
        }


        List<String> imageUrlList = em.createQuery("SELECT p.imageUrl FROM photo p WHERE p.diary.diaryId = :diary_id")
                .setParameter("diary_id", diaryId).getResultList();

        List<String> tagNameList = em.createQuery("SELECT t.tagName"+
                        " FROM diarytag dt LEFT OUTER JOIN dt.tag t"+
                        " WHERE dt.diary.id = :diary_id")
                .setParameter("diary_id", diaryId).getResultList();


        return new DiaryShowDetailResponseDTO(diaryDate, title, imageUrlList, content, tagNameList);

    }

}