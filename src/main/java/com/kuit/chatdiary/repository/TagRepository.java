package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
