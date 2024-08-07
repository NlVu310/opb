package com.openbanking.model.account;

import lombok.Data;

@Data
public class CreateAccount {
    private String name;
    private String username;
    private Long customerId;
    private Long accountTypeId;
    private String email;
    private String phone;
    private String status;
    private String note;
}
