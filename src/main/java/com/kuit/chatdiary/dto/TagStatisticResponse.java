package com.kuit.chatdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagStatisticResponse {
    private String category;
    private String tagName;
    private Long count;
    private double percentage;
    private Date startDate;
    private Date endDate;

}