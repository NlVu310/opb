package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import com.openbanking.enums.ReconciliationHistoryResult;
import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "transaction_manage_reconciliation_history")
public class TransactionManageReconciliationHistoryEntity extends BaseEntity {
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "reconciliation_date")
    private OffsetDateTime reconciliationDate;
    @Column(name = "reconciliation_source")
    private String reconciliationSource;

    @Column(name = "reconciliation_result")
    @Enumerated(EnumType.STRING)
    private ReconciliationHistoryResult reconciliationResult;
    @Column(name = "transaction_manage_id")
    private Long transactionManageId;
    @Column(name = "reconciliation_manage_id")
    private Long reconciliationManageId;
}
