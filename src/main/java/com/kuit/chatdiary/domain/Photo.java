package com.kuit.chatdiary.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="photo")
@Getter
@Setter
@NoArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long photoId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="diary_id")
//    private Diary diary;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_photo_id")
    private DiaryPhoto diaryPhoto;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Column(name = "image_url")
    private String imageUrl;
}
