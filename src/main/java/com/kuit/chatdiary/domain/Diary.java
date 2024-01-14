package com.kuit.chatdiary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "diary")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)

public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long diaryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private Member member;

    @Column(name = "diary_date")
    private Date diaryDate;

    private String title;

    private String content;

    @CreatedDate
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @ColumnDefault("'ACTIVE'")
    private String status;

    @OneToMany(mappedBy = "diary")
    private List<DiaryTag> diaryTagList = new ArrayList<>();

    @OneToMany(mappedBy = "diary")
    private List<Photo> photoList = new ArrayList<>();


}
