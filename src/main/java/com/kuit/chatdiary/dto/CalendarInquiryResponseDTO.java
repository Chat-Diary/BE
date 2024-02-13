package com.kuit.chatdiary.dto;

import com.kuit.chatdiary.domain.Sender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarInquiryResponseDTO {
    private String dates;
    private Sender sender;
    private boolean exists;
}
