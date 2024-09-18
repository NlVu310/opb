package com.openbanking.model.reconciliation_manage;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Data
public class ReconciliationManage extends BaseDTO {
    private OffsetDateTime transactionDate;
    private String amount;
    private String dorc;
    private String content;
    private String source;
    private String refNo;
    private String senderAccount;
    private String senderAccountNo;
    private String senderBank;
    private String senderCode;
    private String receiverAccount;
    private String receiverBank;
    private String receiverCode;
    private String receiverAccountNo;
    private String transactionId;
    private String sourceInstitution;
    private String status;
    private OffsetDateTime reconciliationDate;

}
