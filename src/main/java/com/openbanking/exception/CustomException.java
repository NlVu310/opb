package com.openbanking.exception;

import com.openbanking.comon.ErrorCode;
import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String customMessage;

    protected CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.customMessage = errorCode.getMessage();
    }

    protected CustomException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }
}

