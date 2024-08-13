package com.openbanking.model.system_configuration_auto_reconciliation;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdateSystemConfigurationAutoReconciliation extends BaseDTO {
    private LocalTime reconciliationTime;
    private Integer reconciliationFrequencyNumber;
    private String reconciliationFrequencyUnit;
    private Integer retryTimeNumber;
    private Integer retryFrequencyNumber;
}
