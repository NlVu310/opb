package com.openbanking.exception.resource_not_found_exception;

import com.openbanking.comon.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ResourceNotFoundExceptionEnum implements ErrorCode {
    RNF_ACC(HttpStatus.NOT_FOUND, "Account not found", "RNF_ACC_001"),
    RNF_ACC_TYPE(HttpStatus.NOT_FOUND, "Account Type not found", "RNF_ACC_TYPE_001"),
    RNF_USER(HttpStatus.NOT_FOUND, "User not found", "RNF_ACC_TYPE_001"),
    RNF_PAR_CUS(HttpStatus.NOT_FOUND, "List Customer Parent not found", "RNF_CUS_001"),
    RNF_CUS(HttpStatus.NOT_FOUND, "Customer not found", "RNF_CUS_002"),
    RNF_UDP_CUS(HttpStatus.NOT_FOUND, "Failed to update Customer", "RNF_CUS_003"),
    RNF_PARTNER(HttpStatus.NOT_FOUND, "Partner not found", "RNF_PAR_001"),
    RNF_RECON(HttpStatus.NOT_FOUND, "System Config Auto Reconciliation not found", "RNF_RECON_001"),
    RNF_SOURCE(HttpStatus.NOT_FOUND, "System Config Source not found", "RNF_SOURCE_001"),
    RNF_TRANS_CONT(HttpStatus.NOT_FOUND, "System Config Transaction Content not found", "RNF_TRANS_CONTENT_001"),
    RNF_TRANS(HttpStatus.NOT_FOUND, "Transaction not found", "RNF_TRANS_001");
    private final HttpStatus status;
    @Getter
    private final String message;
    @Getter
    private final String code;

    ResourceNotFoundExceptionEnum(HttpStatus status, String message, String code) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }

}
