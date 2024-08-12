package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.AccountTypePermissionEntity;
import com.openbanking.model.account_type_permission.AccountTypePermission;
import com.openbanking.model.account_type_permission.CreateAccountTypePermission;
import com.openbanking.model.account_type_permission.UpdateAccountTypePermission;
import org.mapstruct.Mapper;

@Mapper
public interface AccountTypePermissionMapper extends BaseMapper<AccountTypePermissionEntity, AccountTypePermission, CreateAccountTypePermission, UpdateAccountTypePermission> {
}
