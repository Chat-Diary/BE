package com.kuit.chatdiary.dto.chat;

import com.kuit.chatdiary.domain.Chat;
import com.kuit.chatdiary.domain.ChatType;
import com.kuit.chatdiary.domain.Sender;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatGetResponseDTO {

    private Long chatId;
    private LocalDateTime createAt;
    private String content;
    private ChatType chatType;
    private Sender sender;

    public ChatGetResponseDTO(Chat chat) {
        this.chatId = chat.getChatId();
        this.createAt = chat.getCreateAt();
        this.content = chat.getContent();
        this.chatType = chat.getChatType();
        this.sender = chat.getSender();
    }
}