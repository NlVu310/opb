package com.openbanking.exception;

import com.openbanking.comon.CommonErrorCodes;

public class ResourceNotFoundException extends CustomException {
    public ResourceNotFoundException() {
        super(CommonErrorCodes.RESOURCE_NOT_FOUND);
    }

    public ResourceNotFoundException(String message) {
        super(CommonErrorCodes.RESOURCE_NOT_FOUND, message);
    }
}
