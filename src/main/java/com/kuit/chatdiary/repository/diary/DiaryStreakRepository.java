package com.kuit.chatdiary.repository.diary;

import com.kuit.chatdiary.domain.Diary;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public class DiaryStreakRepository {

    private final EntityManager em;

    public DiaryStreakRepository(EntityManager em) {
        this.em = em;
    }

    public Long streakDate(long userId, LocalDate today){
        String jpql = "SELECT d FROM diary d WHERE d.member.userId = :userId ORDER BY d.diaryDate DESC";

        List<Diary> diaries =em.createQuery(jpql,Diary.class)
                .setParameter("userId",userId)
                .getResultList();

        /** 오늘 날짜 기준으로 조회하기위한 조건문
         *  연속 기록있어도 조회기준 날짜 즉 오늘 작성 일기 없다면 연속작성 기록 없는거로
         * */
        if (diaries.isEmpty() || !diaries.get(0).getDiaryDate().toLocalDate().equals(today)) {
            return 0L;
        }

        long streakCount = 1;
        Date lastDate = diaries.get(0).getDiaryDate();

        for(int i=1; i<diaries.size(); i++){
            Date currentDate = diaries.get(i).getDiaryDate();
            if(lastDate.toLocalDate().minusDays(1).equals(currentDate.toLocalDate())){
                streakCount++;
                lastDate = currentDate;
            }else{
                break;
            }
        }

        return streakCount;
    }

}
