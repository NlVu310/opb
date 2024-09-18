package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.ReconciliationManageEntity;
import com.openbanking.model.reconciliation_manage.SearchReconciliationRQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReconciliationManageRepository extends BaseRepository<ReconciliationManageEntity, Long> {
@Query("SELECT r " +
        "FROM ReconciliationManageEntity r " +
        "JOIN BankAccountEntity b ON (b.accountNumber = r.receiverAccountNo OR b.accountNumber = r.senderAccountNo) " +
        "   AND b.status = com.openbanking.enums.BankAccountStatus.ACTIVE " +
        "JOIN CustomerEntity c ON b.customerId = c.id " +
        "   AND c.status = com.openbanking.enums.CustomerStatus.ACTIVE " +
        "JOIN PartnerEntity p ON (p.code = r.senderCode OR p.code = r.receiverCode) " +
        "   AND p.status = com.openbanking.enums.PartnerStatus.ACTIVE " +
        "   AND p.deletedAt IS NULL " +
        "JOIN SystemConfigurationSourceEntity s ON s.code = r.sourceInstitution " +
        "   AND s.status = com.openbanking.enums.SourceConfigStatus.CONNECTED " +
        "   AND s.deletedAt IS NULL " +
        "AND (:#{#searchRQ.amount} IS NULL OR r.amount = :#{#searchRQ.amount}) " +
        "AND (:#{#searchRQ.transactionId} IS NULL OR r.transactionId = :#{#searchRQ.transactionId}) " +
        "AND (:#{#searchRQ.content} IS NULL OR r.content = :#{#searchRQ.content}) " +
        "AND (:#{#searchRQ.source} IS NULL OR r.source = :#{#searchRQ.source}) " +
        "AND (:#{#searchRQ.refNo} IS NULL OR r.refNo = :#{#searchRQ.refNo}) " +
        "AND (:#{#searchRQ.senderAccount} IS NULL OR r.senderAccount = :#{#searchRQ.senderAccount}) " +
        "AND (:#{#searchRQ.senderAccountNo} IS NULL OR r.senderAccountNo = :#{#searchRQ.senderAccountNo}) " +
        "AND (:#{#searchRQ.senderBank} IS NULL OR r.senderBank = :#{#searchRQ.senderBank}) " +
        "AND (:#{#searchRQ.senderCode} IS NULL OR r.senderCode = :#{#searchRQ.senderCode}) " +
        "AND (:#{#searchRQ.receiverAccount} IS NULL OR r.receiverAccount = :#{#searchRQ.receiverAccount}) " +
        "AND (:#{#searchRQ.receiverAccountNo} IS NULL OR r.receiverAccountNo = :#{#searchRQ.receiverAccountNo}) " +
        "AND (:#{#searchRQ.receiverBank} IS NULL OR r.receiverBank = :#{#searchRQ.receiverBank}) " +
        "AND (:#{#searchRQ.receiverCode} IS NULL OR r.receiverCode = :#{#searchRQ.receiverCode}) " +
        "AND (:#{#searchRQ.sourceInstitution} IS NULL OR r.sourceInstitution = :#{#searchRQ.sourceInstitution}) " +
        "AND (CAST(:transDate AS date) IS NULL OR DATE(r.transactionDate) = CAST(:transDate AS date))"+
        "AND (CAST(:reconDate AS date) IS NULL OR DATE(r.reconciliationDate) = CAST(:reconDate AS date))"+
        "AND (:term IS NULL OR " +
        "LOWER(r.transactionId) LIKE LOWER(CONCAT('%', :term, '%'))) " +
        "GROUP BY r.id")
Page<ReconciliationManageEntity> searchTransactions(@Param("searchRQ") SearchReconciliationRQ searchRQ,
                                                    @Param("transDate") LocalDate transDate,
                                                    @Param("reconDate") LocalDate reconDate,
                                                    @Param("term") String term,
                                                        Pageable pageable);


@Query("SELECT r " +
        "FROM ReconciliationManageEntity r " +
        "JOIN BankAccountEntity b " +
        "   ON (b.accountNumber =r.receiverAccountNo OR b.accountNumber = r.senderAccountNo) " +
        "   AND b.status = com.openbanking.enums.BankAccountStatus.ACTIVE " +
        "JOIN CustomerEntity c ON b.customerId = c.id " +
        "   AND c.status = com.openbanking.enums.CustomerStatus.ACTIVE " +
        "JOIN PartnerEntity p " +
        "   ON (p.code = r.senderCode OR p.code = r.receiverCode) " +
        "   AND p.status = com.openbanking.enums.PartnerStatus.ACTIVE " +
        "   AND p.deletedAt IS NULL " +
        "JOIN SystemConfigurationSourceEntity s " +
        "   ON s.code = r.sourceInstitution " +
        "   AND s.status = com.openbanking.enums.SourceConfigStatus.CONNECTED " +
        "   AND s.deletedAt IS NULL " +
        "GROUP BY r.id")
    List<ReconciliationManageEntity> findActiveTransactions();

}
