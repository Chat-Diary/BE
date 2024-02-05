package com.kuit.chatdiary.dto.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class TagDetailStatisticsResponseDTO {
    private String startDate;
    private String endDate;
    private List<TagDetailStatisticsDTO> statistics;
}
