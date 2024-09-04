package com.openbanking.model.bank_account;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
public class UpdateBankAccount extends BaseDTO implements BankAccountProjection {
    private Long partnerId;
    private String partnerName;
    private String accountNumber;
    private String branch;
    private Long sourceId;
    private String sourceCode;
    private OffsetDateTime fromDate;
    private OffsetDateTime toDate;
}
