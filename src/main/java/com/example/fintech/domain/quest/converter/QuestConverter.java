package com.example.fintech.domain.quest.converter;

import com.example.fintech.domain.quest.dto.request.QuestRequestDTO;
import com.example.fintech.domain.quest.dto.response.QuestListResponseDTO;
import com.example.fintech.domain.quest.dto.response.QuestResponseDTO;
import com.example.fintech.domain.quest.entity.Category;
import com.example.fintech.domain.quest.entity.Quest;
import com.example.fintech.domain.user.entity.User;
import com.example.fintech.domain.quest.entity.Status;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class QuestConverter {

    public Quest toEntity(QuestRequestDTO dto, User user) {
        return Quest.builder()
                .category(Category.valueOf(dto.getCategory().toUpperCase()))
                .title(dto.getTitle())
                .body(dto.getBody())
                .reward(Integer.parseInt(dto.getReward()))
                .deadline(dto.getDeadline())
                .status(Status.CHALLENGING)
                .user(user)
                .build();
    }

    public QuestResponseDTO toResponse(Quest quest, Long childId) {
        return QuestResponseDTO.builder()
                .questId(Long.valueOf(String.valueOf(quest.getId())))
                .title(quest.getTitle())
                .childId(childId)
                .reward(quest.getReward())
                .build();
    }

    public QuestListResponseDTO toResponse(Quest quest) {
        return QuestListResponseDTO.builder()
                .questId(Long.valueOf(String.valueOf(quest.getId())))
                .category(quest.getCategory().toString())
                .title(quest.getTitle())
                .body(quest.getBody())
                .reward(quest.getReward())
                .createdAt(quest.getCreatedAt())
                .deadline(quest.getDeadline())
                .status(quest.getStatus().toString())
                .build();
    }
}