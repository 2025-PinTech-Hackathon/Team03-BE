package com.example.fintech.domain.quest.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestResponseDTO {
    private Long questId;
    private String title;
}
