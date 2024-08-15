package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.BankAccountEntity;
import com.openbanking.entity.CustomerEntity;
import com.openbanking.model.bank_account.ListPartnerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface BankAccountRepository extends BaseRepository<BankAccountEntity, Long> {
    @Query(value = "select b from CustomerEntity c join BankAccountEntity b on b.customerId = c.id and b.customerId = :id")
    List<BankAccountEntity> getListBankAccountByCustomerId(Long id);

    void deleteByCustomerId(Long customerId);

    void deleteByCustomerIdIn(List<Long> customerIds);

    @Query(value = "select b.id from BankAccountEntity b where b.customerId in :ids")
    List<Long> getListBankAccountIdByCustomerIds(@Param("ids") List<Long> ids);

   @Query("select distinct b.status from BankAccountEntity b")
    List<String> findDistinctStatus();

    @Query("select distinct new com.openbanking.model.bank_account.ListPartnerInfo(b.partnerId, b.partnerName) FROM BankAccountEntity b")
    List<ListPartnerInfo> findDistinctPartnerInfo();

    @Query("SELECT b FROM BankAccountEntity b " +
            "WHERE (:partnerName IS NULL AND :status IS NULL) OR " +
            "(b.partnerName = :partnerName) OR " +
            "(b.status = :status) OR " +
            "(b.partnerName = :partnerName AND b.status = :status)")
    Page<BankAccountEntity> searchBankAccount(@Param("status") String status,
                                              @Param("partnerName") String partnerName,
                                               Pageable pageable);
}


