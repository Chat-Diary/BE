package com.kuit.chatdiary.dto.diary;


import lombok.Getter;

import java.sql.Date;

@Getter
public class DiaryModifyRequestDTO {
    private Long userId;

    private String diaryDate;

    private String title;

    private String content;

    //private String imgUrl;

    //private String tagName;
}
