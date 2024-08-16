package com.openbanking.model.account;

import com.openbanking.comon.SearchCriteria;
import com.openbanking.enums.AccountStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
public class SearchAccountRQ extends SearchCriteria {
    private Long id;
    private String name;
    private String userName;
    private String email;
    private String accountTypeName;
    private String customerName;
    private AccountStatus status;
    private Long createdBy;
    private LocalDateTime createdAt;
}
