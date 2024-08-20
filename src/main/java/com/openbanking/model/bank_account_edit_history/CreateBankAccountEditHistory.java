package com.openbanking.model.bank_account_edit_history;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
@Data
public class CreateBankAccountEditHistory {
    @NotNull
    private Long bankAccountId;
    private OffsetDateTime oldFromDate;
    private OffsetDateTime oldToDate;
    private OffsetDateTime newFromDate;
    private OffsetDateTime newToDate;
}
