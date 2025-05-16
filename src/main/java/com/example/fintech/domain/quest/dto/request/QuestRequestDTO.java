package com.example.fintech.domain.quest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deadline;
}

