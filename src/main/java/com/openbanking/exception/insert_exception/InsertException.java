package com.openbanking.exception.insert_exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InsertExceptionService extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    public InsertException(InsertExceptionEnum insertExceptionEnum, String message) {
        super(constructMessage(insertExceptionEnum.getMessage(), message));
        this.status = insertExceptionEnum.getHttpStatus();
        this.code = insertExceptionEnum.getCode();
    }

    private static String constructMessage(String defaultMessage, String customMessage) {
        if (customMessage != null && !customMessage.isEmpty()) {
            return defaultMessage + " " + customMessage;
        } else {
            return defaultMessage;
        }
    }

}