package com.openbanking.model.account;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Account extends BaseDTO {
    private String name;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String status;
    private String note;
    private String customerName;
    private String accountTypeName;
    private OffsetDateTime createdAt;
    private Long createdBy;
}
