package com.openbanking.model.account;

import java.time.OffsetDateTime;

public interface AccountInfo {
    Long getId();
    String getAccountName();
    String getPhone();
    String getEmail();
    String getNote();
    String getUsername();
    String getAccountTypeName();
    String getCustomerName();
    String getStatus();
    OffsetDateTime getCreatedAt();
}

