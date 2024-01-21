package com.kuit.chatdiary.dto.diary;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.domain.DiaryTag;
import com.kuit.chatdiary.domain.Photo;
import com.kuit.chatdiary.dto.TagInfoDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class DiaryInquiryResponse {
    private Long diaryId;
    private Long userId;
    private String title;
    private String content;
    private Date diaryDate;
    private List<TagInfoDTO> tagList;
    private List<String> photoList;

    @Builder
    public DiaryInquiryResponse(Diary diary){
        this.diaryId = diary.getDiaryId();
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.diaryDate = diary.getDiaryDate();
        this.tagList = diary.getDiaryTagList().stream()
                .map(diaryTag -> {
                    TagInfoDTO TagInfoDTO = new TagInfoDTO();
                    TagInfoDTO.setTagId(diaryTag.getTag().getTagId());
                    TagInfoDTO.setTagName(diaryTag.getTag().getTagName());
                    return TagInfoDTO;
                })
                .collect(Collectors.toList());
        this.photoList = diary.getPhotoList().stream()
                .map(Photo::getImageUrl)
                .collect(Collectors.toList());
    }
}