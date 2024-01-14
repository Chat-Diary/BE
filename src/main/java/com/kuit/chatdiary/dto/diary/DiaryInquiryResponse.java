package com.kuit.chatdiary.dto.diary;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DiaryInquiryResponse {
    private Long diary_id;
    private Long user_id;
    private String day;
    private List<String> tags;
    private String representativePhotoUrl;
}
