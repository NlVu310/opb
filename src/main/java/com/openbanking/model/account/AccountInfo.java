package com.openbanking.model.account;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

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
    LocalDateTime getCreatedAt();
}

