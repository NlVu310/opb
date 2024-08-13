package com.openbanking.model.account;

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
    private Long customerId;
    private Long accountTypeId;
    private String email;
    private String phone;
    private String status;
    private String note;
}

