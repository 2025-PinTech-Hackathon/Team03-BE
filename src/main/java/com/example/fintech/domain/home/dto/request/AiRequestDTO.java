package com.example.fintech.domain.home.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AiRequestDTO {

    private Long userId;
    private String role;
    private List<AiTransactionDTO> transactions;
}