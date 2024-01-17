package com.kuit.chatdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TagSearchResponse {
    private Long diaryId;
    private String title;
    private String content;
    private String diaryDate;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String status;
    private List<String> tagList;
    private int photoCount;
}
