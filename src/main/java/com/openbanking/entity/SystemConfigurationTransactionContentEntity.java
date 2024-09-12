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
    @Column(name = "source_start")
    private String sourceStart;
    @Column(name = "source_length_end")
    private Long sourceLengthEnd;
    @Column(name = "source_index_end")
    private String sourceIndexEnd;
    @Column(name = "source_regex")
    private String sourceRegex;
    @Column(name = "ref_no_start")
    private String refNoStart;
    @Column(name = "ref_no_length_end")
    private Long refNoLengthEnd;
    @Column(name = "ref_no_index_end")
    private String refNoIndexEnd;
    @Column(name = "ref_no_regex")
    private String refNoRegex;
}
