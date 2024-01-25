package com.kuit.chatdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@AllArgsConstructor
@Getter
@Setter
public class DateRange {
    private final Date startDate;
    private final Date endDate;
}
