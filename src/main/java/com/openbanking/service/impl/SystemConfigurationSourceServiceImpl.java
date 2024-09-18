package com.openbanking.service.impl;

import com.openbanking.comon.*;
import com.openbanking.entity.*;
import com.openbanking.exception.delete_exception.DeleteExceptionEnum;
import com.openbanking.exception.delete_exception.DeleteException;
import com.openbanking.exception.insert_exception.InsertExceptionEnum;
import com.openbanking.exception.insert_exception.InsertException;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionEnum;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundException;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        Long partnerId = createSystemConfigurationSource.getPartnerId();
        List<CreateSourceRQ> sources = createSystemConfigurationSource.getSystemConfigurationSources();

        Set<String> uniqueCodes = new HashSet<>();
        for (CreateSourceRQ dtoItem : sources) {
            if (!uniqueCodes.add(dtoItem.getCode())) {
                throw new InsertException(InsertExceptionEnum.INSERT_SOURCE_DUP_ERROR, "");
            }
        }

        Set<String> codesToCheck = sources.stream()
                .map(CreateSourceRQ::getCode)
                .collect(Collectors.toSet());

        List<SystemConfigurationSourceEntity> existingEntities = systemConfigurationSourceRepository.findByCodeIn(codesToCheck);
        if (!existingEntities.isEmpty()) {
            throw new InsertException(InsertExceptionEnum.INSERT_SOURCE_CODE_ERROR, "");
        }

        List<SystemConfigurationSourceEntity> entities = sources.stream()
                .map(dtoItem -> {
                    SystemConfigurationSourceEntity entity = systemConfigurationSourceMapper.getEntity(dtoItem);
                    entity.setPartnerId(partnerId);
                    return entity;
                })
                .collect(Collectors.toList());
        try {
            systemConfigurationSourceRepository.saveAll(entities);
        } catch (InsertException e) {
            throw e;
        }
    }


    @Override
    public void deleteListById(List<Long> ids) {
        try {
        systemConfigurationSourceRepository.deleteAllById(ids);
        }catch (Exception e) {
            throw new DeleteException(DeleteExceptionEnum.DELETE_SYS_SOURCE_ERROR, "");
        }
    }

    @Override
    public SystemConfigurationSource getById(Long id) {
        try {
        var entity = systemConfigurationSourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_SOURCE ,"with id " + id));
        var rs = systemConfigurationSourceMapper.toDTO(entity);
        PartnerEntity partner = partnerRepository.findById(entity.getPartnerId()).orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_PARTNER , "with id " + entity.getPartnerId()));
        rs.setPartnerName(partner.getName());
        rs.setPartnerId(partner.getId());
        return rs;
        }catch (ResourceNotFoundException e){
            throw e;
        }
        catch (Exception e) {
            throw new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_SOURCE ,"");
        }
    }

    @Override
    public PaginationRS<SystemConfigurationSource> getAll(SearchCriteria searchCriteria) {
        if (searchCriteria == null || (searchCriteria.getPage() == null && searchCriteria.getSize() == null &&
                searchCriteria.getSortDirection() == null && searchCriteria.getSortBy() == null &&
                searchCriteria.getTerm() == null)) {
            List<SystemConfigurationSourceEntity> entities = systemConfigurationSourceRepository.findAll();

            List<SystemConfigurationSource> content = entities.stream()
                    .map(systemConfigurationSourceMapper::toDTO)
                    .collect(Collectors.toList());

            PaginationRS<SystemConfigurationSource> response = new PaginationRS<>();
            response.setContent(content);
            response.setPageNumber(1);
            response.setPageSize(content.size());
            response.setTotalElements(content.size());
            response.setTotalPages(1);

            return response;
        } else {
            Pageable pageable = PageRequest.of(
                    searchCriteria.getPage() != null ? searchCriteria.getPage() : 0,
                    searchCriteria.getSize() != null ? searchCriteria.getSize() : 10,
                    searchCriteria.getSortDirection() != null ?
                            Sort.Direction.fromString(searchCriteria.getSortDirection()) : Sort.Direction.DESC,
                    searchCriteria.getSortBy() != null ? searchCriteria.getSortBy() : "id"
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


