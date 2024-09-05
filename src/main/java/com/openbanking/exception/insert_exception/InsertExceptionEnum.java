package com.openbanking.exception.insert_exception;

import com.openbanking.comon.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum InsertExceptionEnum implements ErrorCode {
    INSERT_ACC(HttpStatus.BAD_REQUEST, "Register failed", "INS_ACC_TYPE_001"),
    INSERT_USER_NAME_ERROR(HttpStatus.BAD_REQUEST, "Username already existed", "INS_ACC_TYPE_001"),
    INSERT_ACC_TYPE_ERROR(HttpStatus.BAD_REQUEST, "Create Account Type fail", "INS_ACC_TYPE_002"),
    INSERT_UPDATE_ACC_TYPE_ERROR(HttpStatus.BAD_REQUEST, "Update Account Type fail", "INS_ACC_TYPE_003"),
    INSERT_CUSTOMER_ERROR(HttpStatus.BAD_REQUEST, "Create Customer fail", "INS_CUS_001"),
    INSERT_PARTNER_ERROR(HttpStatus.BAD_REQUEST, "Create Partner fail", "INS_PAR_001"),
    INSERT_UDP_PAR_ERROR(HttpStatus.BAD_REQUEST, "Update Partner fail", "INS_PAR_002"),
    INSERT_VLD_PAR_ERROR(HttpStatus.BAD_REQUEST, "Partner name existed", "INS_PAR_003"),
    INSERT_RECON(HttpStatus.BAD_REQUEST, "Failed to create or update Reconciliation", "INS_RECON_001"),
    INSERT_SOURCE(HttpStatus.BAD_REQUEST, "Create Source failed", "INS_SOURCE_001"),
    INSERT_TRANS_CONTENT(HttpStatus.BAD_REQUEST, "Create System Transaction Content failed", "INS_TRANS_CONT_001");

    private final HttpStatus status;
    @Getter
    private final String message;
    @Getter
    private final String code;

    InsertExceptionEnum(HttpStatus status, String message, String code) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }

}

