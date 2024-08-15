package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.*;
import com.openbanking.exception.DeleteException;
import com.openbanking.exception.InsertException;
import com.openbanking.exception.ResourceNotFoundException;
import com.openbanking.mapper.BankAccountMapper;
import com.openbanking.mapper.CustomerMapper;
import com.openbanking.model.bank_account.BankAccount;
import com.openbanking.model.bank_account.CreateBankAccount;
import com.openbanking.model.customer.CreateCustomer;
import com.openbanking.model.customer.Customer;
import com.openbanking.model.customer.CustomerDetail;
import com.openbanking.model.customer.UpdateCustomer;
import com.openbanking.repository.BankAccountEditHistoryRepository;
import com.openbanking.repository.BankAccountRepository;
import com.openbanking.repository.CustomerRepository;
import com.openbanking.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl extends BaseServiceImpl<CustomerEntity, Customer, CreateCustomer, UpdateCustomer, Long> implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private BankAccountEditHistoryRepository bankAccountEditHistoryRepository;


    public CustomerServiceImpl(BaseRepository<CustomerEntity, Long> repository, BaseMapper<CustomerEntity, Customer, CreateCustomer, UpdateCustomer> mapper) {
        super(repository, mapper);
    }

    @Override
    public void create(CreateCustomer createCustomer) {
        CustomerEntity customerEntity = customerMapper.toEntityFromCD(createCustomer);
        customerRepository.save(customerEntity);

        List<CreateBankAccount> bankAccountList = createCustomer.getBankAccountList();
        List<BankAccountEntity> bankAccountEntities = new ArrayList<>();

        for (CreateBankAccount dtoItem : bankAccountList) {
            BankAccountEntity entity = bankAccountMapper.toEntityFromCD(dtoItem);
            entity.setCustomerId(customerEntity.getId());
            bankAccountEntities.add(entity);
        }
        bankAccountRepository.saveAll(bankAccountEntities);
    }

    @Override
    @Transactional
    public void update(UpdateCustomer updateCustomer) {
        CustomerEntity customerEntity = customerRepository.findById(updateCustomer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + updateCustomer.getId()));
        customerMapper.updateEntityFromUDTO(updateCustomer, customerEntity);
        customerRepository.save(customerEntity);

        List<BankAccountEntity> existedBankAccounts = bankAccountRepository.getListBankAccountByCustomerId(updateCustomer.getId());
        Set<Long> bankAccountIdSet = existedBankAccounts.stream()
                .map(BankAccountEntity::getId)
                .collect(Collectors.toSet());
        Map<Long, BankAccountEntity> bankAccountMap = existedBankAccounts.stream()
                .collect(Collectors.toMap(BankAccountEntity::getId, Function.identity()));

        List<BankAccountEditHistoryEntity> historyEntities = new ArrayList<>();
        List<BankAccountEntity> bankAccountsToUpdate = new ArrayList<>();
        List<BankAccountEntity> bankAccountsToSave = new ArrayList<>();

        updateCustomer.getListUpdateBankAccounts().forEach(updateBankAccount -> {
            Long accountId = updateBankAccount.getId();
            if (bankAccountIdSet.contains(accountId)) {
                BankAccountEntity existingEntity = bankAccountMap.get(accountId);
                if (existingEntity != null) {
                    BankAccountEditHistoryEntity history = new BankAccountEditHistoryEntity();
                    history.setBankAccountId(accountId);
                    history.setOldFromDate(existingEntity.getFromDate());
                    history.setOldToDate(existingEntity.getToDate());
                    history.setNewFromDate(updateBankAccount.getFromDate());
                    history.setNewToDate(updateBankAccount.getToDate());
                    historyEntities.add(history);

                    bankAccountMapper.updateEntityFromUDTO(updateBankAccount, existingEntity);
                    bankAccountsToUpdate.add(existingEntity);
                }
            } else {
                BankAccountEntity newEntity = bankAccountMapper.getEntity(updateBankAccount);
                bankAccountsToSave.add(newEntity);
            }
        });

        bankAccountRepository.saveAll(bankAccountsToUpdate);
        bankAccountRepository.saveAll(bankAccountsToSave);
        bankAccountEditHistoryRepository.saveAll(historyEntities);
    }

    @Override
    public CustomerDetail getCustomerDetail(Long id) {
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        CustomerDetail customerDetail = customerMapper.toDetail(customerEntity);
        List<BankAccountEntity> bankAccountEntities = bankAccountRepository.getListBankAccountByCustomerId(id);
        List<BankAccount> bankAccounts = bankAccountMapper.toDTOs(bankAccountEntities);
        customerDetail.setBankAccounts(bankAccounts);
        return customerDetail;
    }

    @Override
    @Transactional
    public void deleteByListId(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        List<Long> bankAccountIds = bankAccountRepository.getListBankAccountIdByCustomerIds(ids);
        if (bankAccountIds != null && !bankAccountIds.isEmpty()) {
            try {
                bankAccountEditHistoryRepository.deleteByBankAccountIdIn(bankAccountIds);
                bankAccountRepository.deleteByCustomerIdIn(ids);
            } catch (Exception e) {
                throw new DeleteException("Delete fail");
            }
        }

        List<CustomerEntity> customerEntities = customerRepository.findByIdIn(ids);
        if (customerEntities != null && !customerEntities.isEmpty()) {
            customerEntities.forEach(customerEntity -> customerEntity.setDeletedAt(OffsetDateTime.now()));
            try {
                customerRepository.saveAll(customerEntities);
            } catch (Exception e) {
                throw new InsertException("Insert fail");
            }
        }
    }

}