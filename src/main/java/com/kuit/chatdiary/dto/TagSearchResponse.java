package com.kuit.chatdiary.dto;

import com.kuit.chatdiary.domain.Photo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TagSearchResponse {
    private Long diaryId;
    private String title;
    private String content;
    private Date diaryDate;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String status;
    private List<String> tagList;
    private List<Photo> photoList;
}
