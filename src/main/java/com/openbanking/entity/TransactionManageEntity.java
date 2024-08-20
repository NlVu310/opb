package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "transaction_manage")
public class TransactionManageEntity extends BaseEntity {
    @Column(name = "transaction_date")
    private OffsetDateTime transactionDate;

    @Column(name = "amount")
    private String amount;

    @Column(name = "dorc")
    private String dorc;

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

    @Column(name = "receiver_account_no")
    private String receiverAccountNo;

    @Column(name = "receiver_bank")
    private String receiverBank;

    @Column(name = "receiver_code")
    private String receiverCode;

    @Column(name = "source_institution")
    private String sourceInstitution;

    @Column(name = "status")
    private String status;

    @Column(name = "account_id")
    private Long accountId;

}
