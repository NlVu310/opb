package com.openbanking.model.account_type_permission;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import javax.persistence.Column;
@Data
public class AccountTypePermission extends BaseDTO {
    private Long accountTypeId;
    private Long permissionId;
}
