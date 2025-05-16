package com.example.fintech.domain.spendingConstraint.exception;

import com.example.fintech.global.code.BaseErrorCode;
import com.example.fintech.global.exception.GeneralException;

public class SpendingConstraintException extends GeneralException {
    public SpendingConstraintException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}