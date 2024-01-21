package com.kuit.chatdiary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity(name = "chat")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'USER'")
    private Sender sender;

    private String content;

    @CreatedDate
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'CHAT'")
    @Column(name = "chat_type")
    private ChatType chatType;

    public Chat(Member member, Sender sender, String content) {
        this.member = member;
        this.sender = sender;
        this.content = content;
    }

}
