package com.openbanking.model.partner;

import com.openbanking.comon.BaseDTO;
import com.openbanking.enums.PartnerStatus;
import com.openbanking.model.system_configuration_source.SystemConfigurationSource;
import lombok.Data;

import java.util.List;

@Data
public class PartnerDetail extends BaseDTO {
    private String name;

    private String address;

    private String email;

    private String phone;

    private PartnerStatus status;
    private String code;

    private List<SystemConfigurationSource> sources;
}
