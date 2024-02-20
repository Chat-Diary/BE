package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.domain.DiaryPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryPhotoRepository extends JpaRepository<DiaryPhoto, Long> {
}
