package com.example.fintech.domain.quest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestRequestDTO {

    @NotNull(message = "category is required")
    private String category;

    @NotNull(message = "title is required")
    private String title;

    @NotNull(message = "body is required")
    private String body;

    @NotNull(message = "reward is required")
    private String reward;

    @NotNull(message = "deadline is required")
    private String deadline;
}

