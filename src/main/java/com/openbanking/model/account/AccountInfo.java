package com.openbanking.model.account;

import java.time.LocalDateTime;

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
    Long getCreatedBy();
    LocalDateTime getCreatedAt();
}

