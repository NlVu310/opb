package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.comon.PaginationRS;
import com.openbanking.entity.PermissionEntity;
import com.openbanking.model.permission.CreatePermission;
import com.openbanking.model.permission.Permission;
import com.openbanking.model.permission.PermissionRS;
import com.openbanking.model.permission.UpdatePermission;
import com.openbanking.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionEntity, Permission, CreatePermission, UpdatePermission, Long> implements PermissionService {
    public PermissionServiceImpl(BaseRepository<PermissionEntity, Long> repository, BaseMapper<PermissionEntity, Permission, CreatePermission, UpdatePermission> mapper) {
        super(repository, mapper);
    }


    @Override
    public PaginationRS<PermissionRS> convertToRS(PaginationRS<Permission> permissions) {
        PaginationRS<PermissionRS> permissionRSPage = new PaginationRS<>();

        PermissionRS permissionRSContent = PermissionRS.convertToPermissionRS(permissions.getContent());

        permissionRSPage.setContent(List.of(permissionRSContent));
        permissionRSPage.setPageNumber(permissions.getPageNumber());
        permissionRSPage.setPageSize(permissions.getPageSize());
        permissionRSPage.setTotalElements(permissions.getTotalElements());
        permissionRSPage.setTotalPages(permissions.getTotalPages());

        return permissionRSPage;
    }


}
