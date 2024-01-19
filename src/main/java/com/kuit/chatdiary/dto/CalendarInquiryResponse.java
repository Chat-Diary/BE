package com.kuit.chatdiary.dto;

import com.kuit.chatdiary.domain.Sender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CalendarInquiryResponse {
    private Sender sender;
    private boolean exists;
}
