package com.example.fintech.domain.transaction.exception;

import com.example.fintech.global.code.BaseErrorCode;
import com.example.fintech.global.exception.GeneralException;


public class TransactionException extends GeneralException {
    public TransactionException(BaseErrorCode baseErrorCode){
        super(baseErrorCode);
    }
}