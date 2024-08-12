package com.openbanking.model.system_configuration_source;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import javax.persistence.Column;
@Data
public class SystemConfigurationSource extends BaseDTO {
    private String code;
    private String info;
    private String description;
    private String status;
    private String partnerName;
}
