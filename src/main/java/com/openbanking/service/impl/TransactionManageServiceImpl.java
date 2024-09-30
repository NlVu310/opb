package com.openbanking.service.impl;

import com.openbanking.comon.*;
import com.openbanking.entity.*;
import com.openbanking.enums.TransactionStatus;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionEnum;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundException;
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
import java.util.*;
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
    private AwaitingReconciliationTransactionRepository awaitingReconciliationTransactionRepository;
    @Autowired
    private ReconciliationManageRepository reconciliationManageRepository;
    @Autowired
    private AccountRepository accountRepository;
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
    }

    @Override
    public PaginationRS<TransactionManage> getListTransactionRecon(SearchTransactionManageRQ searchRQ) {
        if (searchRQ == null || (searchRQ.getPage() == null && searchRQ.getSize() == null &&
                searchRQ.getSortDirection() == null && searchRQ.getSortBy() == null &&
                searchRQ.getTerm() == null)) {

            List<TransactionManageEntity> entities = transactionManageRepository.findActiveReconciliations();
            List<TransactionManageReconciliationHistoryEntity> reconciliationHistories = transactionManageReconciliationHistoryRepository.findLatestForEachTransactionManageId();

            Set<Long> createdByIds = reconciliationHistories.stream()
                    .map(TransactionManageReconciliationHistoryEntity::getCreatedBy)
                    .filter(Objects::nonNull)
                    .filter(id -> id != 0)
                    .collect(Collectors.toSet());

            Map<Long, String> accountNames = accountRepository.findAllById(createdByIds)
                    .stream()
                    .collect(Collectors.toMap(AccountEntity::getId, AccountEntity::getName));

            List<TransactionManage> content = entities.stream()
                    .map(entity -> {
                        TransactionManage dto = transactionManageMapper.toDTO(entity);
                        List<TransactionManageReconciliationHistory> histories = reconciliationHistories.stream()
                                .filter(historyEntity -> historyEntity.getTransactionManageId() != null && dto.getId() != null && dto.getId().equals(historyEntity.getTransactionManageId()))
                                .map(historyEntity -> {
                                    TransactionManageReconciliationHistory historyDto = transactionManageReconciliationHistoryMapper.toDTO(historyEntity);
                                    if (historyEntity.getCreatedBy() == null || historyEntity.getCreatedBy() == 0) {
                                        historyDto.setReconciler("AUTO");
                                    } else {
                                        historyDto.setReconciler(accountNames.get(historyEntity.getCreatedBy()));
                                    }
                                    return historyDto;
                                })
                                .collect(Collectors.toList());

                        dto.setTransactionManageReconciliationHistories(histories);
                        return dto;
                    })
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

            List<TransactionManageReconciliationHistoryEntity> reconciliationHistories = transactionManageReconciliationHistoryRepository.findLatestForEachTransactionManageId();
            Page<TransactionManageEntity> transactionManageEntities = transactionManageRepository.searchTransactionRecons(
                    searchRQ, searchRQ.getTransactionDate(), searchRQ.getReconciliationDate(), searchRQ.getTerm(), pageable);

            Set<Long> createdByIds = reconciliationHistories.stream()
                    .map(TransactionManageReconciliationHistoryEntity::getCreatedBy)
                    .filter(Objects::nonNull)
                    .filter(id -> id != 0)
                    .collect(Collectors.toSet());

            Map<Long, String> accountNames = accountRepository.findAllById(createdByIds)
                    .stream()
                    .collect(Collectors.toMap(AccountEntity::getId, AccountEntity::getName));

            List<TransactionManage> transactionManages = transactionManageEntities.getContent()
                    .stream()
                    .map(entity -> {
                        TransactionManage dto = transactionManageMapper.toDTO(entity);
                        List<TransactionManageReconciliationHistory> histories = reconciliationHistories.stream()
                                .filter(historyEntity -> historyEntity.getTransactionManageId() != null && dto.getId() != null && dto.getId().equals(historyEntity.getTransactionManageId()))
                                .map(historyEntity -> {
                                    TransactionManageReconciliationHistory historyDto = transactionManageReconciliationHistoryMapper.toDTO(historyEntity);

                                    if (historyEntity.getCreatedBy() == null || historyEntity.getCreatedBy() == 0) {
                                        historyDto.setReconciler("AUTO");
                                    } else {
                                        historyDto.setReconciler(accountNames.get(historyEntity.getCreatedBy()));
                                    }
                                    return historyDto;
                                })
                                .collect(Collectors.toList());

                        dto.setTransactionManageReconciliationHistories(histories);
                        return dto;
                    })
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
                    .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_TRANS, "with id " + id));

            List<TransactionManageReconciliationHistoryEntity> entities =
                    transactionManageReconciliationHistoryRepository.findLatestByTransactionManageIdAndCreatedBy(id);
            TransactionManageDetail transactionManageDetail = transactionManageMapper.getDetail(transactionManageEntity);

            Set<Long> createdByIds = entities.stream()
                    .map(TransactionManageReconciliationHistoryEntity::getCreatedBy)
                    .filter(idValue -> idValue != 0)
                    .collect(Collectors.toSet());

            Map<Long, String> accountNames = accountRepository.findAllById(createdByIds)
                    .stream()
                    .collect(Collectors.toMap(AccountEntity::getId, AccountEntity::getName));

            List<TransactionManageReconciliationHistory> transactionManageReconciliationHistories = entities.stream()
                    .map(historyEntity -> {
                        TransactionManageReconciliationHistory dto = transactionManageReconciliationHistoryMapper.toDTO(historyEntity);
                        if (historyEntity.getCreatedBy() == 0) {
                            dto.setReconciler("auto");
                        } else {
                            dto.setReconciler(accountNames.get(historyEntity.getCreatedBy()));
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());

            transactionManageDetail.setTransactionManageReconciliationHistories(transactionManageReconciliationHistories);

            return transactionManageDetail;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_TRANS, "" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void handleIconnectTransactions(List<Iconnect> iconnects) {
        if (iconnects == null || iconnects.isEmpty()) {
            return;
        }

        List<SystemConfigurationTransactionContentEntity> configs = systemConfigurationTransactionContentRepository.findAll();
        List<BankAccountEntity> bankAccountEntities = bankAccountRepository.findAll();
        List<BankAccount> bankAccounts = bankAccountMapper.toDTOs(bankAccountEntities);

        List<TransactionManageEntity> entities = iconnects.stream()
                .map(iconnect -> convertToEntity(iconnect, configs, bankAccounts))
                .collect(Collectors.toList());

        Map<String, AwaitingReconciliationTransactionEntity> awaitingMap = awaitingReconciliationTransactionRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        entity -> entity.getSourceInstitution() + "_" + entity.getTransactionId(),
                        entity -> entity));

        Map<String, ReconciliationManageEntity> reconciliationMap = reconciliationManageRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        entity -> entity.getSourceInstitution() + "_" + entity.getTransactionId(),
                        entity -> entity));

        List<AwaitingReconciliationTransactionEntity> toDeleteAwaiting = new ArrayList<>();
        List<ReconciliationManageEntity> toDeleteReconciliation = new ArrayList<>();

        for (TransactionManageEntity entity : entities) {
            String key = entity.getSourceInstitution() + "_" + entity.getTransactionId();

            boolean existsInAwaiting = awaitingMap.containsKey(key);
            boolean existsInReconciliation = reconciliationMap.containsKey(key);

            if (existsInAwaiting) {
                toDeleteAwaiting.add(awaitingMap.get(key));
            }

            if (existsInReconciliation) {
                toDeleteReconciliation.add(reconciliationMap.get(key));
            }

            if (existsInAwaiting && existsInReconciliation) {
                entity.setStatus(TransactionStatus.COMPLETED_RECONCILIATION);
            } else {
                entity.setStatus(TransactionStatus.AWAITING_RECONCILIATION);
            }
        }

        awaitingReconciliationTransactionRepository.deleteAll(toDeleteAwaiting);
        reconciliationManageRepository.deleteAll(toDeleteReconciliation);
        transactionManageRepository.saveAll(entities);

        for (TransactionManageEntity entity : entities) {
            String key = entity.getSourceInstitution() + "_" + entity.getTransactionId();
            if (!awaitingMap.containsKey(key)) {
                AwaitingReconciliationTransactionEntity awaitingEntity = convertAwaitingReconciliationEntity(entity);
                awaitingReconciliationTransactionRepository.save(awaitingEntity);
            }
        }
    }



    private AwaitingReconciliationTransactionEntity convertAwaitingReconciliationEntity(TransactionManageEntity entity) {
        AwaitingReconciliationTransactionEntity awaitingEntity = new AwaitingReconciliationTransactionEntity();
        awaitingEntity.setTransactionDate(entity.getTransactionDate());
        awaitingEntity.setAmount(entity.getAmount());
        awaitingEntity.setDorc(entity.getDorc());
        awaitingEntity.setContent(entity.getContent());
        awaitingEntity.setSource(entity.getSource());
        awaitingEntity.setRefNo(entity.getRefNo());
        awaitingEntity.setSenderAccount(entity.getSenderAccount());
        awaitingEntity.setSenderAccountNo(entity.getSenderAccountNo());
        awaitingEntity.setSenderBank(entity.getSenderBank());
        awaitingEntity.setSenderCode(entity.getSenderCode());
        awaitingEntity.setReceiverAccount(entity.getReceiverAccount());
        awaitingEntity.setReceiverAccountNo(entity.getReceiverAccountNo());
        awaitingEntity.setReceiverBank(entity.getReceiverBank());
        awaitingEntity.setReceiverCode(entity.getReceiverCode());
        awaitingEntity.setSourceInstitution(entity.getSourceInstitution());
        awaitingEntity.setTransactionId(entity.getTransactionId());
        awaitingEntity.setTransactionManageId(entity.getId());
        awaitingEntity.setStatus(entity.getStatus());
        return awaitingEntity;
    }


    private String extractPart(String remark, String start, String regex, String indexEnd, Long lengthEnd) {
        try {
            if (remark == null) {
                return null;
            }

            if (regex == null && indexEnd == null && lengthEnd == null) {
                throw new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_TRANS_CONT,
                        "At least one of 'regex', 'indexEnd', or 'lengthEnd' must be provided.");
            }

            if (regex != null && start == null) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(remark);

                if (matcher.find()) {
                    String matchedGroup = matcher.group(0);
                    return removeWhitespace(matchedGroup);
                }
                return getLimitedStringWithoutSpaces(remark);
            } else if (indexEnd != null) {
                int startPosition = findCaseInsensitiveIndex(remark, start);
                int endPosition = findCaseInsensitiveIndex(remark, indexEnd);

                if (startPosition == -1) {
                    if (endPosition != -1) {
                        return getLimitedStringFromPositionReverse(remark, endPosition + indexEnd.length());//
                    } else {
                        return getLimitedStringWithoutSpaces(remark);//
                    }
                }
                while (endPosition != -1 && endPosition < startPosition) {
                    endPosition = findCaseInsensitiveIndex(remark, indexEnd, endPosition + 1);
                }
                if (endPosition != -1) {
                    if (start.equalsIgnoreCase(indexEnd)) {
                        int nextEndPosition = findCaseInsensitiveIndex(remark, indexEnd, startPosition + 1);
                        if (nextEndPosition != -1) {
                            return removeWhitespace(remark.substring(startPosition, nextEndPosition + indexEnd.length()));//
                        }
                    }
                    return removeWhitespace(remark.substring(startPosition, endPosition + indexEnd.length()));//
                } else {
                    return getLimitedStringFromPosition(remark, startPosition, 50);//
                }
            } else if (lengthEnd != null && lengthEnd > 0) {
                int startPosition = findCaseInsensitiveIndex(remark, start);
                if (startPosition != -1) {
                    return getLimitedStringFromPosition(remark, startPosition, lengthEnd.intValue());
                }
                return getLimitedStringWithoutSpaces(remark);
            }

            return getLimitedStringWithoutSpaces(remark);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_TRANS, e.getMessage());
        }
    }

    private int findCaseInsensitiveIndex(String str, String target) {
        return findCaseInsensitiveIndex(str, target, 0);
    }

    private int findCaseInsensitiveIndex(String str, String target, int fromIndex) {
        for (int i = fromIndex; i <= str.length() - target.length(); i++) {
            if (str.substring(i, i + target.length()).equalsIgnoreCase(target)) {
                return i;
            }
        }
        return -1;
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

    private String getLimitedStringFromPosition(String remark, int startPosition, int limit) {
        StringBuilder result = new StringBuilder();
        int actualLength = 0;
        for (int i = startPosition; i < remark.length() && actualLength < limit; i++) {
            char ch = remark.charAt(i);
            if (!Character.isWhitespace(ch)) {
                result.append(ch);
                actualLength++;
            }
        }
        return result.toString();
    }
    private String getLimitedStringFromPositionReverse(String remark, int endPosition) {
        StringBuilder result = new StringBuilder();
        int actualLength = 0;
        for (int i = endPosition - 1; i >= 0 && actualLength < 50; i--) {
            char ch = remark.charAt(i);
            if (!Character.isWhitespace(ch)) {
                result.insert(0, ch);
                actualLength++;
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
            entity.setReceiverAccountNo(iconnect.getAccountNo());
            entity.setSenderAccount(iconnect.getFrAccName());
            entity.setSenderAccountNo(iconnect.getFrAccNo());
            entity.setSenderBank(iconnect.getFrBankName());
            entity.setSenderCode(iconnect.getFrBankCode());
        } else if ("C".equals(iconnect.getDorc())) {
            entity.setSenderAccountNo(iconnect.getAccountNo());
            entity.setReceiverAccount(iconnect.getFrAccName());
            entity.setReceiverAccountNo(iconnect.getFrAccNo());
            entity.setReceiverBank(iconnect.getFrBankName());
            entity.setReceiverCode(iconnect.getFrBankCode());

            Optional<SystemConfigurationTransactionContentEntity> configOpt = configs.stream()
                    .filter(config -> config.getCustomerId().equals(getIconnectCustomerId(iconnect, bankAccounts)))
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
        entity.setSourceInstitution("BIDV_ICN");

        return entity;
    }

    private Long getIconnectCustomerId(Iconnect iconnect, List<BankAccount> bankAccounts) {
        return bankAccounts.stream()
                .filter(account -> account.getAccountNumber().equals(iconnect.getFrAccNo()))
                .map(BankAccount::getCustomerId)
                .findFirst()
                .orElse(null);
    }
    private Long getDebtClearanceCustomerId(DebtClearance debtClearance, List<BankAccount> bankAccounts) {
        return bankAccounts.stream()
                .filter(account -> account.getAccountNumber().equals(debtClearance.getAccountNumber()))
                .map(BankAccount::getCustomerId)
                .findFirst()
                .orElse(null);
    }


    @Override
    @Transactional
    public void handleDebtClearanceTransaction(DebtClearance debtClearance) {
        try {
            if (debtClearance == null) {
                return;
            }

            List<SystemConfigurationTransactionContentEntity> configs = systemConfigurationTransactionContentRepository.findAll();
            List<BankAccountEntity> bankAccountEntities = bankAccountRepository.findAll();
            List<BankAccount> bankAccounts = bankAccountMapper.toDTOs(bankAccountEntities);

            TransactionManageEntity entity = convertDebtClearanceToEntity(debtClearance, configs, bankAccounts);

            transactionManageRepository.save(entity);

            AwaitingReconciliationTransactionEntity awaitingEntity = convertAwaitingReconciliationEntity(
                    entity);

            awaitingReconciliationTransactionRepository.save(awaitingEntity);

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_TRANS, "" + e.getMessage());
        }
    }

    private TransactionManageEntity convertDebtClearanceToEntity(DebtClearance debtClearance, List<SystemConfigurationTransactionContentEntity> configs, List<BankAccount> bankAccounts) {
        TransactionManageEntity entity = new TransactionManageEntity();

        String dateStr = debtClearance.getTransDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyHHmmss");
        OffsetDateTime transactionDate = OffsetDateTime.parse(dateStr, dateTimeFormatter.withZone(ZoneOffset.UTC));
        entity.setTransactionDate(transactionDate);
        entity.setDorc("C");
        entity.setTransactionId(debtClearance.getTransId());
        entity.setAmount(String.valueOf(debtClearance.getAmount()));
        entity.setReceiverAccountNo(debtClearance.getAccountNumber());
        entity.setReceiverAccount(debtClearance.getAccountName());
        entity.setContent(debtClearance.getDescription());
        entity.setSourceInstitution("BIDV_TCH");
        entity.setStatus(TransactionStatus.AWAITING_RECONCILIATION);

        Optional<SystemConfigurationTransactionContentEntity> configOpt = configs.stream()
                .filter(config -> config.getCustomerId().equals(getDebtClearanceCustomerId(debtClearance, bankAccounts)))
                .findFirst();

        if (configOpt.isPresent()) {
            SystemConfigurationTransactionContentEntity config = configOpt.get();
            String remark = debtClearance.getDescription();
            String sourceResult = extractPart(remark, config.getSourceStart(), config.getSourceRegex(), config.getSourceIndexEnd(), config.getSourceLengthEnd());
            String refNoResult = extractPart(remark, config.getRefNoStart(), config.getRefNoRegex(), config.getRefNoIndexEnd(), config.getRefNoLengthEnd());

            entity.setSource(sourceResult);
            entity.setRefNo(refNoResult);
        }

        return entity;
    }

}