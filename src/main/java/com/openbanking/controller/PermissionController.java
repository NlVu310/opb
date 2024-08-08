package com.openbanking.controller;

import com.openbanking.comon.BaseController;
import com.openbanking.comon.PaginationRS;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.model.permission.CreatePermission;
import com.openbanking.model.permission.Permission;
import com.openbanking.model.permission.UpdatePermission;
import com.openbanking.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/permission")
public class PermissionController extends BaseController<Permission, CreatePermission, UpdatePermission, Long> {
    @Autowired
    private PermissionService permissionService;

    @PostMapping("/getAll")
    public ResponseBuilder<PaginationRS<Permission>> getAll(@RequestBody(required = false) SearchCriteria rq) {
        PaginationRS<Permission> listPermission = permissionService.getAll(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", listPermission);
    }
}
