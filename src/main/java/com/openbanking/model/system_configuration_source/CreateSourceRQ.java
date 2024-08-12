package com.openbanking.model.system_configuration_source;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

@Data
public class CreateSourceRQ extends BaseDTO {
    private String code;
    private String info;
    private String description;
    private String status;
}
