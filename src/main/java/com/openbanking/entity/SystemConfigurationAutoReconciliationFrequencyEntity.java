package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "system_configuration_auto_reconciliation_frequency")
public class SystemConfigurationAutoReconciliationFrequencyEntity extends BaseEntity {
    @Column(name = "name")
    private String name;
}
