package com.openbanking.model.bank_account_edit_history;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import java.time.OffsetDateTime;
@Data
public class UpdateBankAccountEditHistory extends BaseDTO {
    private Long bankAccountId;
    private OffsetDateTime oldFromDate;
    private OffsetDateTime oldToDate;
    private OffsetDateTime NewFromDate;
    private OffsetDateTime newToDate;
}
