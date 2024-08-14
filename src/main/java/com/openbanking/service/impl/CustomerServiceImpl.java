package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.*;
import com.openbanking.exception.ResourceNotFoundException;
import com.openbanking.mapper.BankAccountMapper;
import com.openbanking.mapper.CustomerMapper;
import com.openbanking.model.bank_account.BankAccount;
import com.openbanking.model.bank_account.CreateBankAccount;
import com.openbanking.model.bank_account.UpdateBankAccount;
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

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl  extends BaseServiceImpl<CustomerEntity, Customer, CreateCustomer, UpdateCustomer, Long> implements CustomerService {
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
    public void update(UpdateCustomer updateCustomer) {
        CustomerEntity customerEntity = customerRepository.findById(updateCustomer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + updateCustomer.getId()));
        customerMapper.updateEntityFromDTO(updateCustomer, customerEntity);
        customerRepository.save(customerEntity);


        List<UpdateBankAccount> updateCustomerList = updateCustomer.getListUpdateBankAccounts();
        List<BankAccountEntity> bankAccountEntities = new ArrayList<>();
        for (UpdateBankAccount updateBankAccount : updateCustomerList) {
            BankAccountEntity entity = bankAccountMapper.getEntity(updateBankAccount);
            entity.setCustomerId(customerEntity.getId());
            bankAccountEntities.add(entity);
        }
        bankAccountRepository.saveAll(bankAccountEntities);
    }

    @Override
    public CustomerDetail getCustomerDetail(Long id) {
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        CustomerDetail customerDetail = customerMapper.toDetail(customerEntity);
        List<BankAccountEntity> bankAccountEntities = bankAccountRepository.getListBankAccountByCustomerId(id);
        List<BankAccount> bankAccounts = bankAccountMapper.toDTOs(bankAccountEntities);
        customerDetail.setListBankAccount(bankAccounts);
        return  customerDetail;
    }
    @Override
    public void deleteByListId(List<Long> ids) {
        List<Long> bankAccountIds = bankAccountRepository.getListBankAccountIdByCustomerIds(ids);
        bankAccountEditHistoryRepository.deleteByBankAccountIdIn(bankAccountIds);
        bankAccountRepository.deleteByCustomerIdIn(ids);
        List<CustomerEntity> customerEntities = customerRepository.findAllByIdIn(ids);
        customerEntities.forEach(customerEntity -> customerEntity.setDeletedAt(OffsetDateTime.now()));
    }
}