package com.openbanking.exception.base_exception;

import com.openbanking.comon.CommonErrorCodes;
import com.openbanking.exception.base_exception.CustomException;

public class InvalidInputException extends CustomException {
    public InvalidInputException(String message) {
        super(CommonErrorCodes.INSERT_ERROR, message);
    }
}
