package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "reconciliation_manage")
public class ReconciliationManageEntity extends BaseEntity {
    @Column(name = "transaction_date")
    private Timestamp transactionDate;

    @Column(name = "amount")
    private String amount;

    @Column(name = "content")
    private String content;

    @Column(name = "source")
    private String source;

    @Column(name = "ref_no")
    private String refNo;

    @Column(name = "sender_account")
    private String senderAccount;

    @Column(name = "sender_account_no")
    private String senderAccountNo;

    @Column(name = "sender_bank")
    private String senderBank;

    @Column(name = "sender_code")
    private String senderCode;

    @Column(name = "receiver_account")
    private String receiverAccount;

    @Column(name = "receiver_bank")
    private String receiverBank;

    @Column(name = "receiver_code")
    private String receiverCode;

    @Column(name = "source_institution")
    private String sourceInstitution;

    @Column(name = "reconciler")
    private String reconciler;

    @Column(name = "status")
    private String status;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "transaction_manage_id")
    private Long transactionManageId;
}
