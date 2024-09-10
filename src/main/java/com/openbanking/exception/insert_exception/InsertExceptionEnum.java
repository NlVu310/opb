package com.openbanking.exception.insert_exception;

import com.openbanking.comon.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum InsertExceptionEnum implements ErrorCode {
    INSERT_ACC_ERROR(HttpStatus.BAD_REQUEST, "Register failed", "INS_ACC_001"),
    INSERT_CRE_ACC_ERROR(HttpStatus.BAD_REQUEST, "Create Account failed", "INS_ACC_002"),
    INSERT_RES_ACC_ERROR(HttpStatus.BAD_REQUEST, "Reset Password failed", "INS_ACC_003"),
    INSERT_DATE_ERROR(HttpStatus.BAD_REQUEST, "Invalid date format. Please use dd-MM-yyyy.", "INS_DATE_001"),
    INSERT_DATE_RANGE_ERROR(HttpStatus.BAD_REQUEST, "Date range overlap", "INS_DATE_002"),
    INSERT_FROM_DATE_ERROR(HttpStatus.BAD_REQUEST, "From date cannot be null", "INS_DATE_003"),
    INSERT_USER_NAME_ERROR(HttpStatus.BAD_REQUEST, "Username already existed", "INS_ACC_TYPE_001"),
    INSERT_ACC_TYPE_ERROR(HttpStatus.BAD_REQUEST, "Create Account Type fail", "INS_ACC_TYPE_002"),
    INSERT_UPDATE_ACC_TYPE_ERROR(HttpStatus.BAD_REQUEST, "Update Account Type fail", "INS_ACC_TYPE_003"),
    INSERT_CUSTOMER_ERROR(HttpStatus.BAD_REQUEST, "Create Customer fail", "INS_CUS_001"),
    INSERT_PARTNER_ERROR(HttpStatus.BAD_REQUEST, "Create Partner fail", "INS_PAR_001"),
    INSERT_UDP_PAR_ERROR(HttpStatus.BAD_REQUEST, "Update Partner fail", "INS_PAR_002"),
    INSERT_VLD_PAR_ERROR(HttpStatus.BAD_REQUEST, "Partner name existed", "INS_PAR_003"),
    INSERT_RECON_ERROR(HttpStatus.BAD_REQUEST, "Failed to create or update Reconciliation", "INS_RECON_001"),
    INSERT_SOURCE_ERROR(HttpStatus.BAD_REQUEST, "Create Source failed", "INS_SOURCE_001"),
    INSERT_SOURCE_DUP_ERROR(HttpStatus.BAD_REQUEST, "Duplicate code in list", "INS_SOURCE_002"),
    INSERT_SOURCE_CODE_ERROR(HttpStatus.BAD_REQUEST, "Code existed", "INS_SOURCE_003"),
    INSERT_BANK_ACC_ERROR(HttpStatus.BAD_REQUEST, "Bank account existed", "INS_BANK_001"),
    INSERT_TRANS_CONTENT_ERROR(HttpStatus.BAD_REQUEST, "Create System Transaction Content failed", "INS_TRANS_CONT_001");

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

