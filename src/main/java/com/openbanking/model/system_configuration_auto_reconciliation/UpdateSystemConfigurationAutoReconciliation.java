package com.openbanking.model.system_configuration_auto_reconciliation;

import com.openbanking.comon.BaseDTO;
import com.openbanking.enums.ReconciliationFrequencyUnit;
import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdateSystemConfigurationAutoReconciliation extends BaseDTO {
    private LocalTime reconciliationTime;
    private Integer reconciliationFrequencyNumber;
    private ReconciliationFrequencyUnit reconciliationFrequencyUnit;
    private Integer retryTimeNumber;
    private Integer retryFrequencyNumber;
    private Long reconciliationDay;

}
