package com.openbanking.entity;
import com.openbanking.comon.BaseEntity;
import lombok.Data;
import javax.persistence.*;


@Data
@Entity
@Table(name = "account")
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
    private String status;

    @Column(name = "note")
    private String note;
    @Column(name = "account_type_id")
    private Long accountTypeId;

    @Column(name = "customer_id")
    private Long customerId;
}

