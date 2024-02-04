package com.kuit.chatdiary.dto.diary;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagPoolResponseDTO {
    private Long tagId;

    private String category;

    private String tagName;
}
