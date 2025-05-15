package com.example.fintech.domain.quest.repository;

import com.example.fintech.domain.quest.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestRepository extends JpaRepository<Quest, Long> {
}
