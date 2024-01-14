package com.kuit.chatdiary.dto.diary;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryInquiryRequest {


    private Long diary_id;
    private Long user_id;
    private String day;
    private String title;
    private String content;

}
