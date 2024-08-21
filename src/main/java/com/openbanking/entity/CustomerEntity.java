package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import com.openbanking.enums.CustomerStatus;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "customer")
public class CustomerEntity extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "tax_no")
    private String taxNo;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "representative")
    private String representative;

    @Column(name = "representative_email")
    private String representativeEmail;

    @Column(name = "representative_phone")
    private String representativePhone;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    @Column(name = "is_parent")
    private Boolean isParent;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "code")
    private String code;
}
