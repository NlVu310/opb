package com.openbanking.model.account_type_permission;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

@Data
public class UpdateAccountTypePermission extends BaseDTO {
    private Long accountTypeId;
    private Long permissionId;
}
