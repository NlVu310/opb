package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.*;
import com.openbanking.enums.ReconciliationHistoryResult;
import com.openbanking.enums.TransactionStatus;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundException;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionEnum;
import com.openbanking.mapper.TransactionManageMapper;
import com.openbanking.mapper.TransactionManageReconciliationHistoryMapper;
import com.openbanking.model.reconciliation_manage.*;
import com.openbanking.repository.*;
import com.openbanking.service.ReconciliationManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReconciliationManageServiceImpl extends BaseServiceImpl<ReconciliationManageEntity, ReconciliationManage,CreateReconciliationManage, UpdateReconciliationManage, Long> implements ReconciliationManageService {
    @Autowired
    private ReconciliationManageRepository reconciliationManageRepository;
    @Autowired
    private TransactionManageMapper transactionManageMapper;
    @Autowired
    private TransactionManageReconciliationHistoryMapper transactionManageReconciliationHistoryMapper;
    @Autowired
    private SystemConfigurationAutoReconciliationRepository systemConfigurationAutoReconciliationRepository;
    @Autowired
    private SystemConfigurationSourceRepository systemConfigurationSourceRepository;
    @Autowired
    private TransactionManageRepository transactionManageRepository;
    @Autowired
    private TransactionManageReconciliationHistoryRepository transactionManageReconciliationHistoryRepository;
    @Autowired
    private AwaitingReconciliationTransactionRepository awaitingReconciliationTransactionRepository;
    @Autowired
    private JobSchedulerRepository jobSchedulerRepository;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public ReconciliationManageServiceImpl(BaseRepository<ReconciliationManageEntity, Long> repository, BaseMapper<ReconciliationManageEntity, ReconciliationManage, CreateReconciliationManage, UpdateReconciliationManage> mapper) {
        super(repository, mapper);
    }

//     matchingTransaction.setStatus(TransactionStatus.RECONCILIATION_FAILED);
//                        reconciliation.setStatus(TransactionStatus.RECONCILIATION_FAILED);
//                        transactionManage.setStatus(TransactionStatus.RECONCILIATION_FAILED);
//                        TransactionManageReconciliationHistoryEntity errorHistory = new TransactionManageReconciliationHistoryEntity();
//                        errorHistory.setTransactionId(matchingTransaction.getTransactionId());
//                        errorHistory.setReconciliationManageId(reconciliation.getId());
//                        errorHistory.setTransactionManageId(matchingTransaction.getTransactionManageId());
//                        errorHistory.setReconciliationDate(OffsetDateTime.now());
//                        errorHistory.setReconciliationSource(matchingTransaction.getSourceInstitution());
//                        errorHistory.setReconciliationResult("ERROR: " + e.getMessage());
//                        histories.add(errorHistory);

    @Override
    @Transactional
    public void handleIconnectReconciliations(List<ReconciliationIconnect> iconnects) {
        try {
            if (iconnects == null || iconnects.isEmpty()) {
                return;
            }
            List<ReconciliationManageEntity> entities = iconnects.stream()
                    .map(this::convertToEntity)
                    .collect(Collectors.toList());

            reconciliationManageRepository.saveAllAndFlush(entities);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_TRANS, "" + e.getMessage());
        }

    }

    private ReconciliationManageEntity convertToEntity(ReconciliationIconnect iconnect) {
        if (iconnect == null) {
            return null;
        }

        ReconciliationManageEntity entity = new ReconciliationManageEntity();
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
        }
        entity.setStatus(TransactionStatus.AWAITING_RECONCILIATION);
        entity.setSourceInstitution(iconnect.getFrom());

        return entity;
    }

    public void performReconciliation() {
        List<SystemConfigurationAutoReconciliationEntity> configs = systemConfigurationAutoReconciliationRepository.findAll();
        LocalTime currentTime = LocalTime.now();

        for (SystemConfigurationAutoReconciliationEntity config : configs) {
            LocalTime reconciliationTime = config.getReconciliationTime();
            LocalDate lastReconciliationDate = getLastReconciliationDate(config);
            LocalDate nextReconciliationDate = calculateNextReconciliationDate(lastReconciliationDate, config);

            if (LocalDate.now().isAfter(nextReconciliationDate)) {
                if (currentTime.getHour() == reconciliationTime.getHour() && currentTime.getMinute() == reconciliationTime.getMinute()) {
                    try {
                        executorService.submit(() -> {
                            try {
                                handleReconciliation(config);
                            } catch (Exception e) {
                            }
                        });
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }
        }
    }

    private void handleReconciliation(SystemConfigurationAutoReconciliationEntity config) {
        int attempts = 0;
        boolean success = false;

        while (attempts < config.getRetryFrequencyNumber() && !success) {
            success = performReconciliationForSource(config);
            attempts++;

            if (!success && attempts < config.getRetryFrequencyNumber()) {
                executorService.submit(() -> {
                    try {
                        Thread.sleep(config.getRetryTimeNumber() * 60 * 1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    handleReconciliation(config);
                });
                return;
            }
        }
    }

    private LocalDate getLastReconciliationDate(SystemConfigurationAutoReconciliationEntity config) {
        List<AwaitingReconciliationTransactionEntity> transactions = awaitingReconciliationTransactionRepository.findBySource(config.getSourceCode());

        if (transactions.isEmpty()) {
            return LocalDate.now();
        }

        LocalDate lastReconciliationDate = LocalDate.MIN;

        for (AwaitingReconciliationTransactionEntity transaction : transactions) {
            LocalDate transactionDate = transaction.getTransactionDate().toLocalDate();
            if (transactionDate.isAfter(lastReconciliationDate)) {
                lastReconciliationDate = transactionDate;
            }
        }

        return lastReconciliationDate;
    }

    private boolean performReconciliationForSource(SystemConfigurationAutoReconciliationEntity config) {
        List<AwaitingReconciliationTransactionEntity> awaitingTransactions = awaitingReconciliationTransactionRepository.findBySource(config.getSourceCode());
        List<ReconciliationManageEntity> reconciliations = reconciliationManageRepository.findBySourceInstitution(config.getSourceCode());

        LocalDate thresholdDate = LocalDate.now().minusDays(config.getReconciliationDay());
        List<AwaitingReconciliationTransactionEntity> filteredAwaitingTransactions = awaitingTransactions.stream()
                .filter(transaction -> transaction.getTransactionDate().toLocalDate().isBefore(thresholdDate))
                .collect(Collectors.toList());

        Map<String, AwaitingReconciliationTransactionEntity> transactionMap = new HashMap<>();
        for (AwaitingReconciliationTransactionEntity transaction : filteredAwaitingTransactions) {
            String key = transaction.getTransactionId() + "|" + transaction.getSourceInstitution();
            transactionMap.put(key, transaction);
        }

        List<TransactionManageReconciliationHistoryEntity> histories = new ArrayList<>();
        boolean matchedAnyTransaction = false;

        for (ReconciliationManageEntity reconciliation : reconciliations) {
            String key = reconciliation.getTransactionId() + "|" + reconciliation.getSourceInstitution();

            if (transactionMap.containsKey(key)) {
                matchedAnyTransaction = true;
                AwaitingReconciliationTransactionEntity matchingTransaction = transactionMap.get(key);
                Long transactionManageId = matchingTransaction.getTransactionManageId();
                TransactionManageEntity transactionManage = transactionManageRepository.findById(transactionManageId)
                        .orElseThrow(() -> new RuntimeException("Transaction not found"));

                boolean allFieldsMatch = false;
                int attempts = 0;

                try {
                    while (attempts < config.getRetryFrequencyNumber() && !allFieldsMatch) {
                        allFieldsMatch =
                                Objects.equals(matchingTransaction.getAmount(), reconciliation.getAmount()) &&
                                        Objects.equals(matchingTransaction.getContent(), reconciliation.getContent()) &&
                                        Objects.equals(matchingTransaction.getSenderAccount(), reconciliation.getSenderAccount()) &&
                                        Objects.equals(matchingTransaction.getSenderAccountNo(), reconciliation.getSenderAccountNo()) &&
                                        Objects.equals(matchingTransaction.getSenderBank(), reconciliation.getSenderBank()) &&
                                        Objects.equals(matchingTransaction.getSenderCode(), reconciliation.getSenderCode()) &&
                                        Objects.equals(matchingTransaction.getReceiverAccount(), reconciliation.getReceiverAccount()) &&
                                        Objects.equals(matchingTransaction.getReceiverAccountNo(), reconciliation.getReceiverAccountNo()) &&
                                        Objects.equals(matchingTransaction.getReceiverBank(), reconciliation.getReceiverBank()) &&
                                        Objects.equals(matchingTransaction.getReceiverCode(), reconciliation.getReceiverCode());
                        TransactionManageReconciliationHistoryEntity history = createReconciliationHistory(matchingTransaction, reconciliation, allFieldsMatch);
                        histories.add(history);

                        if (!allFieldsMatch) {
//                            long delay = calculateDelay(config.getReconciliationFrequencyUnit(), config.getReconciliationFrequencyNumber());
//                            executorService.submit(() -> {
//                                try {
//                                    Thread.sleep(delay);
//                                    performReconciliationForSource(config);
//                                } catch (InterruptedException e) {
//                                    Thread.currentThread().interrupt();
//                                }
//                            });
                            matchingTransaction.setStatus(TransactionStatus.COMPLETED_RECONCILIATION);
                            reconciliation.setStatus(TransactionStatus.COMPLETED_RECONCILIATION);
                            transactionManage.setStatus(TransactionStatus.COMPLETED_RECONCILIATION);
                            awaitingReconciliationTransactionRepository.save(matchingTransaction);
                        }

                        if (allFieldsMatch) {
                            matchingTransaction.setStatus(TransactionStatus.COMPLETED_RECONCILIATION);
                            reconciliation.setStatus(TransactionStatus.COMPLETED_RECONCILIATION);
                            transactionManage.setStatus(TransactionStatus.COMPLETED_RECONCILIATION);
                            Long transactionId = matchingTransaction.getId();
                            awaitingReconciliationTransactionRepository.deleteById(transactionId);
                        }
                    }
                } catch (Exception e) {
                    transactionManage.setStatus(TransactionStatus.RECONCILING);
                    TransactionManageReconciliationHistoryEntity history = new TransactionManageReconciliationHistoryEntity();
                    history.setTransactionId(matchingTransaction.getTransactionId());
                    history.setReconciliationManageId(reconciliation.getId());
                    history.setTransactionManageId(matchingTransaction.getTransactionManageId());
                    history.setReconciliationDate(OffsetDateTime.now());
                    history.setReconciliationSource(matchingTransaction.getSourceInstitution());
//                    history.setReconciliationResult("Đối soát thất bại");
                } finally {
                    transactionManageRepository.save(transactionManage);
                }
            }
        }
        if (!matchedAnyTransaction) {
            return false;
        }
        transactionManageReconciliationHistoryRepository.saveAll(histories);
        return true;
    }

    private LocalDate calculateNextReconciliationDate(LocalDate lastReconciliationDate, SystemConfigurationAutoReconciliationEntity config) {
        switch (config.getReconciliationFrequencyUnit()) {
            case DAILY:
                return lastReconciliationDate.plusDays(config.getReconciliationFrequencyNumber());
            case WEEKLY:
                return lastReconciliationDate.plusWeeks(config.getReconciliationFrequencyNumber());
            case MONTHLY:
                return lastReconciliationDate.plusMonths(config.getReconciliationFrequencyNumber());
            case QUARTERLY:
                return lastReconciliationDate.plusMonths((long) config.getReconciliationFrequencyNumber() * 3);
            default:
                throw new IllegalArgumentException("Tần suất không hợp lệ");
        }
    }

//    private long calculateDelay(ReconciliationFrequencyUnit unit, int number) {
//        switch (unit) {
//            case DAILY:
//                return TimeUnit.DAYS.toMillis(number);
//            case WEEKLY:
//                return TimeUnit.DAYS.toMillis((long) number * 7);
//            case MONTHLY:
//                return TimeUnit.DAYS.toMillis((long) number * 30);
//            case QUARTERLY:
//                return TimeUnit.DAYS.toMillis((long) number * 90);
//            default:
//                throw new IllegalArgumentException("Đơn vị không hợp lệ");
//        }
//    }

    private TransactionManageReconciliationHistoryEntity createReconciliationHistory(
            AwaitingReconciliationTransactionEntity matchingTransaction,
            ReconciliationManageEntity reconciliation,
            boolean allFieldsMatch) {

        TransactionManageReconciliationHistoryEntity history = new TransactionManageReconciliationHistoryEntity();
        history.setTransactionId(matchingTransaction.getTransactionId());
        history.setReconciliationManageId(reconciliation.getId());
        history.setTransactionManageId(matchingTransaction.getTransactionManageId());
        history.setReconciliationDate(OffsetDateTime.now());
        history.setReconciliationSource(matchingTransaction.getSourceInstitution());
//        history.setReconciliationResult((allFieldsMatch ? "SUCCESS" : "FAILURE"));

        return history;
    }


    @Override
    public void performReconciliation(ReconciliationManageRequest rq, Long accountId) {
            List<SystemConfigurationSourceEntity> sources = systemConfigurationSourceRepository.getListSourceByPartnerId(rq.getPartnerId());

            List<AwaitingReconciliationTransactionEntity> awaitingTransactions = awaitingReconciliationTransactionRepository.findBySourceInstitutionInAndTransactionDateBetween(
                    sources.stream().map(SystemConfigurationSourceEntity::getCode).collect(Collectors.toList()),
                    rq.getFromDate(),
                    rq.getToDate()
            );

            List<ReconciliationManageEntity> reconciliationTransactions = reconciliationManageRepository.findBySourceInstitutionInAndTransactionDateBetween(
                    sources.stream().map(SystemConfigurationSourceEntity::getCode).collect(Collectors.toList()),
                    rq.getFromDate(),
                    rq.getToDate()
            );

            List<TransactionManageReconciliationHistoryEntity> histories = new ArrayList<>();

            Set<Long> transactionManageIds = awaitingTransactions.stream()
                    .map(AwaitingReconciliationTransactionEntity::getTransactionManageId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<Long, TransactionManageEntity> transactionManageMap = transactionManageRepository.findAllById(transactionManageIds)
                    .stream()
                    .collect(Collectors.toMap(TransactionManageEntity::getId, Function.identity()));

            for (AwaitingReconciliationTransactionEntity matchingTransaction : awaitingTransactions) {
                boolean allFieldsMatch = false;
                try {
                    for (ReconciliationManageEntity reconciliation : reconciliationTransactions) {
                        boolean fieldsMatch =
                                        Objects.equals(matchingTransaction.getAmount(), reconciliation.getAmount()) &&
                                        Objects.equals(matchingTransaction.getContent(), reconciliation.getContent()) &&
                                        Objects.equals(matchingTransaction.getSenderAccount(), reconciliation.getSenderAccount()) &&
                                        Objects.equals(matchingTransaction.getSenderAccountNo(), reconciliation.getSenderAccountNo()) &&
                                        Objects.equals(matchingTransaction.getSenderBank(), reconciliation.getSenderBank()) &&
                                        Objects.equals(matchingTransaction.getSenderCode(), reconciliation.getSenderCode()) &&
                                        Objects.equals(matchingTransaction.getReceiverAccount(), reconciliation.getReceiverAccount()) &&
                                        Objects.equals(matchingTransaction.getReceiverAccountNo(), reconciliation.getReceiverAccountNo()) &&
                                        Objects.equals(matchingTransaction.getReceiverBank(), reconciliation.getReceiverBank()) &&
                                        Objects.equals(matchingTransaction.getReceiverCode(), reconciliation.getReceiverCode());
                        if (fieldsMatch) {
                            allFieldsMatch = true;
                            awaitingReconciliationTransactionRepository.delete(matchingTransaction);
                            reconciliationManageRepository.delete(reconciliation);
                        }
                    }

                    TransactionManageReconciliationHistoryEntity history = new TransactionManageReconciliationHistoryEntity();
                    history.setTransactionId(matchingTransaction.getTransactionId());
                    history.setTransactionManageId(matchingTransaction.getTransactionManageId());
                    history.setReconciliationDate(OffsetDateTime.now());
                    history.setReconciliationSource(matchingTransaction.getSourceInstitution());
                    history.setReconciliationResult(allFieldsMatch ? ReconciliationHistoryResult.MATCHED_RECONCILIATION : ReconciliationHistoryResult.UNMATCHED_RECONCILIATION);
                    history.setCreatedBy(accountId);
                    histories.add(history);

                    if (matchingTransaction.getTransactionManageId() != null) {
                        TransactionManageEntity transactionManage = transactionManageMap.get(matchingTransaction.getTransactionManageId());
                        if (transactionManage != null) {
                            transactionManage.setStatus(TransactionStatus.COMPLETED_RECONCILIATION);
                            transactionManageRepository.save(transactionManage);
                        }
                    }
                    matchingTransaction.setStatus(TransactionStatus.COMPLETED_RECONCILIATION);
                } catch (Exception e) {
                    if (matchingTransaction.getTransactionManageId() != null) {
                        TransactionManageEntity transactionManage = transactionManageMap.get(matchingTransaction.getTransactionManageId());
                        if (transactionManage != null) {
                            transactionManage.setStatus(TransactionStatus.FAILED_RECONCILIATION);
                            transactionManageRepository.save(transactionManage);
                        }
                    }
                    matchingTransaction.setStatus(TransactionStatus.AWAITING_RECONCILIATION);
                    TransactionManageReconciliationHistoryEntity history = new TransactionManageReconciliationHistoryEntity();
                    history.setTransactionId(matchingTransaction.getTransactionId());
                    history.setTransactionManageId(matchingTransaction.getTransactionManageId());
                    history.setReconciliationDate(OffsetDateTime.now());
                    history.setReconciliationSource(matchingTransaction.getSourceInstitution());
                    history.setReconciliationResult(ReconciliationHistoryResult.FAILED_RECONCILIATION);
                    history.setCreatedBy(accountId);
                    histories.add(history);
                }
            }

            transactionManageReconciliationHistoryRepository.saveAll(histories);
        }
}