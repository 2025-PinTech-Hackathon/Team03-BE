package com.example.fintech.domain.transaction.dto.response;

import lombok.Builder;
import lombok.Getter;

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
        private String mccCode;
        private String blockedCategory;

    }

    // 금액 초과 실패시 DTO
    @Builder
    @Getter
    public static class failureAmountLimitDTO {
        private String reason;
        private int limitAmount;
        private int attemptedAmount;

    }

    // 시간 제한 실패시 DTO
    @Builder
    @Getter
    public static class failureTimeLimitDTO {
        private String reason;
        private String allowedTimeRange;
        private String attemptedTime;

    }
}
