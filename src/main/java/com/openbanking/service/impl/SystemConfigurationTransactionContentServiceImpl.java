package com.openbanking.service.impl;

import com.openbanking.comon.*;
import com.openbanking.entity.CustomerEntity;
import com.openbanking.entity.SystemConfigurationTransactionContentEntity;
import com.openbanking.exception.ResourceNotFoundException;
import com.openbanking.mapper.SystemConfigurationTransactionContentMapper;
import com.openbanking.model.system_configuration_transaction_content.CreateSystemConfigurationTransactionContent;
import com.openbanking.model.system_configuration_transaction_content.SystemConfigurationTransactionContent;
import com.openbanking.model.system_configuration_transaction_content.SystemConfigurationTransactionContentProjection;
import com.openbanking.model.system_configuration_transaction_content.UpdateSystemConfigurationTransactionContent;
import com.openbanking.repository.CustomerRepository;
import com.openbanking.repository.SystemConfigurationTransactionContentRepository;
import com.openbanking.service.SystemConfigurationTransactionContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystemConfigurationTransactionContentServiceImpl extends BaseServiceImpl<SystemConfigurationTransactionContentEntity, SystemConfigurationTransactionContent, CreateSystemConfigurationTransactionContent, UpdateSystemConfigurationTransactionContent, Long> implements SystemConfigurationTransactionContentService {

    @Autowired
    private SystemConfigurationTransactionContentRepository systemConfigurationTransactionContentRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SystemConfigurationTransactionContentMapper systemConfigurationTransactionContentMapper;

    public SystemConfigurationTransactionContentServiceImpl(BaseRepository<SystemConfigurationTransactionContentEntity, Long> repository, BaseMapper<SystemConfigurationTransactionContentEntity, SystemConfigurationTransactionContent, CreateSystemConfigurationTransactionContent, UpdateSystemConfigurationTransactionContent> mapper) {
        super(repository, mapper);
    }

    @Override
    public void deleteListById(List<Long> ids) {
        systemConfigurationTransactionContentRepository.deleteAllById(ids);
    }

    @Override
    public SystemConfigurationTransactionContent getById(Long id) {
        var entity = systemConfigurationTransactionContentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id " + id));
        var rs = systemConfigurationTransactionContentMapper.toDTO(entity);
        CustomerEntity customer = customerRepository.findById(entity.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + entity.getCustomerId()));
        rs.setCustomerName(customer.getName());
        return rs;
    }

    @Override
    public PaginationRS<SystemConfigurationTransactionContent> getAll(SearchCriteria searchCriteria) {
        Pageable pageable = PageRequest.of(
                searchCriteria != null && searchCriteria.getPage() != null ? searchCriteria.getPage() : 0,
                searchCriteria != null && searchCriteria.getSize() != null ? searchCriteria.getSize() : 10,
                searchCriteria != null && searchCriteria.getSortDirection() != null ?
                        Sort.Direction.fromString(searchCriteria.getSortDirection()) : Sort.Direction.DESC,
                searchCriteria != null && searchCriteria.getSortBy() != null ? searchCriteria.getSortBy() : "id"
        );

        Page<SystemConfigurationTransactionContentProjection> page = systemConfigurationTransactionContentRepository.findByTerm(
                searchCriteria.getTerm(),
                pageable
        );

        List<SystemConfigurationTransactionContent> content = page.getContent().stream()
                .map(systemConfigurationTransactionContentMapper::getDTO).collect(Collectors.toList());

        PaginationRS<SystemConfigurationTransactionContent> response = new PaginationRS<>();
        response.setContent(content);
        response.setPageNumber(page.getNumber() + 1);
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());

        return response;
    }

}
