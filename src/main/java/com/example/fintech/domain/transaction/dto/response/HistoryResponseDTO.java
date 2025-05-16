package com.example.fintech.domain.transaction.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class HistoryResponseDTO {

    private LocalDate timestamp;
    private String merchant;
    private int amount;

}
