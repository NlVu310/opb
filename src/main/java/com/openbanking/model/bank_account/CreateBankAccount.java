package com.openbanking.model.bank_account;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
public class CreateBankAccount  {
    private Long partnerId;
    private String partnerName;
    @NotBlank(message = "Account Number must not be blank")
    private String accountNumber;
    private String branch;
    private Long sourceId;
    @NotBlank(message = "Source code must not be blank")
    private String sourceCode;
    @NotNull
    private OffsetDateTime fromDate;
    private OffsetDateTime toDate;
}
