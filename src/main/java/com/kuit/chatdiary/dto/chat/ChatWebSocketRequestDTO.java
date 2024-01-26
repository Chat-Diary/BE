package com.kuit.chatdiary.dto.chat;

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
