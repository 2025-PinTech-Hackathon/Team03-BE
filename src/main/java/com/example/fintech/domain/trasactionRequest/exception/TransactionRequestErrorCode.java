package com.example.fintech.domain.trasactionRequest.exception;

import com.example.fintech.global.code.BaseErrorCode;
import com.example.fintech.global.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum TransactionRequestErrorCode  implements BaseErrorCode {

    TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "TRANSREQ404_1", "해당 사용자를 찾을 수 없습니다.");


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