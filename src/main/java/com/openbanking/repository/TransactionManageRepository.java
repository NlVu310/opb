package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.TransactionManageEntity;
import com.openbanking.model.transaction_manage.SearchTransactionManageRQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionManageRepository extends BaseRepository<TransactionManageEntity, Long> {
    @Query("SELECT t " +
            "FROM TransactionManageEntity t " +
            "JOIN BankAccountEntity b ON (b.accountNumber = t.receiverAccountNo OR b.accountNumber = t.senderAccountNo) " +
            "JOIN CustomerEntity c ON b.customerId = c.id " +
            "JOIN PartnerEntity p ON b.partnerId = p.id " +
            "JOIN SystemConfigurationSourceEntity s ON b.sourceId = s.id " +
            "WHERE b.status = com.openbanking.enums.BankAccountStatus.ACTIVE " +
            "AND c.status = com.openbanking.enums.CustomerStatus.ACTIVE " +
            "AND p.status = com.openbanking.enums.PartnerStatus.ACTIVE " +
            "AND s.status = com.openbanking.enums.SourceConfigStatus.CONNECTED " +
            "AND p.deletedAt IS NULL " +
            "AND s.deletedAt IS NULL " +
            "AND t.deletedAt IS NULL " +
            "AND b.sourceCode = t.sourceInstitution " +
            "AND (:#{#searchRQ.amount} IS NULL OR t.amount = :#{#searchRQ.amount}) " +
            "AND (:#{#searchRQ.transactionId} IS NULL OR t.transactionId = :#{#searchRQ.transactionId}) " +
            "AND (:#{#searchRQ.content} IS NULL OR t.content = :#{#searchRQ.content}) " +
            "AND (:#{#searchRQ.source} IS NULL OR t.source = :#{#searchRQ.source}) " +
            "AND (:#{#searchRQ.refNo} IS NULL OR t.refNo = :#{#searchRQ.refNo}) " +
            "AND (:#{#searchRQ.senderAccount} IS NULL OR t.senderAccount = :#{#searchRQ.senderAccount}) " +
            "AND (:#{#searchRQ.senderAccountNo} IS NULL OR t.senderAccountNo = :#{#searchRQ.senderAccountNo}) " +
            "AND (:#{#searchRQ.senderBank} IS NULL OR t.senderBank = :#{#searchRQ.senderBank}) " +
            "AND (:#{#searchRQ.senderCode} IS NULL OR t.senderCode = :#{#searchRQ.senderCode}) " +
            "AND (:#{#searchRQ.receiverAccount} IS NULL OR t.receiverAccount = :#{#searchRQ.receiverAccount}) " +
            "AND (:#{#searchRQ.receiverAccountNo} IS NULL OR t.receiverAccountNo = :#{#searchRQ.receiverAccountNo}) " +
            "AND (:#{#searchRQ.receiverBank} IS NULL OR t.receiverBank = :#{#searchRQ.receiverBank}) " +
            "AND (:#{#searchRQ.receiverCode} IS NULL OR t.receiverCode = :#{#searchRQ.receiverCode}) " +
            "AND (:#{#searchRQ.sourceInstitution} IS NULL OR t.sourceInstitution = :#{#searchRQ.sourceInstitution}) " +
            "AND (CAST(:date AS date) IS NULL OR DATE(t.transactionDate) = CAST(:date AS date))"+
            "AND (:term IS NULL OR " +
            "LOWER(t.transactionId) LIKE LOWER(CONCAT('%', :term, '%'))) " +
            "GROUP BY t.id")
    Page<TransactionManageEntity> searchTransactions(@Param("searchRQ") SearchTransactionManageRQ searchRQ,
                                                     @Param("date") LocalDate date,
                                                     @Param("term") String term,
                                                     Pageable pageable);


    @Query("SELECT t " +
            "FROM TransactionManageEntity t " +
            "JOIN BankAccountEntity b ON (b.accountNumber = t.receiverAccountNo OR b.accountNumber = t.senderAccountNo) " +
            "JOIN CustomerEntity c ON b.customerId = c.id " +
            "JOIN PartnerEntity p ON b.partnerId = p.id " +
            "JOIN SystemConfigurationSourceEntity s ON b.sourceId = s.id " +
            "WHERE b.status = com.openbanking.enums.BankAccountStatus.ACTIVE " +
            "AND c.status = com.openbanking.enums.CustomerStatus.ACTIVE " +
            "AND p.status = com.openbanking.enums.PartnerStatus.ACTIVE " +
            "AND s.status = com.openbanking.enums.SourceConfigStatus.CONNECTED " +
            "AND p.deletedAt IS NULL " +
            "AND s.deletedAt IS NULL " +
            "AND t.deletedAt IS NULL " +
            "AND b.sourceCode = t.sourceInstitution " +
            "AND b.customerId = :id " +
            "AND (:#{#searchRQ.id} IS NULL OR t.id = :#{#searchRQ.id}) " +
            "AND (:#{#searchRQ.transactionId} IS NULL OR t.transactionId = :#{#searchRQ.transactionId}) " +
            "AND (:#{#searchRQ.amount} IS NULL OR t.amount = :#{#searchRQ.amount}) " +
            "AND (:#{#searchRQ.content} IS NULL OR t.content = :#{#searchRQ.content}) " +
            "AND (:#{#searchRQ.source} IS NULL OR t.source = :#{#searchRQ.source}) " +
            "AND (:#{#searchRQ.refNo} IS NULL OR t.refNo = :#{#searchRQ.refNo}) " +
            "AND (:#{#searchRQ.senderAccount} IS NULL OR t.senderAccount = :#{#searchRQ.senderAccount}) " +
            "AND (:#{#searchRQ.senderAccountNo} IS NULL OR t.senderAccountNo = :#{#searchRQ.senderAccountNo}) " +
            "AND (:#{#searchRQ.senderBank} IS NULL OR t.senderBank = :#{#searchRQ.senderBank}) " +
            "AND (:#{#searchRQ.senderCode} IS NULL OR t.senderCode = :#{#searchRQ.senderCode}) " +
            "AND (:#{#searchRQ.receiverAccount} IS NULL OR t.receiverAccount = :#{#searchRQ.receiverAccount}) " +
            "AND (:#{#searchRQ.receiverAccountNo} IS NULL OR t.receiverAccountNo = :#{#searchRQ.receiverAccountNo}) " +
            "AND (:#{#searchRQ.receiverBank} IS NULL OR t.receiverBank = :#{#searchRQ.receiverBank}) " +
            "AND (:#{#searchRQ.receiverCode} IS NULL OR t.receiverCode = :#{#searchRQ.receiverCode}) " +
            "AND (:#{#searchRQ.sourceInstitution} IS NULL OR t.sourceInstitution = :#{#searchRQ.sourceInstitution}) " +
            "AND (CAST(:date AS date) IS NULL OR DATE(t.transactionDate) = CAST(:date AS date))"+
            "AND (:term IS NULL OR " +
            "LOWER(t.transactionId) LIKE LOWER(CONCAT('%', :term, '%'))) "+
            "GROUP BY t.id")
    Page<TransactionManageEntity> searchCustomerTransactions(@Param("id") Long id ,
            @Param("searchRQ") SearchTransactionManageRQ searchRQ,
                                                             @Param("date") LocalDate date,
                                                             @Param("term") String term,
                                                     Pageable pageable);



    @Query("SELECT t FROM TransactionManageEntity t " +
            "JOIN BankAccountEntity b ON (b.accountNumber = t.receiverAccountNo OR b.accountNumber = t.senderAccountNo) " +
            "AND b.status = com.openbanking.enums.BankAccountStatus.ACTIVE " +
            "WHERE b.customerId = :id")
    List<TransactionManageEntity> getListByAccountNumberAndCustomerId(Long id);


    @Query("SELECT t FROM TransactionManageEntity t " +
            "JOIN BankAccountEntity b ON (b.accountNumber = t.receiverAccountNo OR b.accountNumber = t.senderAccountNo) " +
            "WHERE b.customerId IN :ids")
    List<TransactionManageEntity> getListByAccountNumberAndCustomerIdIn(@Param("ids") List<Long> ids);

    @Query("SELECT t " +
            "FROM TransactionManageEntity t " +
            "JOIN BankAccountEntity b ON (b.accountNumber = t.receiverAccountNo OR b.accountNumber = t.senderAccountNo) " +
            "JOIN CustomerEntity c ON b.customerId = c.id " +
            "JOIN PartnerEntity p ON b.partnerId = p.id " +
            "JOIN SystemConfigurationSourceEntity s ON b.sourceId = s.id " +
            "WHERE b.status = com.openbanking.enums.BankAccountStatus.ACTIVE " +
            "AND c.status = com.openbanking.enums.CustomerStatus.ACTIVE " +
            "AND p.status = com.openbanking.enums.PartnerStatus.ACTIVE " +
            "AND s.status = com.openbanking.enums.SourceConfigStatus.CONNECTED " +
            "AND p.deletedAt IS NULL " +
            "AND s.deletedAt IS NULL " +
            "AND t.deletedAt IS NULL " +
            "AND b.sourceCode = t.sourceInstitution " +
            "GROUP BY t.id")
    List<TransactionManageEntity> findActiveTransactions();



    @Query("SELECT t " +
            "FROM TransactionManageEntity t " +
            "LEFT JOIN TransactionManageReconciliationHistoryEntity tr ON tr.transactionManageId = t.id " +
            "JOIN BankAccountEntity b ON (b.accountNumber = t.receiverAccountNo OR b.accountNumber = t.senderAccountNo) " +
            "JOIN CustomerEntity c ON b.customerId = c.id " +
            "JOIN PartnerEntity p ON b.partnerId = p.id " +
            "JOIN SystemConfigurationSourceEntity s ON b.sourceId = s.id " +
            "WHERE b.status = com.openbanking.enums.BankAccountStatus.ACTIVE " +
            "AND c.status = com.openbanking.enums.CustomerStatus.ACTIVE " +
            "AND p.status = com.openbanking.enums.PartnerStatus.ACTIVE " +
            "AND s.status = com.openbanking.enums.SourceConfigStatus.CONNECTED " +
            "AND p.deletedAt IS NULL " +
            "AND s.deletedAt IS NULL " +
            "AND t.deletedAt IS NULL " +
            "AND b.sourceCode = t.sourceInstitution " +
            "AND t.status <> com.openbanking.enums.TransactionStatus.AWAITING_RECONCILIATION " +
            "AND (:#{#searchRQ.amount} IS NULL OR t.amount = :#{#searchRQ.amount}) " +
            "AND (:#{#searchRQ.transactionId} IS NULL OR t.transactionId = :#{#searchRQ.transactionId}) " +
            "AND (:#{#searchRQ.content} IS NULL OR t.content = :#{#searchRQ.content}) " +
            "AND (:#{#searchRQ.source} IS NULL OR t.source = :#{#searchRQ.source}) " +
            "AND (:#{#searchRQ.refNo} IS NULL OR t.refNo = :#{#searchRQ.refNo}) " +
            "AND (:#{#searchRQ.senderAccount} IS NULL OR t.senderAccount = :#{#searchRQ.senderAccount}) " +
            "AND (:#{#searchRQ.senderAccountNo} IS NULL OR t.senderAccountNo = :#{#searchRQ.senderAccountNo}) " +
            "AND (:#{#searchRQ.senderBank} IS NULL OR t.senderBank = :#{#searchRQ.senderBank}) " +
            "AND (:#{#searchRQ.senderCode} IS NULL OR t.senderCode = :#{#searchRQ.senderCode}) " +
            "AND (:#{#searchRQ.receiverAccount} IS NULL OR t.receiverAccount = :#{#searchRQ.receiverAccount}) " +
            "AND (:#{#searchRQ.receiverAccountNo} IS NULL OR t.receiverAccountNo = :#{#searchRQ.receiverAccountNo}) " +
            "AND (:#{#searchRQ.receiverBank} IS NULL OR t.receiverBank = :#{#searchRQ.receiverBank}) " +
            "AND (:#{#searchRQ.receiverCode} IS NULL OR t.receiverCode = :#{#searchRQ.receiverCode}) " +
            "AND (:#{#searchRQ.sourceInstitution} IS NULL OR t.sourceInstitution = :#{#searchRQ.sourceInstitution}) " +
            "AND (CAST(:date AS date) IS NULL OR DATE(t.transactionDate) = CAST(:date AS date)) " +
            "AND (CAST(:reconDate AS date) IS NULL OR DATE(tr.reconciliationDate) = CAST(:reconDate AS date)) " +
            "AND (:term IS NULL OR LOWER(t.transactionId) LIKE LOWER(CONCAT('%', :term, '%'))) " +
            "GROUP BY t.id")
    Page<TransactionManageEntity> searchTransactionRecons(@Param("searchRQ") SearchTransactionManageRQ searchRQ,
                                                          @Param("date") LocalDate date,
                                                          @Param("reconDate") LocalDate reconDate,
                                                          @Param("term") String term,
                                                          Pageable pageable);

    @Query("SELECT t " +
            "FROM TransactionManageEntity t " +
            "JOIN BankAccountEntity b ON (b.accountNumber = t.receiverAccountNo OR b.accountNumber = t.senderAccountNo) " +
            "JOIN CustomerEntity c ON b.customerId = c.id " +
            "JOIN PartnerEntity p ON b.partnerId = p.id " +
            "JOIN SystemConfigurationSourceEntity s ON b.sourceId = s.id " +
            "WHERE b.status = com.openbanking.enums.BankAccountStatus.ACTIVE " +
            "AND c.status = com.openbanking.enums.CustomerStatus.ACTIVE " +
            "AND p.status = com.openbanking.enums.PartnerStatus.ACTIVE " +
            "AND s.status = com.openbanking.enums.SourceConfigStatus.CONNECTED " +
            "AND p.deletedAt IS NULL " +
            "AND s.deletedAt IS NULL " +
            "AND t.deletedAt IS NULL " +
            "AND b.sourceCode = t.sourceInstitution " +
            "AND t.status <> com.openbanking.enums.TransactionStatus.AWAITING_RECONCILIATION " +
            "GROUP BY t.id")
    List<TransactionManageEntity> findActiveReconciliations();

    @Query("SELECT t FROM TransactionManageEntity t WHERE t.sourceInstitution = :sourceCode")
    List<TransactionManageEntity> findBySource(@Param("sourceCode") String sourceCode);
}



