package com.openbanking.model.account;

import com.openbanking.comon.BaseDTO;
import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class CreateAccount extends BaseDTO {
    @NotNull
    private String name;
    @NotNull
    private String username;
    private Long customerId;
    private Long accountTypeId;
    private String email;
    private String phone;
    private String status;
    private String note;
}
