package com.openbanking.model.system_configuration_auto_reconciliation;

import com.openbanking.comon.BaseDTO;
import com.openbanking.enums.ReconciliationFrequencyUnit;
import lombok.Data;

@Data
public class SystemConfigurationAutoReconciliation extends BaseDTO {
    private String reconciliationTime;
    private Integer reconciliationFrequencyNumber;
    private ReconciliationFrequencyUnit reconciliationFrequencyUnit;
    private Integer retryTimeNumber;
    private Integer retryFrequencyNumber;
    private Long reconciliationDay;
    private SourceInfo source;
    private PartnerInfo partner;

    @Data
    public static class SourceInfo{
        public Long id;
        public String code;
    }
    @Data
    public static class PartnerInfo{
        public Long id;
        public String name;
    }


}
