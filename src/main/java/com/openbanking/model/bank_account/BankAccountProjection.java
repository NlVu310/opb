package com.openbanking.model.bank_account;

import java.time.OffsetDateTime;

public interface BankAccountProjection {
    OffsetDateTime getFromDate();
    OffsetDateTime getToDate();
    Long getPartnerId();
    String getAccountNumber();
    String getBranch();
    Long getSourceId();
    String getSourceCode();
}
