package com.kuit.chatdiary.dto.diary;

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
@AllArgsConstructor
public class TagSearchResponseDTO {

    private Long diaryId;
    private String title;
    private Date diaryDate;
    private List<TagInfoDTO> tagList;
    private List<String> photoUrls;
    public TagSearchResponseDTO(Long diaryId, String title, Date diaryDate) {
        this.diaryId = diaryId;
        this.title = title;
        this.diaryDate = diaryDate;
    }
}