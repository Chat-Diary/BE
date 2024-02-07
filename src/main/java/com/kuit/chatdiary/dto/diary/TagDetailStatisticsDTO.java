package com.kuit.chatdiary.dto.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class TagDetailStatisticsDTO {
    private Long count;
    private String[] tags;
}
