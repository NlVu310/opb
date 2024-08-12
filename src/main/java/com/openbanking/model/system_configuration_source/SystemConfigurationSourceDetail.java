package com.openbanking.model.system_configuration_source;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

@Data

public class SystemConfigurationSourceDetail extends BaseDTO {
    private Long partnerId;
    private String code;
    private String info;
    private String description;
    private Long accountId;
    private String status;
}
