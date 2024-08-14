package com.openbanking.repository;

import com.openbanking.comon.BaseRepository;
import com.openbanking.entity.CustomerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends BaseRepository<CustomerEntity, Long> {
    @Query(value = "select c from AccountEntity a join CustomerEntity c on c.accountId = a.id and c.accountId = :id")
    List<CustomerEntity> getListCustomerTypeByAccountId(Long id);

    List<CustomerEntity> findAllByIdIn(List<Long> ids);
}
