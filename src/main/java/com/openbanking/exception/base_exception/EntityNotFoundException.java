package com.openbanking.exception.base_exception;

import com.openbanking.comon.CommonErrorCodes;
import com.openbanking.exception.base_exception.CustomException;

public class EntityNotFoundException extends CustomException {
    public EntityNotFoundException(String message) {
        super(CommonErrorCodes.RESOURCE_NOT_FOUND, message);
    }
}
