package com.kuit.chatdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagStatisticResponse {
    private String category;
    private String tagName;
    private Long count;
    private double percentage;

}