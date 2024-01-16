package com.kuit.chatdiary.repository;

import com.kuit.chatdiary.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
