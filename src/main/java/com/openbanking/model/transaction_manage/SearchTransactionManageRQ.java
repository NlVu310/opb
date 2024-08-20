package com.openbanking.model.transaction_manage;

import com.openbanking.comon.SearchCriteria;
import lombok.Data;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Data
public class SearchTransactionManageRQ extends SearchCriteria {
    private Long id;
    private OffsetDateTime transactionDate;
    private String amount;
    private String dorc;
    private String content;
    private String source;
    private String refNo;
    private String senderAccount;
    private String senderAccountNo;
    private String senderBank;
    private String senderCode;
    private String receiverAccount;
    private String receiverAccountNo;
    private String receiverBank;
    private String receiverCode;
    private String sourceInstitution;
    private String status;
    private Long accountId;
}
