package com.openbanking.model.transaction_manage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.enums.TransactionStatus;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
public class SearchTransactionManageRQ extends SearchCriteria {
    private Long id;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate transactionDate;
    private String amount;
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
    private TransactionStatus status;
    private String transactionId;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate reconciliationDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

}
