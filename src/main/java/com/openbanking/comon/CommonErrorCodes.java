package com.openbanking.comon;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum CommonErrorCodes implements ErrorCode {
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found", "001"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation error", "002"),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "Unauthorized access", "003"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "004"),
    DELETE_ERROR(HttpStatus.BAD_REQUEST, "Cannot delete", "005"),
    INSERT_ERROR(HttpStatus.BAD_REQUEST, "Cannot insert", "006");


    private final HttpStatus status;
    @Getter
    private final String code;
    @Getter
    private final String message;

    CommonErrorCodes(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }

}
