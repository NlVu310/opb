package com.openbanking.model.system_configuration_transaction_content;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateSystemConfigurationTransactionContent{
    @NotNull
    private Long customerId;
    @NotNull
    private Long source;
    @NotNull
    private Long refNoLength;
    @NotNull
    private Long refNoStart;
    @NotNull
    private Long refNoEnd;
}
