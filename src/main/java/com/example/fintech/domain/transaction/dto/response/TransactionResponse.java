package com.example.fintech.domain.transaction.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class TransactionResponse {

    // 성공시 DTO
    @Builder
    @Getter
    public static class successDTO {

    }

    // 카테고리 실패시 DTO
    @Builder
    @Getter
    public static class failureCategoryDTO {
        private String reason;
        private String merchantName;
        private int mccCode;
        private int amount;
        private String timestamp;
        private Long userId;
    }

    // 금액 초과 실패시 DTO
    @Builder
    @Getter
    public static class failureAmountLimitDTO {
        private int limitAmount;
        private int attemptedAmount;

    }

    // 시간 제한 실패시 DTO
    @Builder
    @Getter
    public static class failureTimeLimitDTO {
        private List<String> allowedTimeRange;
        private String attemptedTime;

    }

    // 일일 거래량 실패시 DTO
    @Builder
    @Getter
    public static class failureDailyLimitDTO{

    }
}
