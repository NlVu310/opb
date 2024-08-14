package com.openbanking.model.bank_account_edit_history;

import java.time.OffsetDateTime;

public class CreateBankAccountEditHistory {
    private Long bankAccountId;
    private OffsetDateTime oldFromDate;
    private OffsetDateTime oldToDate;
    private OffsetDateTime newFromDate;
    private OffsetDateTime newToDate;
}
