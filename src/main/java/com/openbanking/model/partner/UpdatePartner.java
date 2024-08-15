package com.openbanking.model.partner;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

@Data
public class UpdatePartner extends BaseDTO {
    private String name;

    private String email;

    private String phone;

    private String status;
}
