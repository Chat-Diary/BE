package com.kuit.chatdiary.dto.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@AllArgsConstructor
@Getter
@Setter
public class DateRangeDTO {
    private final Date startDate;
    private final Date endDate;
}