package com.kuit.chatdiary.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponseDTO {
    private Long id;

    private String title;
    private String content;


    /** 지금은 쿼리로 날려서 수작업으로 날짜 입력하지만 나중에 작성 매서드 만들면 .now 등으로 서버 날짜 쓰기*/
    private Date noticeDate;

}
