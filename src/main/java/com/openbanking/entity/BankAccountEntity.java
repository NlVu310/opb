package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "bank_account")
public class BankAccountEntity extends BaseEntity {
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "partner_id")
    private Long partnerId;

    @Column(name = "partner_name")
    private String partnerName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "branch")
    private String branch;
    @Column(name = "source_id")
    private Long sourceId;
    @Column(name = "source_code")
    private String sourceCode;

    @Column(name = "from_date")
    private OffsetDateTime fromDate;

    @Column(name = "to_date")
    private OffsetDateTime toDate;
    @Column(name = "status")
    private String status;

}
