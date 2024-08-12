package com.openbanking.service;

import com.openbanking.comon.BaseService;
import com.openbanking.comon.PaginationRS;
import com.openbanking.model.permission.CreatePermission;
import com.openbanking.model.permission.Permission;
import com.openbanking.model.permission.PermissionRS;
import com.openbanking.model.permission.UpdatePermission;

public interface PermissionService extends BaseService<Permission, CreatePermission, UpdatePermission, Long> {
    PaginationRS<PermissionRS> convertToRS(PaginationRS<Permission> permissions);
}
