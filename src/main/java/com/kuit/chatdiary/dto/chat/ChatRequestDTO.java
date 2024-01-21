package com.kuit.chatdiary.dto.chat;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
@Setter
public class ChatRequestDTO {

    private Long userId;
    private String content;
    private Integer selectedModel;
}
