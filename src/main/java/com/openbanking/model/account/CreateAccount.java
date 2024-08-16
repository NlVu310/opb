package com.openbanking.model.account;

import com.openbanking.enums.AccountStatus;
import com.openbanking.validator.ValidEmail;
import com.openbanking.validator.ValidPhone;
import com.sun.istack.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccount {
    @NotNull
    private String name;
    @NotNull
    private String username;
    @NotNull
    private Long customerId;
    @NotNull
    private Long accountTypeId;
    @ValidEmail
    private String email;
    @ValidPhone
    private String phone;
    @NotNull
    private AccountStatus status;
    private String note;
}

