package com.kuit.chatdiary.dto.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class TagDetailStatisticsResponseDTO {
    private Date startDate;
    private Date endDate;
    private List<TagDetailStatisticsDTO> statistics;
}
