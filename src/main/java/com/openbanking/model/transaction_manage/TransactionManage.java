package com.openbanking.model.transaction_manage;

import com.openbanking.comon.BaseDTO;
import com.openbanking.enums.TransactionStatus;
import com.openbanking.model.transaction_manage_reconciliation_history.TransactionManageReconciliationHistory;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class TransactionManage extends BaseDTO {
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
    private List<TransactionManageReconciliationHistory> transactionManageReconciliationHistories;
}
