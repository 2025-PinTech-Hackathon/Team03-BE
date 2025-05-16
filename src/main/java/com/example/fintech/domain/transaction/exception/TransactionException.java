package com.example.fintech.domain.transaction.exception;

import com.example.fintech.global.code.BaseErrorCode;
import lombok.Getter;

@Getter
public class TransactionException extends RuntimeException {
    private final BaseErrorCode errorCode;

    public TransactionException(BaseErrorCode errorCode) {
        super(errorCode.getReasonHttpStatus().getMessage());
        this.errorCode = errorCode;
    }
}