package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.CustomerEntity;
import com.openbanking.entity.PartnerEntity;
import com.openbanking.entity.SystemConfigurationTransactionContentEntity;
import com.openbanking.exception.ResourceNotFoundException;
import com.openbanking.mapper.SystemConfigurationTransactionContentMapper;
import com.openbanking.model.system_configuration_source.SystemConfigurationSource;
import com.openbanking.model.system_configuration_transaction_content.CreateSystemConfigurationTransactionContent;
import com.openbanking.model.system_configuration_transaction_content.SystemConfigurationTransactionContent;
import com.openbanking.model.system_configuration_transaction_content.UpdateSystemConfigurationTransactionContent;
import com.openbanking.repository.CustomerRepository;
import com.openbanking.repository.SystemConfigurationTransactionContentRepository;
import com.openbanking.service.SystemConfigurationSourceService;
import com.openbanking.service.SystemConfigurationTransactionContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    };
}
