package com.example.fintech.domain.trasactionRequest.exception;

import com.example.fintech.global.code.BaseErrorCode;
import com.example.fintech.global.exception.GeneralException;

public class TransactionRequestException extends GeneralException {
    public TransactionRequestException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);

    }
    public TransactionRequestException(BaseErrorCode baseErrorCode, Object result) {
        super(baseErrorCode, result);
    }

}
