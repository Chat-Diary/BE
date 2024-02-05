package com.kuit.chatdiary.dto.diary;

import com.kuit.chatdiary.domain.Diary;
import com.kuit.chatdiary.domain.DiaryPhoto;
import com.kuit.chatdiary.domain.DiaryTag;
import com.kuit.chatdiary.domain.Photo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class TagSearchResponseDTO {

    private Long diaryId;
    private String title;
    private Date diaryDate;
    private List<String> photoUrls;
    private List<TagInfoDTO> tagList;

    public TagSearchResponseDTO(Diary diary, List<DiaryPhoto> diaryPhotos, List<DiaryTag> diaryTags) {
        this.diaryId = diary.getDiaryId();
        this.title = diary.getTitle();
        this.diaryDate = diary.getDiaryDate();
        this.photoUrls = diaryPhotos != null ? diaryPhotos.stream()
                .map(DiaryPhoto::getPhoto)
                .map(Photo::getImageUrl)
                .collect(Collectors.toList()) : Collections.emptyList();
        this.tagList = diaryTags != null ? diaryTags.stream()
                .map(diaryTag -> new TagInfoDTO(diaryTag.getTag().getTagId(), diaryTag.getTag().getTagName()))
                .collect(Collectors.toList()) : Collections.emptyList();
    }

}