package com.kuit.chatdiary.dto.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TagStatisticsWithDateResponseDTO {
    private Date startDate;
    private Date endDate;
    private List<TagStatisticResponseDTO> Statistics;
}
