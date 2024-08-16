package com.openbanking.model.system_configuration_auto_reconciliation;

import com.openbanking.comon.BaseDTO;
import lombok.Data;

@Data
public class SystemConfigurationAutoReconciliation extends BaseDTO {
    private String reconciliationTime;
    private Integer reconciliationFrequencyNumber;
    private String reconciliationFrequencyUnit;
    private Integer retryTimeNumber;
    private Integer retryFrequencyNumber;
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
