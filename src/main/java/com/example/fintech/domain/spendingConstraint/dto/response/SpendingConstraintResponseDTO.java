package com.example.fintech.domain.spendingConstraint.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SpendingConstraintResponseDTO {

    private List<String> category;
    private List<String> timeRange;
    private List<String> location;
    private int amountLimit;
    private int dailyLimit;
}
