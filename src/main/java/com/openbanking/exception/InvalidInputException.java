package com.openbanking.exception;

import com.openbanking.comon.CommonErrorCodes;

public class InvalidInputException extends CustomException {
    public InvalidInputException(String message) {
        super(CommonErrorCodes.VALIDATION_ERROR, message);
    }
}
