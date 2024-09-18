package com.openbanking.model.transaction_manage_reconciliation_history;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import javax.persistence.Column;
import java.time.OffsetDateTime;

@Data
public class TransactionManageReconciliationHistory extends BaseDTO {
    private String transactionId;
    private OffsetDateTime reconciliationDate;
    private String reconciliationSource;
    private String reconciliationResult;
    private Long transactionManageId;
    private Long reconciliationManageId;

}
