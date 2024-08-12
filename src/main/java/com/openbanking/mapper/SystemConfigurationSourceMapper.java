package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.SystemConfigurationSourceEntity;
import com.openbanking.model.system_configuration_source.*;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface SystemConfigurationSourceMapper extends BaseMapper<SystemConfigurationSourceEntity, SystemConfigurationSource, CreateSystemConfigurationSource, UpdateSystemConfigurationSource> {
    SystemConfigurationSourceEntity getEntity(CreateSourceRQ rq);
    SystemConfigurationSource getDTO(SystemConfigurationSourceProjection sourceProjection);
}

