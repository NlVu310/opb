package com.openbanking.model.partner;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import javax.persistence.Column;

@Data
public class Partner extends BaseDTO {
    private String name;

    private String email;

    private String phone;

    private String status;

}
