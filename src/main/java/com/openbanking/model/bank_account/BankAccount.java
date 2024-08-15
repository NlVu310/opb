package com.openbanking.model.bank_account;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
public class BankAccount extends BaseDTO {
    private Long customerId;
    private Long partnerId;
    private String partnerName;
    private String accountNumber;
    private String branch;
    private Long sourceId;
    private String sourceCode;
    private OffsetDateTime fromDate;
    private OffsetDateTime toDate;
    private String status;
}
