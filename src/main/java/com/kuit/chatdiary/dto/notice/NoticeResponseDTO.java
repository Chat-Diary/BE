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

    private Date noticeDate;

}
