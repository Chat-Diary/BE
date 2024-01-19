package com.kuit.chatdiary.dto.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryShowDetailResponseDTO {

    private Date diaryDate ;

    private String title;

    private String imgUrl;

    private String content;

    private String tagName;
}
