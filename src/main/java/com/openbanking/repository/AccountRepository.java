package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.AccountEntity;
import com.openbanking.model.account.AccountInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends BaseRepository<AccountEntity, Long>, JpaRepository<AccountEntity, Long>, JpaSpecificationExecutor<AccountEntity> {
    Optional<AccountEntity> findByUsernameAndDeletedAtNull(String username);
    List<AccountEntity> findAllByIdInAndDeletedAtNull(List<Long> ids);

    List<AccountEntity> findByAccountTypeIdAndDeletedAtNull(Long accountTypeId);
    List<AccountEntity> findAllByDeletedAtNull();

    @Query("SELECT DISTINCT a.username FROM AccountEntity a WHERE a.deletedAt IS NULL")
    List<String> findDistinctUsernames();

    @Query(value = "select a.id from AccountEntity a where a.customerId in :ids")
    List<Long> getListAccountIdByCustomerIds(List<Long> ids);
    @Query(value = "SELECT a.id FROM account a WHERE EXISTS (" +
            "SELECT 1 FROM jsonb_array_elements_text(a.customer_concerned) AS elem " +
            "WHERE CAST(elem AS BIGINT) IN :ids)", nativeQuery = true)
    List<Long> getListCustomerConcernedByCustomerIds(@Param("ids") List<Long> ids);

    @Query(value = "SELECT a.id, a.name as accountName, a.username, a.email, at.name as accountTypeName, " +
            "c.name as customerName, a.status, a.created_at as createdAt, a.phone, a.note, a.created_by as createdBy " +
            "FROM account a " +
            "JOIN account_type at ON a.account_type_id = at.id " +
            "JOIN customer c ON a.customer_id = c.id " +
            "WHERE (:id IS NULL OR a.id = :id) " +
            "AND (:term IS NULL OR " +
            "(LOWER(a.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(a.username) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(a.email) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(at.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(a.status) LIKE LOWER(CONCAT('%', :term, '%')))) " +
            "AND (:name IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:username IS NULL OR LOWER(a.username) LIKE LOWER(CONCAT('%', :username, '%'))) " +
            "AND (:email IS NULL OR LOWER(a.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
            "AND (:accountTypeName IS NULL OR LOWER(at.name) LIKE LOWER(CONCAT('%', :accountTypeName, '%'))) " +
            "AND (:customerName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%'))) " +
            "AND (:status IS NULL OR LOWER(a.status) LIKE LOWER(CONCAT('%', :status, '%'))) " +
            "AND (:createdBy IS NULL OR a.created_by = :createdBy) " +
            "AND ((CAST(:createdAt AS date)) IS NULL OR CAST(a.created_at AS date) = CAST(:createdAt AS date)) " +
            "AND a.deleted_at IS NULL",
            countQuery = "SELECT COUNT(*) " +
                    "FROM account a " +
                    "JOIN account_type at ON a.account_type_id = at.id " +
                    "JOIN customer c ON a.customer_id = c.id " +
                    "WHERE (:id IS NULL OR a.id = :id) " +
                    "AND (:term IS NULL OR " +
                    "(LOWER(a.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
                    "OR LOWER(a.username) LIKE LOWER(CONCAT('%', :term, '%')) " +
                    "OR LOWER(a.email) LIKE LOWER(CONCAT('%', :term, '%')) " +
                    "OR LOWER(at.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
                    "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%')) " +
                    "OR LOWER(a.status) LIKE LOWER(CONCAT('%', :term, '%')))) " +
                    "AND (:name IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
                    "AND (:username IS NULL OR LOWER(a.username) LIKE LOWER(CONCAT('%', :username, '%'))) " +
                    "AND (:email IS NULL OR LOWER(a.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
                    "AND (:accountTypeName IS NULL OR LOWER(at.name) LIKE LOWER(CONCAT('%', :accountTypeName, '%'))) " +
                    "AND (:customerName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%'))) " +
                    "AND (:status IS NULL OR LOWER(a.status) LIKE LOWER(CONCAT('%', :status, '%'))) " +
                    "AND (:createdBy IS NULL OR a.created_by = :createdBy) " +
                    "AND ((CAST(:createdAt AS date)) IS NULL OR CAST(a.created_at AS date) = CAST(:createdAt AS date)) " +
                    "AND a.deleted_at IS NULL",
            nativeQuery = true)
    Page<AccountInfo> searchAccounts(
            @Param("id") Long id,
            @Param("term") String term,
            @Param("name") String name,
            @Param("username") String username,
            @Param("email") String email,
            @Param("accountTypeName") String accountTypeName,
            @Param("customerName") String customerName,
            @Param("status") String status,
            @Param("createdBy") Long createdBy,
            @Param("createdAt") LocalDate createdAt,
            Pageable pageable
    );



}












