package com.openbanking.model.reconciliation_manage;

import com.openbanking.comon.BaseDTO;
import com.openbanking.model.transaction_manage_reconciliation_history.TransactionManageReconciliationHistory;
import lombok.Data;

import javax.persistence.Column;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class ReconciliationManageDetail extends BaseDTO {
    private String transactionDate;
    private String dorc;
    private String amount;
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
    private String sourceInstitution;
    private String reconciler;
    private String status;
    private String reconciliationDate;
    private String transactionId;
    private List<TransactionManageReconciliationHistory> transactionManageReconciliationHistories;
}
