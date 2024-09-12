package com.openbanking.service.impl;

import com.openbanking.comon.*;
import com.openbanking.entity.TransactionManageEntity;
import com.openbanking.entity.TransactionManageReconciliationHistoryEntity;
import com.openbanking.enums.TransactionStatus;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionEnum;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionService;
import com.openbanking.mapper.TransactionManageMapper;
import com.openbanking.mapper.TransactionManageReconciliationHistoryMapper;
import com.openbanking.model.transaction_manage.*;
import com.openbanking.model.transaction_manage_reconciliation_history.TransactionManageReconciliationHistory;
import com.openbanking.repository.SystemConfigurationTransactionContentRepository;
import com.openbanking.repository.TransactionManageReconciliationHistoryRepository;
import com.openbanking.repository.TransactionManageRepository;
import com.openbanking.service.TransactionManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    private SystemConfigurationTransactionContentRepository systemConfigurationTransactionContentRepository;
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

        Page<TransactionManageEntity> transactionManageEntities = transactionManageRepository.searchTransactions(searchRQ, searchRQ.getTransactionDate() , searchRQ.getTerm(), pageable);

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
                .orElseThrow(() -> new ResourceNotFoundExceptionService(ResourceNotFoundExceptionEnum.RNF_TRANS, "with id " + id));

        List<TransactionManageReconciliationHistoryEntity> entities = transactionManageReconciliationHistoryRepository.getListByTransactionManageId(id);

        TransactionManageDetail transactionManageDetail = transactionManageMapper.getDetail(transactionManageEntity);

        List<TransactionManageReconciliationHistory> transactionManageReconciliationHistories = entities.stream()
                .map(transactionManageReconciliationHistoryMapper::toDTO)
                .collect(Collectors.toList());

        transactionManageDetail.setTransactionManageReconciliationHistories(transactionManageReconciliationHistories);

        return transactionManageDetail;
        } catch (ResourceNotFoundExceptionService e) {
            throw e;
        }
        catch (Exception e) {
            throw new ResourceNotFoundExceptionService(ResourceNotFoundExceptionEnum.RNF_TRANS, "");
        }
    }

    @Override
    @Transactional
    public void handleIconnectTransactions(List<Iconnect> iconnects) {
        if (iconnects == null || iconnects.isEmpty()) {
            return;
        }

        List<TransactionManageEntity> entities = iconnects.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        transactionManageRepository.saveAllAndFlush(entities);
    }

    private TransactionManageEntity convertToEntity(Iconnect iconnect) {
        if (iconnect == null) {
            return null;
        }

        TransactionManageEntity entity = new TransactionManageEntity();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

        String dateStr = iconnect.getTransDate();
        String timeStr = iconnect.getTransTime();

        if (dateStr != null && timeStr != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyHHmmss");
            String combinedDateTime = dateStr + timeStr;
            OffsetDateTime dateTime = OffsetDateTime.parse(combinedDateTime, formatter.withZone(ZoneOffset.UTC));
            entity.setTransactionDate(dateTime);
        }

        entity.setAmount(iconnect.getAmount());
        entity.setDorc(iconnect.getDorc());
        entity.setContent(iconnect.getRemark());
        entity.setTransactionId(iconnect.getRefNo());

        if ("D".equals(iconnect.getDorc())) {
            entity.setSenderAccount(iconnect.getFrAccName());
            entity.setSenderAccountNo(iconnect.getFrAccNo());
            entity.setSenderBank(iconnect.getFrBankName());
            entity.setSenderCode(iconnect.getFrBankCode());
        } else if ("C".equals(iconnect.getDorc())) {
            entity.setReceiverAccount(iconnect.getFrAccName());
            entity.setReceiverAccountNo(iconnect.getFrAccNo());
            entity.setReceiverBank(iconnect.getFrBankName());
            entity.setReceiverCode(iconnect.getFrBankCode());
        }
        entity.setStatus(TransactionStatus.AWAITING_RECONCILIATION);
        entity.setSourceInstitution(iconnect.getFrom());
        return entity;
    }
}
