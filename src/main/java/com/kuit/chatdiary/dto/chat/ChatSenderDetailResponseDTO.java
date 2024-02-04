package com.kuit.chatdiary.dto.chat;

import com.kuit.chatdiary.domain.Sender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatSenderDetailResponseDTO {
    private Sender sender;
    private Long chatCount;
    private double percentage;
}
