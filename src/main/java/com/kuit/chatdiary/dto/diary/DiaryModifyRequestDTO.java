package com.kuit.chatdiary.dto.diary;


import lombok.Getter;

import java.sql.Date;
import java.util.List;

@Getter
public class DiaryModifyRequestDTO {

    private Long userId;

    private String diaryDate;

    private String title;

    private String content;

    private List<String> tagNames;

    private List<String> deleteImgUrls;

    private List<String> newImgUrls;

    public void setNewImgUrls(List<String> newImgUrls) {
        this.newImgUrls = newImgUrls;
    }
}
