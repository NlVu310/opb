package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "transaction_manage_reconciliation_history")
public class TransactionManageReconciliationHistoryEntity extends BaseEntity {
    @Column(name = "transaction_manage_id")
    private Long transactionManageId;
    @Column(name = "reconciliation_date")
    private OffsetDateTime reconciliationDate;
    @Column(name = "reconciliationer")
    private String reconciliationer;

    @Column(name = "reconciliation_source")
    private String reconciliationSource;

    @Column(name = "reconciliation_result")
    private String reconciliationResult;

}
