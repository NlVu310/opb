package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "account_type")
public class AccountTypeEntity extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "note")
    private String note;
}
