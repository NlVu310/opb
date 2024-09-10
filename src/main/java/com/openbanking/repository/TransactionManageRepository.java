package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.PartnerEntity;
import com.openbanking.entity.TransactionManageEntity;
import com.openbanking.model.partner.SearchPartnerRQ;
import com.openbanking.model.transaction_manage.SearchTransactionManageRQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TransactionManageRepository extends BaseRepository<TransactionManageEntity, Long> {
    @Query("SELECT t " +
            "FROM TransactionManageEntity t " +
            "JOIN BankAccountEntity b ON (b.accountNumber = t.receiverAccountNo OR b.accountNumber = t.senderAccountNo) " +
            "JOIN PartnerEntity p ON (p.code = t.senderCode OR p.code = t.receiverCode) " +
            "JOIN SystemConfigurationSourceEntity s ON s.code = t.sourceInstitution " +
            "WHERE t.deletedAt IS NULL " +
            "AND b.status = com.openbanking.enums.BankAccountStatus.ACTIVE " +
            "AND p.status = com.openbanking.enums.PartnerStatus.ACTIVE " +
            "AND p.deletedAt IS NULL " +
            "AND s.status = com.openbanking.enums.SourceConfigStatus.CONNECTED " +
            "AND s.deletedAt IS NULL " +
//            "AND (:#{#searchRQ.id} IS NULL OR t.id = :#{#searchRQ.id}) " +
//        "AND (CAST(:#{#searchRQ.transactionDate} AS date) IS NULL OR DATE(t.transactionDate) = CAST(:#{#searchRQ.transactionDate} AS date)) " +
//            "AND (:#{#searchRQ.amount} IS NULL OR LOWER(t.amount) LIKE LOWER(CONCAT('%', :#{#searchRQ.amount}, '%'))) " +
//            "AND (:#{#searchRQ.content} IS NULL OR LOWER(t.content) LIKE LOWER(CONCAT('%', :#{#searchRQ.content}, '%'))) " +
//            "AND (:#{#searchRQ.source} IS NULL OR LOWER(t.source) LIKE LOWER(CONCAT('%', :#{#searchRQ.source}, '%'))) " +
//            "AND (:#{#searchRQ.refNo} IS NULL OR LOWER(t.refNo) LIKE LOWER(CONCAT('%', :#{#searchRQ.refNo}, '%'))) " +
//            "AND (:#{#searchRQ.senderAccount} IS NULL OR LOWER(t.senderAccount) LIKE LOWER(CONCAT('%', :#{#searchRQ.senderAccount}, '%'))) " +
//            "AND (:#{#searchRQ.senderAccountNo} IS NULL OR LOWER(t.senderAccountNo) LIKE LOWER(CONCAT('%', :#{#searchRQ.senderAccountNo}, '%'))) " +
//            "AND (:#{#searchRQ.senderBank} IS NULL OR LOWER(t.senderBank) LIKE LOWER(CONCAT('%', :#{#searchRQ.senderBank}, '%'))) " +
//            "AND (:#{#searchRQ.senderCode} IS NULL OR LOWER(t.senderCode) LIKE LOWER(CONCAT('%', :#{#searchRQ.senderCode}, '%'))) " +
//            "AND (:#{#searchRQ.receiverAccount} IS NULL OR LOWER(t.receiverAccount) LIKE LOWER(CONCAT('%', :#{#searchRQ.receiverAccount}, '%'))) " +
//            "AND (:#{#searchRQ.receiverAccountNo} IS NULL OR LOWER(t.receiverAccountNo) LIKE LOWER(CONCAT('%', :#{#searchRQ.receiverAccountNo}, '%'))) " +
//            "AND (:#{#searchRQ.receiverBank} IS NULL OR LOWER(t.receiverBank) LIKE LOWER(CONCAT('%', :#{#searchRQ.receiverBank}, '%'))) " +
//            "AND (:#{#searchRQ.receiverCode} IS NULL OR LOWER(t.receiverCode) LIKE LOWER(CONCAT('%', :#{#searchRQ.receiverCode}, '%'))) " +
//            "AND (:#{#searchRQ.sourceInstitution} IS NULL OR LOWER(t.sourceInstitution) LIKE LOWER(CONCAT('%', :#{#searchRQ.sourceInstitution}, '%'))) " +
//            "AND (:#{#searchRQ.status} IS NULL OR t.status = :#{#searchRQ.status}) " +
//            "AND (:term IS NULL OR " +
//            "LOWER(t.amount) LIKE LOWER(CONCAT('%', :term, '%')) " +
//            "OR LOWER(t.content) LIKE LOWER(CONCAT('%', :term, '%')) " +
//            "OR LOWER(t.source) LIKE LOWER(CONCAT('%', :term, '%')) " +
//            "OR LOWER(t.refNo) LIKE LOWER(CONCAT('%', :term, '%')) " +
//            "OR LOWER(t.senderAccount) LIKE LOWER(CONCAT('%', :term, '%')) " +
//            "OR LOWER(t.senderAccountNo) LIKE LOWER(CONCAT('%', :term, '%')) " +
//            "OR LOWER(t.senderBank) LIKE LOWER(CONCAT('%', :term, '%')) " +
//            "OR LOWER(t.senderCode) LIKE LOWER(CONCAT('%', :term, '%')) " +
//            "OR LOWER(t.receiverAccount) LIKE LOWER(CONCAT('%', :term, '%')) " +
//            "OR LOWER(t.receiverAccountNo) LIKE LOWER(CONCAT('%', :term, '%')) " +
//            "OR LOWER(t.receiverBank) LIKE LOWER(CONCAT('%', :term, '%')) " +
//            "OR LOWER(t.receiverCode) LIKE LOWER(CONCAT('%', :term, '%')) " +
//            "OR LOWER(t.sourceInstitution) LIKE LOWER(CONCAT('%', :term, '%')))" +
            " ")
    Page<TransactionManageEntity> searchTransactions(@Param("searchRQ") SearchTransactionManageRQ searchRQ,
                                                     @Param("term") String term,
                                                     Pageable pageable);


    @Query("SELECT t FROM TransactionManageEntity t " +
            "JOIN BankAccountEntity b ON (b.accountNumber = t.receiverAccountNo OR b.accountNumber = t.senderAccountNo) " +
            "AND b.status = com.openbanking.enums.BankAccountStatus.ACTIVE " +
            "JOIN PartnerEntity p ON (p.code = t.senderCode OR p.code = t.receiverCode) " +
            "AND p.status = com.openbanking.enums.PartnerStatus.ACTIVE " +
            "AND p.deletedAt IS NULL " +
            "JOIN SystemConfigurationSourceEntity s ON s.code = t.sourceInstitution " +
            "AND s.status = com.openbanking.enums.SourceConfigStatus.CONNECTED " +
            "AND s.deletedAt IS NULL " +
            "WHERE t.deletedAt IS NULL " +
            "AND b.customerId = :id " +
            "AND (:#{#searchRQ.id} IS NULL OR t.id = :#{#searchRQ.id}) " +
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
            "AND (:term IS NULL OR " +
            "LOWER(t.amount) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(t.content) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(t.source) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(t.refNo) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(t.senderAccount) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(t.senderAccountNo) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(t.senderBank) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(t.senderCode) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(t.receiverAccount) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(t.receiverAccountNo) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(t.receiverBank) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(t.receiverCode) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(TO_CHAR(t.transactionDate, 'dd-MM-yyyy')) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(t.sourceInstitution) LIKE LOWER(CONCAT('%', :term, '%')))"+
            "GROUP BY t.id")
    Page<TransactionManageEntity> searchCustomerTransactions(@Param("id") Long id ,
            @Param("searchRQ") SearchTransactionManageRQ searchRQ,
                                                     @Param("term") String term,
                                                     Pageable pageable);



    @Query("SELECT t FROM TransactionManageEntity t " +
            "JOIN BankAccountEntity b ON (b.accountNumber = t.receiverAccountNo OR b.accountNumber = t.senderAccountNo) " +
            "AND b.status = com.openbanking.enums.BankAccountStatus.ACTIVE " +
            "WHERE b.customerId = :id")
    List<TransactionManageEntity> getListByAccountNumberAndCustomerId(Long id);


    @Query("SELECT t FROM TransactionManageEntity t " +
            "JOIN BankAccountEntity b ON (b.accountNumber = t.receiverAccountNo OR b.accountNumber = t.senderAccountNo) " +
            "WHERE b.customerId = :ids")
    List<TransactionManageEntity> getListByAccountNumberAndCustomerIdIn(List<Long> ids);
}



