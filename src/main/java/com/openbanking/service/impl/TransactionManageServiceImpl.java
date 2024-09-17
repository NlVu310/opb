package com.openbanking.service.impl;

import com.openbanking.comon.*;
import com.openbanking.entity.*;
import com.openbanking.enums.TransactionStatus;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionEnum;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionService;
import com.openbanking.mapper.BankAccountMapper;
import com.openbanking.mapper.TransactionManageMapper;
import com.openbanking.mapper.TransactionManageReconciliationHistoryMapper;
import com.openbanking.model.bank_account.BankAccount;
import com.openbanking.model.transaction_manage.*;
import com.openbanking.model.transaction_manage_reconciliation_history.TransactionManageReconciliationHistory;
import com.openbanking.repository.*;
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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private SystemConfigurationSourceRepository systemConfigurationSourceRepository;
    @Autowired
    private BankAccountMapper bankAccountMapper;

    public TransactionManageServiceImpl(BaseRepository<TransactionManageEntity, Long> repository, BaseMapper<TransactionManageEntity, TransactionManage, CreateTransactionManage, UpdateTransactionManage> mapper) {
        super(repository, mapper);
    }

    @Override
    public PaginationRS<TransactionManage> getListTransaction(SearchTransactionManageRQ searchRQ) {
        if (searchRQ == null || (searchRQ.getPage() == null && searchRQ.getSize() == null &&
                searchRQ.getSortDirection() == null && searchRQ.getSortBy() == null &&
                searchRQ.getTerm() == null)) {
            List<TransactionManageEntity> entities = transactionManageRepository.findActiveTransactions();

            List<TransactionManage> content = entities.stream()
                    .map(transactionManageMapper::toDTO)
                    .collect(Collectors.toList());

            PaginationRS<TransactionManage> response = new PaginationRS<>();
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

            Page<TransactionManageEntity> transactionManageEntities = transactionManageRepository.searchTransactions(searchRQ, searchRQ.getTransactionDate(), searchRQ.getTerm(), pageable);

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
    }

    @Override
    public TransactionManageDetail getDetailById(Long id) {
        try {
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
            throw new ResourceNotFoundExceptionService(ResourceNotFoundExceptionEnum.RNF_TRANS, "" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void handleIconnectTransactions(List<Iconnect> iconnects) {
        try{
            if (iconnects == null || iconnects.isEmpty()) {
                return;
            }

            List<SystemConfigurationTransactionContentEntity> configs = systemConfigurationTransactionContentRepository.findAll();
            List<BankAccountEntity> bankAccountEntities = bankAccountRepository.findAll();
            List<BankAccount> bankAccounts = bankAccountMapper.toDTOs(bankAccountEntities);

            List<TransactionManageEntity> entities = iconnects.stream()
                    .map(iconnect -> convertToEntity(iconnect, configs, bankAccounts))
                    .collect(Collectors.toList());

            transactionManageRepository.saveAllAndFlush(entities);
        }catch (ResourceNotFoundExceptionService e){
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
    private TransactionManageEntity convertToEntity(Iconnect iconnect, List<SystemConfigurationTransactionContentEntity> configs, List<BankAccount> bankAccounts) {
        if (iconnect == null) {
            return null;
        }

        TransactionManageEntity entity = new TransactionManageEntity();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyHHmmss");
        String dateStr = iconnect.getTransDate();
        String timeStr = iconnect.getTransTime();

        if (dateStr != null && timeStr != null) {
            String combinedDateTime = dateStr + timeStr;
            OffsetDateTime dateTime = OffsetDateTime.parse(combinedDateTime, dateTimeFormatter.withZone(ZoneOffset.UTC));
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

            Optional<SystemConfigurationTransactionContentEntity> configOpt = configs.stream()
                    .filter(config -> config.getCustomerId().equals(getCustomerId(iconnect, bankAccounts)))
                    .findFirst();

            if (configOpt.isPresent()) {
                SystemConfigurationTransactionContentEntity config = configOpt.get();

                String remark = iconnect.getRemark();
                String sourceResult = extractPart(remark, config.getSourceStart(), config.getSourceRegex(), config.getSourceIndexEnd(), config.getSourceLengthEnd());
                String refNoResult = extractPart(remark, config.getRefNoStart(), config.getRefNoRegex(), config.getRefNoIndexEnd(), config.getRefNoLengthEnd());

                entity.setSource(sourceResult);
                entity.setRefNo(refNoResult);
            }
        }



        entity.setStatus(TransactionStatus.AWAITING_RECONCILIATION);
        entity.setSourceInstitution(iconnect.getFrom());

        return entity;
    }

    private Long getCustomerId(Iconnect iconnect, List<BankAccount> bankAccounts) {
        return bankAccounts.stream()
                .filter(account -> account.getAccountNumber().equals(iconnect.getFrAccNo()))
                .map(BankAccount::getCustomerId)
                .findFirst()
                .orElse(null);
    }
}