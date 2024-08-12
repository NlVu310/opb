package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.PermissionEntity;
import com.openbanking.model.permission.CreatePermission;
import com.openbanking.model.permission.Permission;
import com.openbanking.model.permission.UpdatePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<PermissionEntity, Permission, CreatePermission, UpdatePermission> {
}
