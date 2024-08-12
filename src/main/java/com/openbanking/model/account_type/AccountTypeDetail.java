package com.openbanking.model.account_type;

import com.openbanking.comon.BaseDTO;
import com.openbanking.model.permission.PermissionRS;
import lombok.Data;


@Data
public class AccountTypeDetail extends BaseDTO {
    private String name;
    private String note;
    private PermissionRS permissions;
}
