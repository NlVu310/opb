package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.PermissionEntity;

import com.openbanking.model.permission.CreatePermission;
import com.openbanking.model.permission.Permission;
import com.openbanking.model.permission.UpdatePermission;
import com.openbanking.service.PermissionService;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionEntity, Permission, CreatePermission, UpdatePermission, Long> implements PermissionService {
    public PermissionServiceImpl(BaseRepository<PermissionEntity, Long> repository, BaseMapper<PermissionEntity, Permission, CreatePermission, UpdatePermission> mapper) {
        super(repository, mapper);
    }

}
