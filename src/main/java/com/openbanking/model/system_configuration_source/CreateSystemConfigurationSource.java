package com.openbanking.model.system_configuration_source;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateSystemConfigurationSource {
    @NotNull
    private Long partnerId;
    private List<CreateSourceRQ> systemConfigurationSources;
}
