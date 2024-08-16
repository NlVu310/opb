package com.openbanking.model.partner;

import com.openbanking.comon.BaseDTO;
import com.openbanking.enums.PartnerStatus;
import lombok.Data;

import javax.persistence.Column;

@Data
public class Partner extends BaseDTO {
    private String name;

    private String email;

    private String phone;

    private PartnerStatus status;

}
