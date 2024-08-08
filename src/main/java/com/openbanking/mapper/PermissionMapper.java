package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.PermissionEntity;
import com.openbanking.model.permission.CreatePermission;
import com.openbanking.model.permission.Permission;
import com.openbanking.model.permission.UpdatePermission;
import org.mapstruct.Mapper;

@Mapper
public interface PermissionMapper extends BaseMapper<PermissionEntity, Permission, CreatePermission, UpdatePermission> {
}
