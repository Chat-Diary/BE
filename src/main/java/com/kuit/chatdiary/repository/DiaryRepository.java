package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.domain.*;
import com.kuit.chatdiary.dto.diary.DiaryDeleteRequestDTO;
import com.kuit.chatdiary.dto.diary.DiaryDeleteResponseDTO;
import com.kuit.chatdiary.dto.diary.DiaryModifyRequestDTO;
import com.kuit.chatdiary.dto.diary.DiaryShowDetailResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Repository
public class DiaryRepository {

    private final EntityManager em;

    public DiaryShowDetailResponseDTO showDiaryDetail (Long userId, java.sql.Date diaryDate) throws Exception {

        log.info("[DiaryRepository.showDiaryDetail]");

        Long diaryId = 0L;
        String title = "";
        String content = "";

        List<Object[]> resultList = em.createQuery("SELECT d.diaryId, d.title, d.content FROM diary d WHERE d.member.userId = :user_id AND d.diaryDate = :diary_date")
                .setParameter("user_id", userId).setParameter("diary_date", diaryDate).getResultList();

        if(resultList.size()==0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        for(Object[] result : resultList){
            diaryId = (Long) result[0];
            title = (String) result[1];
            content = (String) result[2];
        }


        List<String> imageUrlList =  em.createQuery("SELECT p.imageUrl FROM diaryphoto dp LEFT OUTER JOIN dp.photo p"+
                " WHERE dp.diary.diaryId = :diary_id").setParameter("diary_id", diaryId).getResultList();

        List<String> tagNameList = em.createQuery("SELECT t.tagName"+
                        " FROM diarytag dt LEFT OUTER JOIN dt.tag t"+
                        " WHERE dt.diary.id = :diary_id")
                .setParameter("diary_id", diaryId).getResultList();

        List<Object[]> senderCounts = em.createQuery("SELECT c.sender, MAX(c.createAt) AS latest_created_at, COUNT(*) AS cnt FROM chat c"+
                        " WHERE DATE(c.createAt) = : diary_date AND c.sender NOT IN :user"+
                        " GROUP BY c.sender ORDER BY cnt DESC, latest_created_at DESC")
                .setParameter("diary_date", diaryDate).setParameter("user", Sender.USER).getResultList();

        if(senderCounts.size()==0){
            return new DiaryShowDetailResponseDTO(diaryDate, title, imageUrlList, content, tagNameList, null);
        }

        Sender sender = (Sender) senderCounts.get(0)[0];
        return new DiaryShowDetailResponseDTO(diaryDate, title, imageUrlList, content, tagNameList, (long) sender.getIndex());


    }


    public Long findDiaryId(DiaryModifyRequestDTO diaryModifyRequestDTO) throws ParseException {

        log.info("[DiaryRepository.findDiaryId]");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Long diaryId = 0L;

        Date diaryDate = dateFormat.parse(diaryModifyRequestDTO.getDiaryDate());

        List<Long> resultList = em.createQuery("SELECT d.diaryId FROM diary d WHERE d.member.userId = :user_id AND d.diaryDate = :diary_date")
                .setParameter("user_id", diaryModifyRequestDTO.getUserId()).setParameter("diary_date",diaryDate).getResultList();


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

    }


    public void deleteImgFromDP(Long diaryId ,List<String> deleteImgUrls){
        log.info("[DiaryRepository.deleteImgFromDP]");

     /*   이미지 url로 삭제할 photo_id를 찾음
         -> photo_id기반으로 삭제할 diary_photo_id를 찾음
         -> diary_photo_id 기준으로 diaryphoto테이블에서 행 삭제*/

        List<Long> deletePhotoIds = em.createQuery("SELECT p.photoId FROM photo p"+
                " WHERE p.imageUrl IN :urls").setParameter("urls", deleteImgUrls).getResultList();
        List<Long> deleteDiaryPhotoIds = em.createQuery("SELECT dp.diaryPhotoId FROM diaryphoto dp"+
                " WHERE dp.photo.photoId IN :delete_photo_ids AND dp.diary.diaryId =: diary_id")
                .setParameter("delete_photo_ids", deletePhotoIds).setParameter("diary_id", diaryId)
                .getResultList();

        for(Long deleteDiaryPhotoId : deleteDiaryPhotoIds){

            DiaryPhoto deleteDiaryPhoto = em.find(DiaryPhoto.class, deleteDiaryPhotoId);
            if (deleteDiaryPhoto != null) {
                em.remove(deleteDiaryPhoto);
            }
                }


    }

    public void addNewImg(Long diaryId, List<String> newImgUrls) {
        log.info("[DiaryRepository.addNewImg]");

/*        생성된 이미지URL을 포함한 photo객체 생성, 저장
         -> Photo객체를 포함하여 diaryPhoto객체 생성, 저장*/

        for(String newImgUrl: newImgUrls){
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



    public DiaryDeleteResponseDTO deleteDiary(DiaryDeleteRequestDTO diaryDeleteRequestDTO) throws ParseException {
        log.info("[DiaryRepository.deleteDiary]");

        Long diaryId = 0L;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date diaryDate = dateFormat.parse(diaryDeleteRequestDTO.getDiaryDate());


        List<Long> resultList = em.createQuery("SELECT d.diaryId FROM diary d WHERE d.member.userId = :user_id AND d.diaryDate = :diary_date")
                .setParameter("user_id", diaryDeleteRequestDTO.getUserId()).setParameter("diary_date",diaryDate).getResultList();

        for(Long result : resultList){
            diaryId = result;
        }


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


    @Transactional
    public Diary insertDiary(Member member) throws ParseException {
       //diaryDate
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        java.util.Date utilDate = dateFormat.parse("20240214");
        java.sql.Date diaryDate = new java.sql.Date(utilDate.getTime());

        Diary diary = new Diary(member, diaryDate, "테니스 첫걸음", "오늘은 테니스의 첫 수업이 있는 날이었다. 아침에 일어나서부터 설레임이 가득했다. 처음이라 그런지 자세부터 배우느라 정말 힘들었다. 선생님이 추가 레슨을 해주셔서 정말 감사했다. 한 시간이 금새 지나갔고 아직은 부족하지만, 얼른 배워서 멋지게 치고 싶은 욕심이 생겼다. 나는 테니스를 경기할 수 있을 정도의 실력을 갖추기 위해 열심히 배우고 싶다는 다짐을 하며 하루를 마무리했다.", "ACTIVE");

        em.persist(diary);

        return diary;

    }
}