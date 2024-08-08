package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.model.permission.CreatePermission;
import com.openbanking.model.permission.Permission;
import com.openbanking.model.permission.UpdatePermission;
import com.openbanking.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/permission")
public class PermissionController extends BaseController<Permission, CreatePermission, UpdatePermission, Long> {
    @Autowired
    private PermissionService permissionService;
}
