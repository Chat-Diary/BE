package com.kuit.chatdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetDiaryDetailResponse {

    public String day;

    private String title;

    private String imgUrl;

    private String content;

    private Long tagId;  //tag 테이블의 tag_id
}
