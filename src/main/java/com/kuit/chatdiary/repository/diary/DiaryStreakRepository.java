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

        /**
         * 조회 기준 날짜 하루전으로 일기 스트릭 인정해줌
         * */
        if (diaries.isEmpty() || !diaries.get(0).getDiaryDate().toLocalDate().equals(today.minusDays(1))) {
            return 0L;
        }

        long streakCount = 1;
        Date lastDate = diaries.get(0).getDiaryDate();

        /** 구상도 (리펙시 주석 줄일 예정)
         * 선택정렬 알고리즘 처럼
         * 위에서 오늘이라는 검증 끝내면 lastDate라는 것은 리스트의 마지막 일기 일자
         * 반복문에서 0이 아닌 i=1 부터 시작
         * 즉 오늘 작성한 일기, 오늘 작성한 일기에서 하루 뺀 날짜와 currentDate의 날짜가 같다면 연속 일수 늘림
         * currentDate 가 이제 lastDate가 되어 하루 전날짜와 그다음 i=2일때 일기의 일자가 같다면 streakCount 늘림
         * */

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
