package com.kuit.chatdiary.dto.chat;

import com.kuit.chatdiary.domain.Sender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatSenderStaticResponseDTO {
    private Date startDate;
    private Date endDate;
    private List<ChatSenderStaticResponseDTO> statistics;
}