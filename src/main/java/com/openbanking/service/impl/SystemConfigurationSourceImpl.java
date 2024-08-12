package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.AccountTypeEntity;
import com.openbanking.entity.PartnerEntity;
import com.openbanking.entity.SystemConfigurationSourceEntity;
import com.openbanking.exception.ResourceNotFoundException;
import com.openbanking.mapper.SystemConfigurationSourceMapper;
import com.openbanking.model.system_configuration_source.CreateSourceRQ;
import com.openbanking.model.system_configuration_source.CreateSystemConfigurationSource;
import com.openbanking.model.system_configuration_source.SystemConfigurationSource;
import com.openbanking.model.system_configuration_source.UpdateSystemConfigurationSource;
import com.openbanking.repository.PartnerRepository;
import com.openbanking.repository.SystemConfigurationSourceRepository;
import com.openbanking.service.SystemConfigurationSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SystemConfigurationSourceImpl extends BaseServiceImpl<SystemConfigurationSourceEntity, SystemConfigurationSource, CreateSystemConfigurationSource, UpdateSystemConfigurationSource, Long> implements SystemConfigurationSourceService {

    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private SystemConfigurationSourceRepository systemConfigurationSourceRepository;

    @Autowired
    private SystemConfigurationSourceMapper systemConfigurationSourceMapper;

    public SystemConfigurationSourceImpl(BaseRepository<SystemConfigurationSourceEntity, Long> repository, BaseMapper<SystemConfigurationSourceEntity, SystemConfigurationSource, CreateSystemConfigurationSource, UpdateSystemConfigurationSource> mapper) {
        super(repository, mapper);
    }


    @Override
    public void create(CreateSystemConfigurationSource createSystemConfigurationSource) {
        Long partnerId = createSystemConfigurationSource.getPartnerId();
        List<CreateSourceRQ> sources = createSystemConfigurationSource.getSystemConfigurationSources();
        List<SystemConfigurationSourceEntity> entities = new ArrayList<>();
        for (CreateSourceRQ dtoItem : sources) {
            SystemConfigurationSourceEntity entity = systemConfigurationSourceMapper.getEntity(dtoItem);
            entity.setPartnerId(partnerId);
            entities.add(entity);
        }
        systemConfigurationSourceRepository.saveAll(entities);
    }

    @Override
    public void deleteListById(List<Long> ids) {
        systemConfigurationSourceRepository.deleteAllById(ids);
    }

    @Override
    public SystemConfigurationSource getById(Long id) {
        var entity = systemConfigurationSourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Source not found with id " + id));
        var rs = systemConfigurationSourceMapper.toDTO(entity);
        PartnerEntity partner = partnerRepository.findById(entity.getPartnerId()).orElseThrow(() -> new ResourceNotFoundException("Partner not found with id " + entity.getPartnerId()));
        rs.setPartnerName(partner.getName());
        return rs;
    };
}


