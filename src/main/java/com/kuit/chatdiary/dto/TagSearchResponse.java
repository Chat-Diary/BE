package com.kuit.chatdiary.dto;

import com.kuit.chatdiary.domain.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class TagSearchResponse {
    private Long diaryId;
    private String title;
    private String content;
    private Date diaryDate;
    private List<TagInfoDTO> tagList;
    private List<String> photoList;

    @Builder
    public TagSearchResponse(Diary diary){
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
