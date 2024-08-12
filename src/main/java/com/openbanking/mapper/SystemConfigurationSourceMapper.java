package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.AccountTypeEntity;
import com.openbanking.entity.SystemConfigurationSourceEntity;
import com.openbanking.model.account_type.AccountTypeDetail;
import com.openbanking.model.system_configuration_source.CreateSystemConfigurationSource;
import com.openbanking.model.system_configuration_source.SystemConfigurationSource;
import com.openbanking.model.system_configuration_source.UpdateSystemConfigurationSource;
import org.mapstruct.Mapper;

@Mapper
public interface SystemConfigurationSourceMapper extends BaseMapper<SystemConfigurationSourceEntity, SystemConfigurationSource, CreateSystemConfigurationSource, UpdateSystemConfigurationSource> {
}

