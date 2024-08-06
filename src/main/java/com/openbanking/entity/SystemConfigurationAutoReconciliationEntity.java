package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "system_configuration_auto_reconciliation")
public class SystemConfigurationAutoReconciliationEntity extends BaseEntity {
    @Column(name = "partner_id")
    private Long partnerId;

    @Column(name = "reconciliation_time")
    private Timestamp reconciliationTime;

    @Column(name = "reconciliation_frequency_id")
    private Long reconciliationFrequencyId;

    @Column(name = "account_id")
    private Long accountId;
}
