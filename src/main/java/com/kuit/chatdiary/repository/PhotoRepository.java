package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
