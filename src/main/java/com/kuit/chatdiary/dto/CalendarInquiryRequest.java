package com.kuit.chatdiary.dto;

import com.kuit.chatdiary.domain.Member;
import com.kuit.chatdiary.domain.Sender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CalendarInquiryRequest {

    private Member member;
    private LocalDateTime createAt;

}
