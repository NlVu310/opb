package com.openbanking.model.bank_account_edit_history;

import com.openbanking.comon.BaseDTO;
import lombok.Data;
import java.time.OffsetDateTime;
@Data
public class BankAccountEditHistory extends BaseDTO {
    private Long bankAccountId;
    private OffsetDateTime oldFromDate;
    private OffsetDateTime oldToDate;
    private OffsetDateTime newFromDate;
    private OffsetDateTime newToDate;
}
