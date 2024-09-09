package com.openbanking.exception.base_exception;

import com.openbanking.comon.CommonErrorCodes;
import com.openbanking.exception.base_exception.CustomException;

public class ValidationException extends CustomException {
    public ValidationException(String message) {
        super(CommonErrorCodes.VALIDATION_ERROR, message);
    }
}
