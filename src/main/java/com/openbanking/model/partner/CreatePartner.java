package com.openbanking.model.partner;

import com.openbanking.comon.BaseDTO;
import com.openbanking.enums.PartnerStatus;
import lombok.Data;

@Data
public class CreatePartner  {
    private String name;
    private String address;
    private String email;
    private String phone;
    private PartnerStatus status;
}
