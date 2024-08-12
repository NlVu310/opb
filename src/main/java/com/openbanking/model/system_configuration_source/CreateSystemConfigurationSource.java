package com.openbanking.model.system_configuration_source;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import java.util.List;

@Data
public class CreateSystemConfigurationSource extends BaseDTO {
    private Long partnerId;
    private List<CreateSourceRQ> systemConfigurationSources;
}
