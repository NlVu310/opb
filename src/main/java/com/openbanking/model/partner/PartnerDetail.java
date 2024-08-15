package com.openbanking.model.partner;

import com.openbanking.comon.BaseDTO;
import com.openbanking.model.system_configuration_source.SystemConfigurationSource;
import lombok.Data;

import java.util.List;

@Data
public class PartnerDetail extends BaseDTO {
    private String name;

    private String address;

    private String email;

    private String phone;

    private String status;
    private List<SystemConfigurationSource> sources;
}
