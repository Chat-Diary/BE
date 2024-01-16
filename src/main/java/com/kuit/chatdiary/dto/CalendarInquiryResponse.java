package com.kuit.chatdiary.dto;

import com.kuit.chatdiary.domain.Sender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CalendarInquiryResponse {
    private String senderName;
    private LocalDateTime createAt;


}
