package com.kuit.chatdiary.dto.chat;

import com.kuit.chatdiary.domain.Sender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatSenderStaticResponseDTO {

    private Sender sender;
    private Long chatCount;
    private double percentage;
    private Date startDate;
    private Date endDate;
}