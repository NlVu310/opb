package com.openbanking.model.account;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

@Data
public class UpdateAccount extends BaseDTO {
    private String name;
    private String username;
    private Long customerId;
    private Long accountTypeId;
    private String email;
    private String phone;
    private String status;
    private String note;
}
