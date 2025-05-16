package com.example.fintech.domain.transaction.exception;

import com.example.fintech.global.code.BaseErrorCode;
import com.example.fintech.global.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum TransactionErrorCode implements BaseErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "TRANS404_1", "해당 사용자를 찾을 수 없습니다."),
    TIME_LIMIT(HttpStatus.FORBIDDEN, "TIME_LIMIT403", "현재 시간에는 결제를 이용할 수 없습니다."),
    AMOUNT_LIMIT(HttpStatus.FORBIDDEN, "AMOUNT_LIMIT403", "결제 금액이 설정된 한도를 초과했습니다."),
    DAILY_LIMIT(HttpStatus.FORBIDDEN, "DAILY_LIMIT403", "오늘 결제 가능한 금액을 모두 사용했습니다."),
    CATEGORY_LIMIT(HttpStatus.FORBIDDEN, "CATEGORY_LIMIT403", "이 업종에서는 결제가 제한되어 있습니다."),
    REQUEST_NOT_APPROVED(HttpStatus.FORBIDDEN, "REQUEST_NOT_APPROVED403", "결제 요청이 승인되지 않았습니다.");





    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .httpStatus(this.httpStatus)
                .isSuccess(false)
                .code(this.code)
                .message(this.message)
                .build();
    }
}
