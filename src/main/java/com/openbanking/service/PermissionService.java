package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.model.permission.CreatePermission;
import com.openbanking.model.permission.Permission;
import com.openbanking.model.permission.UpdatePermission;

public interface PermissionService extends BaseService<Permission, CreatePermission, UpdatePermission, Long> {

}
