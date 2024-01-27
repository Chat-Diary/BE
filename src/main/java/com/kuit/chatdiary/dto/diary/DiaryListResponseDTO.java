package com.kuit.chatdiary.dto.diary;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.domain.Photo;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class DiaryListResponseDTO {
    private Long diaryId;
    private Long userId;
    private String title;
    private Date diaryDate;
    private List<TagInfoDTO> tagList;
    private List<String> photoList;
    @Builder
    public DiaryListResponseDTO(Diary diary){
        this.diaryId = diary.getDiaryId();
        this.title = diary.getTitle();
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