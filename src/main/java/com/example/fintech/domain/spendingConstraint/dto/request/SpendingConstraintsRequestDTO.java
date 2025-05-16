package com.example.fintech.domain.spendingConstraint.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SpendingConstraintsRequestDTO {

    @NotNull(message = "category is required")
    private List<String> category;

    @NotNull(message = "timeRange is required")
    private List<String> timeRange;

    @NotNull(message = "location is required")
    private List<String> location;

    @NotNull(message = "amountLimit is required")
    private int amountLimit;

    @NotNull(message = "dailyLimit is required")
    private int dailyLimit;

}