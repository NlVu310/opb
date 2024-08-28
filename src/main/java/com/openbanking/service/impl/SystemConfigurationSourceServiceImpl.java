package com.openbanking.service.impl;

import com.openbanking.comon.*;
import com.openbanking.entity.*;
import com.openbanking.exception.InsertException;
import com.openbanking.exception.ResourceNotFoundException;
import com.openbanking.mapper.SystemConfigurationSourceMapper;
import com.openbanking.model.system_configuration_source.*;
import com.openbanking.repository.PartnerRepository;
import com.openbanking.repository.SystemConfigurationSourceRepository;
import com.openbanking.service.SystemConfigurationSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystemConfigurationSourceServiceImpl extends BaseServiceImpl<SystemConfigurationSourceEntity, SystemConfigurationSource, CreateSystemConfigurationSource, UpdateSystemConfigurationSource, Long> implements SystemConfigurationSourceService {

    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private SystemConfigurationSourceRepository systemConfigurationSourceRepository;

    @Autowired
    private SystemConfigurationSourceMapper systemConfigurationSourceMapper;

    public SystemConfigurationSourceServiceImpl(BaseRepository<SystemConfigurationSourceEntity, Long> repository, BaseMapper<SystemConfigurationSourceEntity, SystemConfigurationSource, CreateSystemConfigurationSource, UpdateSystemConfigurationSource> mapper) {
        super(repository, mapper);
    }


    @Override
    public void create(CreateSystemConfigurationSource createSystemConfigurationSource) {
        try {
        Long partnerId = createSystemConfigurationSource.getPartnerId();
        List<CreateSourceRQ> sources = createSystemConfigurationSource.getSystemConfigurationSources();
        List<SystemConfigurationSourceEntity> entities = new ArrayList<>();
        for (CreateSourceRQ dtoItem : sources) {
            SystemConfigurationSourceEntity entity = systemConfigurationSourceMapper.getEntity(dtoItem);
            entity.setPartnerId(partnerId);
            entities.add(entity);
        }
        systemConfigurationSourceRepository.saveAll(entities);
        }catch (InsertException e) {
            throw e;
        }catch (Exception e) {
            throw new RuntimeException("Failed to create Source", e);
        }
    }

    @Override
    public void deleteListById(List<Long> ids) {
        try {
        systemConfigurationSourceRepository.deleteAllById(ids);
        }catch (InsertException e) {
            throw e;
        }catch (Exception e) {
            throw new RuntimeException("Failed to delete Source", e);
        }
    }

    @Override
    public SystemConfigurationSource getById(Long id) {
        try {
        var entity = systemConfigurationSourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Source not found with id " + id));
        var rs = systemConfigurationSourceMapper.toDTO(entity);
        PartnerEntity partner = partnerRepository.findById(entity.getPartnerId()).orElseThrow(() -> new ResourceNotFoundException("Partner not found with id " + entity.getPartnerId()));
        rs.setPartnerName(partner.getName());
        rs.setPartnerId(partner.getId());
        return rs;
    }
        catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch SourceDetail", e);
        }
    }

    @Override
    public PaginationRS<SystemConfigurationSource> getAll(SearchCriteria searchCriteria) {
        Pageable pageable = PageRequest.of(
                searchCriteria != null && searchCriteria.getPage() != null ? searchCriteria.getPage() : 0,
                searchCriteria != null && searchCriteria.getSize() != null ? searchCriteria.getSize() : 10,
                searchCriteria != null && searchCriteria.getSortDirection() != null ?
                        Sort.Direction.fromString(searchCriteria.getSortDirection()) : Sort.Direction.DESC,
                searchCriteria != null && searchCriteria.getSortBy() != null ? searchCriteria.getSortBy() : "id"
        );

        Page<SystemConfigurationSourceProjection> page = systemConfigurationSourceRepository.findByTerm(
                searchCriteria.getTerm(),
                pageable
        );

        List<SystemConfigurationSource> content = page.getContent().stream()
                .map(systemConfigurationSourceMapper::getDTO).collect(Collectors.toList());

        PaginationRS<SystemConfigurationSource> response = new PaginationRS<>();
        response.setContent(content);
        response.setPageNumber(page.getNumber() + 1);
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());

        return response;
    }

    @Override
    public List<SystemConfigurationSource> getListSourceByPartnerId(Long id) {
        List<SystemConfigurationSourceEntity> sourceEntities  = systemConfigurationSourceRepository.getListSourceByPartnerId(id);
        return systemConfigurationSourceMapper.toDTOs(sourceEntities);
    }

    @Override
    public List<String> getListSourceCode() {
        List<String> listSourceCode = systemConfigurationSourceRepository.findDistinctCode();
        return listSourceCode;
    }
}


