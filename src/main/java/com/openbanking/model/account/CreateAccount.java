package com.openbanking.model.account;

import com.openbanking.enums.AccountStatus;
import com.openbanking.validator.ValidEmail;
import com.openbanking.validator.ValidPhone;
import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccount {
    @NotBlank(message = "Name must not be blank")
    private String name;
    @NotBlank(message = "Username must not be blank")
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

