package com.openbanking.exception.authen_exception;

import org.springframework.http.HttpStatus;

public class ChangePasswordExceptionService extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    public ChangePasswordExceptionService(ChangePasswordExceptionEnum changePasswordExceptionEnum , String message) {
        super(constructMessage(changePasswordExceptionEnum.getMessage(), message));
        this.status = changePasswordExceptionEnum.getHttpStatus();
        this.code = changePasswordExceptionEnum.getCode();
    }

    private static String constructMessage(String defaultMessage, String customMessage) {
        if (customMessage != null && !customMessage.isEmpty()) {
            return defaultMessage + " " + customMessage;
        } else {
            return defaultMessage;
        }
    }
    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}