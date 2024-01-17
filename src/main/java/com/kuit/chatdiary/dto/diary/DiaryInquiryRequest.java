package com.kuit.chatdiary.dto.diary;

import com.kuit.chatdiary.domain.DiaryTag;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DiaryInquiryRequest {

    private Long diary_id;
    private Long user_id;
    private String diaryDate;
    private String title;
    private String content;

}
