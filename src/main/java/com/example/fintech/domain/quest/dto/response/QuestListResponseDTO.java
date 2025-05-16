package com.example.fintech.domain.quest.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class QuestListResponseDTO {
    private Long questId;
    private String category;
    private String title;
    private String body;
    private int reward;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private String status;
}
