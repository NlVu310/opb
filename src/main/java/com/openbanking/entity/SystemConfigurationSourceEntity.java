package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "system_configuration_source")
public class SystemConfigurationSourceEntity extends BaseEntity {
    @Column(name = "partner_id")
    private Long partnerId;
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "code")
    private String code;
    @Column(name = "info")
    private String info;
    @Column(name = "description")
    private String description;
    @Column(name = "account_id")
    private Long accountId;

}
