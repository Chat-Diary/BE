package com.kuit.chatdiary.repository.diary;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.domain.DiaryTag;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class DiaryTagRepository {
    private final EntityManager em;

    public DiaryTagRepository(EntityManager em) {
        this.em = em;
    }

    public List<Object[]> findTagStatisticsByMember(Long memberId, Date startDate, Date endDate) {
        String jpql = "SELECT dt.tag.category, dt.tag.tagName, COUNT(dt) FROM diarytag dt " +
                "JOIN dt.diary.member m " +
                "WHERE m.id = :memberId AND dt.diary.diaryDate BETWEEN :startDate AND :endDate " +
                "GROUP BY dt.tag.category, dt.tag.tagName";
        return em.createQuery(jpql, Object[].class)
                .setParameter("memberId", memberId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }


}
