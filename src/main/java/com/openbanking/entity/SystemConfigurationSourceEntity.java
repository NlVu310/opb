package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import com.openbanking.enums.SourceConfigStatus;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "system_configuration_source")
public class SystemConfigurationSourceEntity extends BaseEntity {
    @Column(name = "partner_id")
    private Long partnerId;

    @Column(name = "code")
    private String code;

    @Column(name = "info")
    private String info;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SourceConfigStatus status;
}
