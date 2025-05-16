package com.example.fintech.domain.quest.converter;

import com.example.fintech.domain.quest.dto.request.QuestRequestDTO;
import com.example.fintech.domain.quest.dto.response.QuestResponseDTO;
import com.example.fintech.domain.quest.entity.Quest;
import com.example.fintech.domain.user.entity.User;
import com.example.fintech.domain.quest.entity.Status;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class QuestConverter {

    public Quest toEntity(QuestRequestDTO dto, User user) {
        return Quest.builder()
                .category(dto.getCategory())
                .title(dto.getTitle())
                .body(dto.getBody())
                .reward(Integer.parseInt(dto.getReward()))
                .deadline(LocalDate.parse(dto.getDeadline().substring(0, 10)))
                .status(Status.Challenging)
                .user(user)
                .build();
    }

    public QuestResponseDTO toResponse(Quest quest) {
        return QuestResponseDTO.builder()
                .questId(Long.valueOf(String.valueOf(quest.getId())))
                .title(quest.getTitle())
                .build();
    }
}