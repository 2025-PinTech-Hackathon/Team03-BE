package com.example.fintech.domain.quest.repository;

import com.example.fintech.domain.quest.entity.Quest;
import com.example.fintech.domain.quest.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, Long> {
    List<Quest> findByUserId(Long userId);
    List<Quest> findByUserIdIn(List<Long> userIds);
    long countByUserIdAndStatus(Long userId, Status status);
}
