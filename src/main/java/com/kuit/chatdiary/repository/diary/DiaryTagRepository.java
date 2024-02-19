package com.kuit.chatdiary.repository.diary;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.domain.DiaryTag;
import com.kuit.chatdiary.domain.Member;
import com.kuit.chatdiary.domain.Tag;
import com.kuit.chatdiary.repository.TagRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class DiaryTagRepository {
    private final EntityManager em;

    @Autowired
    private final TagRepository tagRepository;

    public DiaryTagRepository(EntityManager em, TagRepository tagRepository) {
        this.em = em;
        this.tagRepository = tagRepository;
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


    @Transactional
    public void insertDiaryTag(Diary diary) {

        Tag tag1 = tagRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        DiaryTag diaryTag1 = new DiaryTag(diary, tag1);
        em.persist(diaryTag1);

        Tag tag2 = tagRepository.findById(8L)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        DiaryTag diaryTag2 = new DiaryTag(diary, tag2);
        em.persist(diaryTag2);

    }
}
