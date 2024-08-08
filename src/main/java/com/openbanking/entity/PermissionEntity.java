package com.openbanking.entity;

import com.openbanking.comon.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "permission")
public class PermissionEntity extends BaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "key")
    private String key;
}
