package com.openbanking.model.awaiting_reconciliation_transactions;

import com.openbanking.comon.BaseDTO;
import com.openbanking.enums.TransactionStatus;
import lombok.Data;

@Data
public class AwaitingReconciliationTransaction extends BaseDTO {
    private String transactionDate;
    private String amount;
    private String content;
    private String source;
    private String refNo;
    private String senderAccount;
    private String senderAccountNo;
    private String senderBank;
    private String senderCode;
    private String receiverAccount;
    private String receiverAccountNo;
    private String receiverBank;
    private String receiverCode;
    private String sourceInstitution;
    private TransactionStatus status;
    private String transactionId;
}
