package com.openbanking.mapper;

import com.openbanking.comon.BaseMapper;
import com.openbanking.entity.SystemConfigurationAutoReconciliationEntity;
import com.openbanking.model.system_configuration_auto_reconciliation.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Mapper(config = GlobalMapperConfig.class)
public interface SystemConfigurationAutoReconciliationMapper extends BaseMapper<SystemConfigurationAutoReconciliationEntity, SystemConfigurationAutoReconciliation, CreateSystemConfigurationAutoReconciliation, UpdateSystemConfigurationAutoReconciliation> {
    @Mapping(source = "sourceId", target = "source.id")
    @Mapping(source = "sourceCode", target = "source.code")
    @Mapping(source = "partnerName", target = "partner.name")
    SystemConfigurationAutoReconciliation toDTO(SystemConfigurationAutoReconciliationEntity entity);

    @Mapping(source = "sourceId", target = "source.id")
    @Mapping(source = "sourceCode", target = "source.code")
    SystemConfigurationAutoReconciliation getDTO(SystemConfigurationAutoReconciliationProjection projection);


    @Mapping(source = "reconciliationTime", target = "reconciliationTime", qualifiedByName = "stringToLocalTime")
    SystemConfigurationAutoReconciliationEntity getEntity(CreateReconciliationRQ rq);


    @Named("stringToLocalTime")
    default LocalTime stringToLocalTime(String localTimeStr) {
        return localTimeStr != null ? LocalTime.parse(localTimeStr) : null;
    }
}

