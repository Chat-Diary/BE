package com.kuit.chatdiary.repository.diary;

import com.kuit.chatdiary.domain.Diary;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class DiaryStreakRepository {

    private final EntityManager em;

    public DiaryStreakRepository(EntityManager em) {
        this.em = em;
    }

    public Long streakDate(long userId){
        String jpql = "SELECT d FROM diary d WHERE d.member.userId = :userId ORDER BY d.diaryDate DESC";

        List<Diary> diaries =em.createQuery(jpql,Diary.class)
                .setParameter("userId",userId)
                .getResultList();

        if (diaries.isEmpty()) {
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
