package com.kuit.chatdiary.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="diaryphoto")
@Getter
@NoArgsConstructor
public class DiaryPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_photo_id")
    private Long diaryPhotoId;

    //단방향
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="diary_id")
    private Diary diary;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="photo_id")
    private Photo photo;


}
