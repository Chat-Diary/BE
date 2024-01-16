package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
