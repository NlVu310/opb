package com.openbanking.model.system_configuration_source;

import com.openbanking.comon.BaseDTO;
import com.openbanking.enums.SourceConfigStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateSystemConfigurationSource extends BaseDTO {
    @NotNull
    private Long partnerId;
    private String code;
    private String info;
    private String description;
    private SourceConfigStatus status;
}
