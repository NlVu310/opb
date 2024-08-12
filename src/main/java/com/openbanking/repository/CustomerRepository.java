package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.AccountTypeEntity;
import com.openbanking.entity.CustomerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends BaseRepository<CustomerEntity, Long> {
    @Query(value = "select c from AccountEntity a join CustomerEntity c on a.customerId = c.id and a.id = :id")
    List<CustomerEntity> getListCustomerTypeByAccountId(@Param("id") Long id);
}
