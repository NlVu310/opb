package com.openbanking.model.account_type;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AccountType extends BaseDTO {
    @NotNull
    private String name;
    private String note;
}
