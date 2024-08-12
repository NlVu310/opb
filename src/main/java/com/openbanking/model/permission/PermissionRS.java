package com.openbanking.model.permission;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PermissionRS {
    List<Permission> account;
    List<Permission> data;


    public static PermissionRS convertToPermissionRS(List<Permission> permissions) {
        PermissionRS permissionRS = new PermissionRS();

        List<Permission> accountPermissions = permissions.stream()
                .filter(permission -> "acc".equals(permission.getKey()))
                .collect(Collectors.toList());

        List<Permission> dataPermissions = permissions.stream()
                .filter(permission -> "data".equals(permission.getKey()))
                .collect(Collectors.toList());

        permissionRS.setAccount(accountPermissions);
        permissionRS.setData(dataPermissions);

        return permissionRS;
    }
}
