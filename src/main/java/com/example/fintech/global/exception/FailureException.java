package com.example.fintech.global.exception;

import com.example.fintech.global.code.BaseErrorCode;

public class FailureException extends GeneralException {

    public FailureException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
