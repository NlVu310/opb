package com.openbanking.exception.resource_not_found_exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundExceptionService extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    public ResourceNotFoundExceptionService(ResourceNotFoundExceptionEnum resourceNotFoundExceptionEnum, String message) {
        super(constructMessage(resourceNotFoundExceptionEnum.getMessage(), message));
        this.status = resourceNotFoundExceptionEnum.getHttpStatus();
        this.code = resourceNotFoundExceptionEnum.getCode();
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
