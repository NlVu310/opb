package com.openbanking.model.system_configuration_source;

import com.openbanking.comon.BaseDTO;
import com.openbanking.enums.SourceConfigStatus;
import lombok.Data;

@Data
public class SystemConfigurationSource extends BaseDTO {
    private String code;
    private String info;
    private String description;
    private SourceConfigStatus status;
    private String partnerName;
    private Long partnerId;
}
