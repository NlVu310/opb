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
    @Query("SELECT t FROM TransactionManageEntity t " +
            "JOIN BankAccountEntity b ON (b.accountNumber = t.receiverAccountNo OR b.accountNumber = t.senderAccountNo) " +
            "WHERE t.deletedAt IS NULL " +
            "AND b.status = com.openbanking.enums.BankAccountStatus.ACTIVE " +
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
            "OR LOWER(t.sourceInstitution) LIKE LOWER(CONCAT('%', :term, '%')))")
    Page<TransactionManageEntity> searchTransactions(@Param("searchRQ") SearchTransactionManageRQ searchRQ,
                                       @Param("term") String term,
                                       Pageable pageable);

    @Query("SELECT t FROM TransactionManageEntity t " +
            "JOIN BankAccountEntity b ON (b.accountNumber = t.receiverAccountNo OR b.accountNumber = t.senderAccountNo) " +
            "WHERE b.customerId = :id")
    List<TransactionManageEntity> getListByAccountNumberAndCustomerId(Long id);


    @Query("SELECT t FROM TransactionManageEntity t " +
            "JOIN BankAccountEntity b ON (b.accountNumber = t.receiverAccountNo OR b.accountNumber = t.senderAccountNo) " +
            "WHERE b.customerId = :ids")
    List<TransactionManageEntity> getListByAccountNumberAndCustomerIdIn(List<Long> ids);
}



