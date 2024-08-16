package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import com.openbanking.enums.ReconciliationFrequencyUnit;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "system_configuration_auto_reconciliation")
public class SystemConfigurationAutoReconciliationEntity extends BaseEntity {
    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "reconciliation_time")
    private LocalTime reconciliationTime;

    @Column(name = "reconciliation_frequency_number")
    private Integer reconciliationFrequencyNumber;

    @Column(name = "reconciliation_frequency_unit")
    @Enumerated(EnumType.STRING)
    private ReconciliationFrequencyUnit reconciliationFrequencyUnit;

    @Column(name = "retry_time_number")
    private Integer retryTimeNumber;

    @Column(name = "retry_frequency_number")
    private Integer retryFrequencyNumber;

    @Column(name = "partner_name")
    private String partnerName;

    @Column(name = "source_code")
    private String sourceCode;

}
