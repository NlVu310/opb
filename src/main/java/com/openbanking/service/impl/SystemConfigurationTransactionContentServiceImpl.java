package com.openbanking.service.impl;

import com.openbanking.comon.*;
import com.openbanking.entity.CustomerEntity;
import com.openbanking.entity.SystemConfigurationTransactionContentEntity;
import com.openbanking.exception.delete_exception.DeleteExceptionEnum;
import com.openbanking.exception.delete_exception.DeleteException;
import com.openbanking.exception.insert_exception.InsertExceptionEnum;
import com.openbanking.exception.insert_exception.InsertException;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionEnum;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundException;
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
        try {
        systemConfigurationTransactionContentRepository.deleteAllById(ids);
    }catch (Exception e) {
            throw new DeleteException(DeleteExceptionEnum.DELETE_SYS_TRANS_ERROR, "");
        }
    }

    @Override
    public SystemConfigurationTransactionContent getById(Long id) {
        try {
        var entity = systemConfigurationTransactionContentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_TRANS ,"with id " + id));
        var rs = systemConfigurationTransactionContentMapper.toDTO(entity);
        CustomerEntity customer = customerRepository.findById(entity.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_CUS , "with id " + entity.getCustomerId()));
        rs.setCustomerName(customer.getName());
        return rs;
        }catch(ResourceNotFoundException e){
            throw e;
        }
        catch (Exception e) {
            throw new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_TRANS_CONT,"");
        }
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
        if (searchCriteria == null) searchCriteria = new SearchCriteria();
        Page<SystemConfigurationTransactionContentProjection> page = systemConfigurationTransactionContentRepository.findByTerm(
                searchCriteria.getTerm() == null ? null : searchCriteria.getTerm(),
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

    @Override
    public void createTransactionConfig(CreateSystemConfigurationTransactionContent rq, Long accountId) {
        try{
            List<Long> customerIds = systemConfigurationTransactionContentRepository.getListCustomerId();
            if (customerIds.contains(rq.getCustomerId()))
                throw new InsertException( InsertExceptionEnum.INSERT_TRANS_CONTENT_ERROR, "Id of customer: " + rq.getCustomerId() + "is already config");
            var entity = systemConfigurationTransactionContentMapper.toEntityFromCD(rq);
            entity.setCreatedBy(accountId);
            systemConfigurationTransactionContentRepository.save(entity);
        }catch (InsertException e){
            throw e;
        }catch (Exception e){
            throw new InsertException( InsertExceptionEnum.INSERT_TRANS_CONTENT_ERROR, "Config for this customer existed");
        }
    }

}
