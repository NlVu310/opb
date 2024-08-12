package com.openbanking.model.account_type_permission;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateAccountTypePermission {
    @NotNull
    private Long accountTypeId;
    @NotNull
    private Long permissionId;
}
