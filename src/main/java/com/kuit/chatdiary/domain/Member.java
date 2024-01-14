package com.kuit.chatdiary.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "member")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private Long user_id;

    private String email;

    private String password;

    @CreatedDate
    private LocalDateTime create_at;

    @LastModifiedDate
    private LocalDateTime update_at;

    @ColumnDefault("ACTIVE")
    private String status;

    @OneToMany(mappedBy = "member")  // 자신이 연관관계 주인이 아님
    private List<Chat> chatList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Diary> diaryList = new ArrayList<>();

}