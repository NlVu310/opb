package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import com.openbanking.enums.PartnerStatus;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "partner")
public class PartnerEntity extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PartnerStatus status;

}
