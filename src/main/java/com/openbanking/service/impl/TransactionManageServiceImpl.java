package com.openbanking.service.impl;

import com.openbanking.comon.*;
import com.openbanking.entity.PartnerEntity;
import com.openbanking.entity.SystemConfigurationSourceEntity;
import com.openbanking.entity.TransactionManageEntity;
import com.openbanking.entity.TransactionManageReconciliationHistoryEntity;
import com.openbanking.exception.ResourceNotFoundException;
import com.openbanking.mapper.TransactionManageMapper;
import com.openbanking.mapper.TransactionManageReconciliationHistoryMapper;
import com.openbanking.model.partner.PartnerDetail;
import com.openbanking.model.system_configuration_source.SystemConfigurationSource;
import com.openbanking.model.transaction_manage.*;
import com.openbanking.model.transaction_manage_reconciliation_history.TransactionManageReconciliationHistory;
import com.openbanking.repository.TransactionManageReconciliationHistoryRepository;
import com.openbanking.repository.TransactionManageRepository;
import com.openbanking.service.TransactionManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionManageServiceImpl extends BaseServiceImpl<TransactionManageEntity, TransactionManage, CreateTransactionManage, UpdateTransactionManage, Long> implements TransactionManageService {

    @Autowired
    private TransactionManageRepository transactionManageRepository;

    @Autowired
    private TransactionManageMapper transactionManageMapper;

    @Autowired
    private TransactionManageReconciliationHistoryMapper transactionManageReconciliationHistoryMapper;
    @Autowired
    private TransactionManageReconciliationHistoryRepository transactionManageReconciliationHistoryRepository;

    public TransactionManageServiceImpl(BaseRepository<TransactionManageEntity, Long> repository, BaseMapper<TransactionManageEntity, TransactionManage, CreateTransactionManage, UpdateTransactionManage> mapper) {
        super(repository, mapper);
    }

    @Override
    public PaginationRS<TransactionManage> getListTransaction(SearchTransactionManageRQ searchRQ) {
        if (searchRQ == null) {
            searchRQ = new SearchTransactionManageRQ();
        }
        Pageable pageable = PageRequest.of(
                searchRQ.getPage() != null ? searchRQ.getPage() : 0,
                searchRQ.getSize() != null ? searchRQ.getSize() : 10,
                Sort.by(Sort.Direction.fromString(
                                searchRQ.getSortDirection() != null ? searchRQ.getSortDirection() : "DESC"),
                        searchRQ.getSortBy() != null ? searchRQ.getSortBy() : "id")
        );

        Page<TransactionManageEntity> transactionManageEntities = transactionManageRepository.searchTransactions(searchRQ, searchRQ.getTerm(), pageable);

        List<TransactionManage> transactionManages = transactionManageEntities.getContent()
                .stream()
                .map(transactionManageMapper::toDTO)
                .collect(Collectors.toList());

        PaginationRS<TransactionManage> result = new PaginationRS<>();
        result.setContent(transactionManages);
        result.setPageNumber(transactionManageEntities.getNumber());
        result.setPageSize(transactionManageEntities.getSize());
        result.setTotalElements(transactionManageEntities.getTotalElements());
        result.setTotalPages(transactionManageEntities.getTotalPages());

        return result;
    }

    @Override
    public TransactionManageDetail getDetailById(Long id) {
        try{
        TransactionManageEntity transactionManageEntity = transactionManageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partner not found with id " + id));

        List<TransactionManageReconciliationHistoryEntity> entities = transactionManageReconciliationHistoryRepository.getListByTransactionManageId(id);

        TransactionManageDetail transactionManageDetail = transactionManageMapper.getDetail(transactionManageEntity);

        List<TransactionManageReconciliationHistory> transactionManageReconciliationHistories = entities.stream()
                .map(transactionManageReconciliationHistoryMapper::toDTO)
                .collect(Collectors.toList());

        transactionManageDetail.setTransactionManageReconciliationHistories(transactionManageReconciliationHistories);

        return transactionManageDetail;

    }catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch AccountTypeDetail", e);
        }
    }

}
