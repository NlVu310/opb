package com.openbanking.exception;

import com.openbanking.comon.CommonErrorCodes;

public class EntityNotFoundException extends CustomException {
    public EntityNotFoundException(String message) {
        super(CommonErrorCodes.RESOURCE_NOT_FOUND, message);
    }
}
