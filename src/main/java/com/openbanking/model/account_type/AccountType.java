package com.openbanking.model.account_type;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

@Data
public class AccountType extends BaseDTO {
    private String name;
    private String note;
}
