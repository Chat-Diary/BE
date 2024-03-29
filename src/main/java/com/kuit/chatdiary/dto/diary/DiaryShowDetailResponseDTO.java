package com.kuit.chatdiary.dto.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryShowDetailResponseDTO {

    private Date diaryDate ;

    private String title;

    private List<String> imgUrl;

    private String content;

    private List<String> tagName;

    private Long CharacterIndex;

}
