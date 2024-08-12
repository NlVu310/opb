package com.openbanking.model.permission;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

@Data
public class Permission extends BaseDTO {
    private String code;
    private String name;
    private Long parentId;
    private String key;
}
