package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "account_type_permission")
public class AccountTypePermissionEntity extends BaseEntity {
    @Column(name = "account_type_id")
    private Long accountTypeId;

    @Column(name = "permission_id")
    private Long permissionId;
}
