package com.openbanking.exception;

import com.openbanking.comon.CommonErrorCodes;

public class ValidationException extends CustomException {
    public ValidationException(String message) {
        super(CommonErrorCodes.VALIDATION_ERROR, message);
    }
}
