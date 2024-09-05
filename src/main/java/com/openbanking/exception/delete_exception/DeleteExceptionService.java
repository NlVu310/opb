package com.openbanking.exception.delete_exception;

import org.springframework.http.HttpStatus;

public class DeleteExceptionService extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    public DeleteExceptionService(DeleteExceptionEnum deleteExceptionEnum , String message) {
        super(constructMessage(deleteExceptionEnum.getMessage(), message));
        this.status = deleteExceptionEnum.getHttpStatus();
        this.code = deleteExceptionEnum.getCode();
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