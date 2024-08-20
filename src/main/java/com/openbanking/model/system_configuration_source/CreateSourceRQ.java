package com.openbanking.model.system_configuration_source;

import com.openbanking.comon.BaseDTO;
import com.openbanking.enums.SourceConfigStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateSourceRQ extends BaseDTO {
    @NotBlank(message = "Code must not be blank")
    private String code;
    private String info;
    private String description;
    @NotNull
    private SourceConfigStatus status;
}
