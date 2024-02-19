package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findTop10ByMember_UserIdOrderByChatIdDesc(Long userId);
    @Query("SELECT c FROM chat c WHERE c.member.userId = :userId AND :lastChatId < c.chatId AND c.chatId <= :lastChatId+10  ORDER BY c.chatId ASC")
    List<Chat> findTop10ByUserIdAndChatIdGreaterThanOrderByChatIdDesc(@Param("userId") Long userId, @Param("lastChatId") Long lastChatId);
    List<Chat> findTopByMember_UserIdOrderByChatIdDesc(Long userId);
}
