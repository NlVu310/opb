package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.comon.PaginationRS;
import com.openbanking.entity.*;
import com.openbanking.enums.TransactionStatus;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionEnum;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionService;
import com.openbanking.mapper.BankAccountMapper;
import com.openbanking.mapper.ReconciliationManageMapper;
import com.openbanking.mapper.TransactionManageReconciliationHistoryMapper;
import com.openbanking.model.bank_account.BankAccount;
import com.openbanking.model.reconciliation_manage.*;
import com.openbanking.model.transaction_manage.Iconnect;
import com.openbanking.model.transaction_manage.TransactionManage;
import com.openbanking.model.transaction_manage.TransactionManageDetail;
import com.openbanking.model.transaction_manage_reconciliation_history.TransactionManageReconciliationHistory;
import com.openbanking.repository.*;
import com.openbanking.service.ReconciliationManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ReconciliationManageServiceImpl extends BaseServiceImpl<ReconciliationManageEntity, ReconciliationManage,CreateReconciliationManage, UpdateReconciliationManage, Long> implements ReconciliationManageService {

    @Autowired
    private ReconciliationManageRepository reconciliationManageRepository;
    @Autowired
    private ReconciliationManageMapper reconciliationManageMapper;
    @Autowired
    private TransactionManageReconciliationHistoryMapper transactionManageReconciliationHistoryMapper;
    @Autowired
    private TransactionManageReconciliationHistoryRepository transactionManageReconciliationHistoryRepository;
    @Autowired
    private SystemConfigurationTransactionContentRepository systemConfigurationTransactionContentRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private SystemConfigurationSourceRepository systemConfigurationSourceRepository;
    @Autowired
    private BankAccountMapper bankAccountMapper;
    public ReconciliationManageServiceImpl(BaseRepository<ReconciliationManageEntity, Long> repository, BaseMapper<ReconciliationManageEntity, ReconciliationManage, CreateReconciliationManage, UpdateReconciliationManage> mapper) {
        super(repository, mapper);
    }


    @Override
    public PaginationRS<ReconciliationManage> getListReconciliation(SearchReconciliationRQ searchRQ) {
        if (searchRQ == null || (searchRQ.getPage() == null && searchRQ.getSize() == null &&
                searchRQ.getSortDirection() == null && searchRQ.getSortBy() == null &&
                searchRQ.getTerm() == null)) {
            List<ReconciliationManageEntity> entities = reconciliationManageRepository.findActiveTransactions();

            List<ReconciliationManage> content = entities.stream()
                    .map(reconciliationManageMapper::toDTO)
                    .collect(Collectors.toList());

            PaginationRS<ReconciliationManage> response = new PaginationRS<>();
            response.setContent(content);
            response.setPageNumber(1);
            response.setPageSize(content.size());
            response.setTotalElements(content.size());
            response.setTotalPages(1);

            return response;
        } else {
            Pageable pageable = PageRequest.of(
                    searchRQ.getPage() != null ? searchRQ.getPage() : 0,
                    searchRQ.getSize() != null ? searchRQ.getSize() : 10,
                    Sort.by(Sort.Direction.fromString(
                                    searchRQ.getSortDirection() != null ? searchRQ.getSortDirection() : "DESC"),
                            searchRQ.getSortBy() != null ? searchRQ.getSortBy() : "id")
            );

            Page<ReconciliationManageEntity> reconciliationManageEntities = reconciliationManageRepository.searchTransactions(searchRQ, searchRQ.getTransactionDate(), searchRQ.getReconciliationDate() , searchRQ.getTerm(), pageable);

            List<ReconciliationManage> reconciliationManages = reconciliationManageEntities.getContent()
                    .stream()
                    .map(reconciliationManageMapper::toDTO)
                    .collect(Collectors.toList());

            PaginationRS<ReconciliationManage> result = new PaginationRS<>();
            result.setContent(reconciliationManages);
            result.setPageNumber(reconciliationManageEntities.getNumber());
            result.setPageSize(reconciliationManageEntities.getSize());
            result.setTotalElements(reconciliationManageEntities.getTotalElements());
            result.setTotalPages(reconciliationManageEntities.getTotalPages());

            return result;
        }
    }

    @Override
    public void handleIconnectReconciliations(List<ReconciliationIconnect> reconciliationIconnects) {
        try{
            if (reconciliationIconnects == null || reconciliationIconnects.isEmpty()) {
                return;
            }

            List<SystemConfigurationTransactionContentEntity> configs = systemConfigurationTransactionContentRepository.findAll();
            List<BankAccountEntity> bankAccountEntities = bankAccountRepository.findAll();
            List<BankAccount> bankAccounts = bankAccountMapper.toDTOs(bankAccountEntities);

            List<ReconciliationManageEntity> entities = reconciliationIconnects.stream()
                    .map(reconciliationIconnect -> convertToEntity(reconciliationIconnect, configs, bankAccounts))
                    .collect(Collectors.toList());

            reconciliationManageRepository.saveAllAndFlush(entities);
        }catch (ResourceNotFoundExceptionService e){
            throw new ResourceNotFoundExceptionService(ResourceNotFoundExceptionEnum.RNF_TRANS, "" + e.getMessage());
        }

    }

    @Override
    public ReconciliationManageDetail getDetailById(Long id) {
        try {
            ReconciliationManageEntity reconciliationManageEntity = reconciliationManageRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundExceptionService(ResourceNotFoundExceptionEnum.RNF_TRANS, "with id " + id));


            List<TransactionManageReconciliationHistoryEntity> entities =
                    transactionManageReconciliationHistoryRepository.findByTransactionIdAndReconciliationSource(reconciliationManageEntity.getTransactionId() , reconciliationManageEntity.getSourceInstitution());
            ReconciliationManageDetail reconciliationManageDetail = reconciliationManageMapper.getDetail(reconciliationManageEntity);

            List<TransactionManageReconciliationHistory> transactionManageReconciliationHistories = entities.stream()
                    .map(transactionManageReconciliationHistoryMapper::toDTO)
                    .collect(Collectors.toList());

            reconciliationManageDetail.setTransactionManageReconciliationHistories(transactionManageReconciliationHistories);

            return reconciliationManageDetail;
        } catch (ResourceNotFoundExceptionService e) {
            throw new ResourceNotFoundExceptionService(ResourceNotFoundExceptionEnum.RNF_TRANS, "" + e.getMessage());
        }
    }

    private String extractPart(String remark, String start, String regex, String indexEnd, Long lengthEnd) {
        try {
            if (remark == null) {
                return null;
            }

            if (regex == null && indexEnd == null && lengthEnd == null) {
                throw new ResourceNotFoundExceptionService(ResourceNotFoundExceptionEnum.RNF_TRANS_CONT,
                        "At least one of 'regex', 'indexEnd', or 'lengthEnd' must be provided.");
            }

            if (regex != null && start == null) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(remark);

                if (matcher.find()) {
                    String matchedGroup = matcher.group();
                    return removeWhitespace(matchedGroup);
                }
                return getLimitedStringWithoutSpaces(remark);

            } else if (indexEnd != null) {
                int startPosition = remark.indexOf(start);
                if (startPosition != -1) {
                    int endPosition = remark.indexOf(indexEnd, startPosition + start.length());
                    int endPositionFinal = endPosition != -1 ? endPosition + indexEnd.length() : Math.min(startPosition + 50, remark.length());

                    StringBuilder result = new StringBuilder();
                    int actualLength = 0;

                    for (int i = startPosition; i < endPositionFinal && (lengthEnd == null || actualLength < lengthEnd); i++) {
                        char ch = remark.charAt(i);
                        if (!Character.isWhitespace(ch)) {
                            result.append(ch);
                            actualLength++;
                        }
                    }

                    if (endPosition == -1) {
                        return getLimitedStringWithoutSpaces(remark);
                    }

                    return result.toString();
                }

                return getLimitedStringWithoutSpaces(remark);
            } else if (lengthEnd > 0) {
                int startPosition = remark.indexOf(start);
                if (startPosition != -1) {
                    StringBuilder result = new StringBuilder();
                    int actualLength = 0;
                    for (int i = startPosition; i < remark.length() && (actualLength < lengthEnd); i++) {
                        char ch = remark.charAt(i);
                        if (!Character.isWhitespace(ch)) {
                            result.append(ch);
                            actualLength++;
                        }
                    }
                    return result.toString();
                }
                return getLimitedStringWithoutSpaces(remark);
            }

            return getLimitedStringWithoutSpaces(remark);
        } catch (ResourceNotFoundExceptionService e) {
            throw new ResourceNotFoundExceptionService(ResourceNotFoundExceptionEnum.RNF_TRANS, "" + e.getMessage());
        }
    }
    private String removeWhitespace(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    private String getLimitedStringWithoutSpaces(String remark) {
        StringBuilder result = new StringBuilder();
        int count = 0;
        for (char c : remark.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                result.append(c);
                count++;
                if (count >= 50) {
                    break;
                }
            }
        }
        return result.toString();
    }
    private ReconciliationManageEntity convertToEntity(ReconciliationIconnect reconciliationIconnect, List<SystemConfigurationTransactionContentEntity> configs, List<BankAccount> bankAccounts) {
        if (reconciliationIconnect == null) {
            return null;
        }

        ReconciliationManageEntity entity = new ReconciliationManageEntity();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyHHmmss");
        String dateStr = reconciliationIconnect.getTransDate();
        String timeStr = reconciliationIconnect.getTransTime();

        if (dateStr != null && timeStr != null) {
            String combinedDateTime = dateStr + timeStr;
            OffsetDateTime dateTime = OffsetDateTime.parse(combinedDateTime, dateTimeFormatter.withZone(ZoneOffset.UTC));
            entity.setTransactionDate(dateTime);
        }

        entity.setAmount(reconciliationIconnect.getAmount());
        entity.setDorc(reconciliationIconnect.getDorc());
        entity.setContent(reconciliationIconnect.getRemark());
        entity.setTransactionId(reconciliationIconnect.getRefNo());

        if ("D".equals(reconciliationIconnect.getDorc())) {
            entity.setSenderAccount(reconciliationIconnect.getFrAccName());
            entity.setSenderAccountNo(reconciliationIconnect.getFrAccNo());
            entity.setSenderBank(reconciliationIconnect.getFrBankName());
            entity.setSenderCode(reconciliationIconnect.getFrBankCode());
        } else if ("C".equals(reconciliationIconnect.getDorc())) {
            entity.setReceiverAccount(reconciliationIconnect.getFrAccName());
            entity.setReceiverAccountNo(reconciliationIconnect.getFrAccNo());
            entity.setReceiverBank(reconciliationIconnect.getFrBankName());
            entity.setReceiverCode(reconciliationIconnect.getFrBankCode());

            Optional<SystemConfigurationTransactionContentEntity> configOpt = configs.stream()
                    .filter(config -> config.getCustomerId().equals(getCustomerId(reconciliationIconnect, bankAccounts)))
                    .findFirst();

            if (configOpt.isPresent()) {
                SystemConfigurationTransactionContentEntity config = configOpt.get();

                String remark = reconciliationIconnect.getRemark();
                String sourceResult = extractPart(remark, config.getSourceStart(), config.getSourceRegex(), config.getSourceIndexEnd(), config.getSourceLengthEnd());
                String refNoResult = extractPart(remark, config.getRefNoStart(), config.getRefNoRegex(), config.getRefNoIndexEnd(), config.getRefNoLengthEnd());

                entity.setSource(sourceResult);
                entity.setRefNo(refNoResult);
            }
        }



        entity.setStatus(TransactionStatus.AWAITING_RECONCILIATION);
        entity.setSourceInstitution(reconciliationIconnect.getFrom());

        return entity;
    }

    private Long getCustomerId(ReconciliationIconnect reconciliationIconnect, List<BankAccount> bankAccounts) {
        return bankAccounts.stream()
                .filter(account -> account.getAccountNumber().equals(reconciliationIconnect.getFrAccNo()))
                .map(BankAccount::getCustomerId)
                .findFirst()
                .orElse(null);
    }
}