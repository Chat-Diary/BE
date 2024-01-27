package com.kuit.chatdiary.dto.chat;

import com.kuit.chatdiary.domain.ChatType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatWebSocketRequestDTO {

    private Long userId;
    private String content;
    private Integer selectedModel;
    private String chatType;
}
