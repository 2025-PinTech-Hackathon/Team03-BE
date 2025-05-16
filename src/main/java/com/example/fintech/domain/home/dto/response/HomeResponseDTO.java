package com.example.fintech.domain.home.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomeResponseDTO {
    private int inProgressCount;
    private double leftMoney;
    private int todayTotalSpend;
}
