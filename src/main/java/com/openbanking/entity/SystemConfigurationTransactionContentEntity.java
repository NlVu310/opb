package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "system_configuration_transaction_content")
public class SystemConfigurationTransactionContentEntity extends BaseEntity {
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "source")
    private Long source;

    @Column(name = "ref_no_length")
    private Long refNoLength;

    @Column(name = "ref_no_start")
    private Long refNoStart;

    @Column(name = "ref_no_end")
    private Long refNoEnd;
}
