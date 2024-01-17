package com.kuit.chatdiary.dto.diary;

import com.kuit.chatdiary.domain.DiaryTag;
import com.kuit.chatdiary.domain.Photo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DiaryInquiryResponse {
    private Long diary_id;
    private Long user_id;
    private String title;
    private String diaryDate;
    private List<DiaryTag> diaryTagList = new ArrayList<>();
    private List<Photo> photoList = new ArrayList<>();
}
