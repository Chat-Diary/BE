package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.domain.*;
import com.kuit.chatdiary.dto.diary.DiaryDeleteRequestDTO;
import com.kuit.chatdiary.dto.diary.DiaryDeleteResponseDTO;
import com.kuit.chatdiary.dto.diary.DiaryModifyRequestDTO;
import com.kuit.chatdiary.dto.diary.DiaryModifyResponseDTO;
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

    public Long findDiaryId(DiaryModifyRequestDTO diaryModifyRequestDTO){
        log.info("[DiaryRepository.findDiaryId]");



    public DiaryModifyResponseDTO modifyDiary(DiaryModifyRequestDTO diaryModifyRequestDTO) {
        log.info("[DiaryRepository.modifyDiary]");

        Long diaryId = 0L;
        List<Long> resultList = em.createQuery("SELECT d.diaryId FROM diary d WHERE d.member.userId = :user_id AND d.diaryDate = :diary_date")
                .setParameter("user_id", diaryModifyRequestDTO.getUserId()).setParameter("diary_date",diaryModifyRequestDTO.getDiaryDate()).getResultList();

        for(Long result : resultList){
            diaryId = result;
        }

        return diaryId;
    }

    public void modifyDiaryTitleContent(Long diaryId,DiaryModifyRequestDTO diaryModifyRequestDTO) {
        log.info("[DiaryRepository.modifyDiaryTitleContent]");

        Diary updateDiary = em.find(Diary.class, diaryId);
        if (updateDiary != null) {
            updateDiary.setTitle(diaryModifyRequestDTO.getTitle());
            updateDiary.setContent(diaryModifyRequestDTO.getContent());

            em.merge(updateDiary);

        }

        System.out.println("title, content 변경 완료!");

    }


    public void deleteImgFromDP(Long diaryId ,List<String> deleteImgUrls){
        log.info("[DiaryRepository.deleteImgFromDP]");
        System.out.println("삭제할 url: "+ deleteImgUrls);

        List<Long> deletePhotoIds = em.createQuery("SELECT p.photoId FROM photo p"+
                " WHERE p.imageUrl IN :urls").setParameter("urls", deleteImgUrls).getResultList();
        List<Long> deleteDiaryPhotoIds = em.createQuery("SELECT dp.diaryPhotoId FROM diaryphoto dp"+
                " WHERE dp.photo.photoId IN :urls AND dp.diary.diaryId =: diary_id")
                .setParameter("urls", deletePhotoIds).setParameter("diary_id", diaryId)
                .getResultList();

        //diaryphoto에서 삭제
        for(Long deleteDiaryPhotoId : deleteDiaryPhotoIds){

            DiaryPhoto deleteDiaryPhoto = em.find(DiaryPhoto.class, deleteDiaryPhotoId);
            if (deleteDiaryPhoto != null) {
                em.remove(deleteDiaryPhoto);
            }
                }

        // 일기 제목, 내용 수정

    }

    public void addNewImg(Long diaryId, List<String> newImgUrls) {
        log.info("[DiaryRepository.addNewImg]");
        for(String newImgUrl: newImgUrls){
            System.out.println("새 imgurl: "+ newImgUrl);
            //Photo에 저장
            Photo photo = new Photo();
            photo.setImageUrl(newImgUrl);
            em.persist(photo);

            //diaryPhoto에 저장
            DiaryPhoto diaryPhoto = new DiaryPhoto();
            diaryPhoto.setDiary(em.find(Diary.class, diaryId));
            diaryPhoto.setPhoto(photo);
            em.persist(diaryPhoto);

        }

        System.out.println("새로운 이미지 저장 완료");
    }

    public void modifyTag(Long diaryId, List<String> tagNames) {
        log.info("[DiaryRepository.modifyTag]");

        //기존 diaryTag 삭제 (tagName에 해당하는 tagid 찾고 -> tagid 기준으로 행 삭제)
        List<Long> deleteDiaryTagIds = em.createQuery("SELECT dt.diaryTagId FROM diarytag dt WHERE dt.diary.diaryId = :diary_id")
                .setParameter("diary_id", diaryId).getResultList();

        for(Long deleteDiaryTagId:deleteDiaryTagIds){
            DiaryTag deletediaryTag = em.find(DiaryTag.class, deleteDiaryTagId);
            if(deletediaryTag != null){
                em.remove(deletediaryTag);
            }
        }

        //새로운 diaryTag 추가 (tagName에 해당하는 tagid 찾고 -> tagid 기준으로 행 추가)
        List<Long> newTagIds = em.createQuery("SELECT t.tagId FROM tag t WHERE t.tagName IN :tag_names")
                .setParameter("tag_names", tagNames).getResultList();
        for(Long newTagId:newTagIds){
            DiaryTag diaryTag = new DiaryTag();
            diaryTag.setDiary(em.find(Diary.class, diaryId));
            diaryTag.setTag(em.find(Tag.class, newTagId));
            em.persist(diaryTag);
        }

    }



    public DiaryDeleteResponseDTO deleteDiary(DiaryDeleteRequestDTO diaryDeleteRequestDTO) {
        log.info("[DiaryRepository.deleteDiary]");

        Long diaryId = 0L;


        List<Long> resultList = em.createQuery("SELECT d.diaryId FROM diary d WHERE d.member.userId = :user_id AND d.diaryDate = :diary_date")
                .setParameter("user_id", diaryDeleteRequestDTO.getUserId()).setParameter("diary_date",diaryDeleteRequestDTO.getDiaryDate()).getResultList();

        for(Long result : resultList){
            diaryId = result;
        }

        System.out.println("diaryId: "+diaryId);

        //diaryphoto에서 삭제
        List<Long> deleteDiaryPhotoIds = em.createQuery("SELECT dp.diaryPhotoId FROM diaryphoto dp WHERE dp.diary.diaryId = :diary_id")
                .setParameter("diary_id", diaryId).getResultList();
        for(Long deleteDiaryPhotoId:deleteDiaryPhotoIds){
            DiaryPhoto deletediaryPhoto = em.find(DiaryPhoto.class, deleteDiaryPhotoId);
            if(deletediaryPhoto!=null){
                em.remove(deletediaryPhoto);
            }
        }

        //diarytag에서 삭제
        List<Long> deleteDiaryTagIds = em.createQuery("SELECT dt.diaryTagId FROM diarytag dt WHERE dt.diary.diaryId = :diary_id")
                .setParameter("diary_id", diaryId).getResultList();

        for(Long deleteDiaryTagId:deleteDiaryTagIds){
            DiaryTag deleteDiaryTag = em.find(DiaryTag.class, deleteDiaryTagId);
            if(deleteDiaryTag!=null){
                em.remove(deleteDiaryTag);
            }
        }

        // diary에서 삭제
        Diary deleteDiary = em.find(Diary.class, diaryId);
        if(deleteDiary != null){
            em.remove(deleteDiary);
        }

        DiaryDeleteResponseDTO diaryDeleteResponseDTO = new DiaryDeleteResponseDTO(true);

        return diaryDeleteResponseDTO;
    }



}