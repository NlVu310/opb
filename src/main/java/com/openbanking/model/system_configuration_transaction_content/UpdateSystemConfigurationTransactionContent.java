package com.openbanking.model.system_configuration_transaction_content;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateSystemConfigurationTransactionContent extends BaseDTO {
    @NotNull
    private Long customerId;
    @NotNull
    private String sourceStart;
    @NotNull
    private Long sourceLengthEnd;
    @NotNull
    private String sourceIndexEnd;
    @NotNull
    private String sourceRegex;
    @NotNull
    private String refNoStart;
    @NotNull
    private Long refNoLengthEnd;
    @NotNull
    private String refNoIndexEnd;
    @NotNull
    private String refNoRegex;
}
