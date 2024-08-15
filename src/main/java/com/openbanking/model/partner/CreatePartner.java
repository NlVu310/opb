package com.openbanking.model.partner;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

@Data
public class CreatePartner  {
    private Long customerId;

    private String name;

    private String email;

    private String phone;

    private String status;

    private Long accountId;
}
