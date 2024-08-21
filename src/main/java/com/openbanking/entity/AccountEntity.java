package com.openbanking.entity;
import com.openbanking.comon.BaseEntity;
import com.openbanking.enums.AccountStatus;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;


@Data
@Entity
@Table(name = "account")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class AccountEntity extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "note")
    private String note;
    @Column(name = "account_type_id")
    private Long accountTypeId;

    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "customer_concerned", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private List<Long> customerConcerned;

    @Column(name = "partner_concerned", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private List<Long> partnerConcerned;
}


