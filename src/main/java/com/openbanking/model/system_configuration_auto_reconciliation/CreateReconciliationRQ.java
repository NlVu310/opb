package com.openbanking.model.system_configuration_auto_reconciliation;

import com.openbanking.enums.ReconciliationFrequencyUnit;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
public class CreateReconciliationRQ {
    @NotBlank(message = "Source code must not be blank")
    private String sourceCode;
    @NotBlank(message = "Reconciliation time must not be blank")
    private String reconciliationTime;
    @NotNull
    private Integer reconciliationFrequencyNumber;
    @NotNull
    private ReconciliationFrequencyUnit reconciliationFrequencyUnit;
    @NotNull
    private Integer retryTimeNumber;
    @NotNull
    private Integer retryFrequencyNumber;
}
