package com.openbanking.exception.delete_exception;

import com.openbanking.comon.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum DeleteExceptionEnum implements ErrorCode {
    DELETE_ACC_ACC_TYPE_ERROR(HttpStatus.BAD_REQUEST, "Deleted Account Type failed, Account existed", "DEL_ACC_TYPE_001"),
    DELETE_ACC_TYPE_ERROR(HttpStatus.BAD_REQUEST, "Deleted Account Type failed", "DEL_ACC_TYPE_002"),
    DELETE_TRANS_CONTENT_ERROR(HttpStatus.BAD_REQUEST, "Delete failed , Transaction Content Config existed", "DEL_TRANS_CONTENT_001"),
    DELETE_SYS_TRANS_ERROR(HttpStatus.BAD_REQUEST, "Delete Transaction Content Config failed.", "DEL_TRANS_CONTENT_002"),
    DELETE_TRANS_ERROR(HttpStatus.BAD_REQUEST, "Delete failed , Transaction existed", "DEL_TRANS_001"),
    DELETE_ACC_ERROR(HttpStatus.BAD_REQUEST, "Delete failed , Account existed", "DEL_ACC_001"),
    DELETE_ACC_REF_ERROR(HttpStatus.BAD_REQUEST, "Delete failed , This account create another account", "DEL_ACC_002"),

    DELETE_CUS_CON_ERROR(HttpStatus.BAD_REQUEST, "Delete failed , Customer data reference existed", "DEL_CUS_CON_001"),
    DELETE_CUS_ERROR(HttpStatus.BAD_REQUEST, "Delete Customer failed", "DEL_CUS_001"),
    DELETE_BANK_ACC_ERROR(HttpStatus.BAD_REQUEST, "Delete Bank Account failed", "DEL_BANK_ACC_001"),
    DELETE_PAR_BANK_ERROR(HttpStatus.BAD_REQUEST, "Partner has been assigned to the bank account. Delete operation failed.", "DEL_PAR_BANK_001"),
    DELETE_PAR_SOURCE_ERROR(HttpStatus.BAD_REQUEST, "Partner has been assigned to the source config. Delete operation failed.", "DEL_PAR_SOURCE_002"),
    DELETE_PARTNER(HttpStatus.BAD_REQUEST, "Delete Partner failed", "DEL_PAR_003"),
    DELETE_SYS_RECON_ERROR(HttpStatus.BAD_REQUEST, "Delete System Config Auto Reconciliation failed.", "DEL_SYS_REC_001"),
    DELETE_SYS_SOURCE_ERROR(HttpStatus.BAD_REQUEST, "Delete System Config Source failed.", "DEL_SYS_SOURCE_001");
    private final HttpStatus status;
    @Getter
    private final String message;
    @Getter
    private final String code;

    DeleteExceptionEnum(HttpStatus status, String message, String code) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }

}



