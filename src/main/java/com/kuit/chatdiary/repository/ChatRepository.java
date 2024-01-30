package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findTop10ByMember_UserIdOrderByChatIdDesc(Long userId);
}