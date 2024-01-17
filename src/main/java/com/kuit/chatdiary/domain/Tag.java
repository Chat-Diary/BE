package com.kuit.chatdiary.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name="tag")
@Getter
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    private String category;

    @Column(name = "tag_name")
    private String tagName;

    @OneToMany(mappedBy = "tag")
    private List<DiaryTag> diaryTagList = new ArrayList<>();
}
