package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.comon.PaginationRS;
import com.openbanking.entity.*;
import com.openbanking.exception.DeleteException;
import com.openbanking.exception.InsertException;
import com.openbanking.exception.ResourceNotFoundException;
import com.openbanking.mapper.BankAccountMapper;
import com.openbanking.mapper.CustomerMapper;
import com.openbanking.mapper.TransactionManageMapper;
import com.openbanking.model.bank_account.BankAccount;
import com.openbanking.model.bank_account.CreateBankAccount;
import com.openbanking.model.customer.*;
import com.openbanking.model.partner.PartnerDetail;
import com.openbanking.model.system_configuration_source.SystemConfigurationSource;
import com.openbanking.model.transaction_manage.SearchTransactionManageRQ;
import com.openbanking.model.transaction_manage.TransactionManage;
import com.openbanking.repository.*;
import com.openbanking.service.BankAccountService;
import com.openbanking.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private TransactionManageMapper transactionManageMapper;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private TransactionManageRepository transactionManageRepository;
    @Autowired
    private BankAccountEditHistoryRepository bankAccountEditHistoryRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private SystemConfigurationTransactionContentRepository systemConfigurationTransactionContentRepository;
    @Autowired
    private BankAccountService bankAccountService;


    public CustomerServiceImpl(BaseRepository<CustomerEntity, Long> repository, BaseMapper<CustomerEntity, Customer, CreateCustomer, UpdateCustomer> mapper) {
        super(repository, mapper);
    }

    @Override
    public void create(CreateCustomer createCustomer) {
        try {
            if (customerRepository.existsByTaxNoAndDeletedAtIsNull(createCustomer.getTaxNo())) {
                throw new IllegalStateException("tax existed.");
            }

            CustomerEntity customerEntity = customerMapper.toEntityFromCD(createCustomer);
            customerRepository.save(customerEntity);

            List<CreateBankAccount> bankAccountList = createCustomer.getBankAccountList();
            if (bankAccountList != null) {
                Set<String> accountSourcePairs = new HashSet<>();
                for (CreateBankAccount dtoItem : bankAccountList) {
                    if (dtoItem != null) {
                        String pair = dtoItem.getAccountNumber() + "|" + dtoItem.getSourceCode();
                        if (!accountSourcePairs.add(pair)) {
                            throw new IllegalStateException("Duplicate account number and source code in list");
                        }
                    }
                }

                List<String> accountNumberList = bankAccountList.stream()
                        .map(CreateBankAccount::getAccountNumber)
                        .distinct()
                        .collect(Collectors.toList());

                List<String> sourceCodeList = bankAccountList.stream()
                        .map(CreateBankAccount::getSourceCode)
                        .distinct()
                        .collect(Collectors.toList());

                List<BankAccountEntity> existingByAccountNumbers = bankAccountRepository.findByAccountNumberIn(accountNumberList);
                List<BankAccountEntity> existingAccountsBySource = bankAccountRepository.findBySourceCodeIn(sourceCodeList);

                Set<String> existingAccountSourcePairs = new HashSet<>();
                for (BankAccountEntity existing : existingByAccountNumbers) {
                    existingAccountSourcePairs.add(existing.getAccountNumber() + "|" + existing.getSourceCode());
                }

                for (BankAccountEntity existing : existingAccountsBySource) {
                    existingAccountSourcePairs.add(existing.getAccountNumber() + "|" + existing.getSourceCode());
                }

                for (CreateBankAccount dtoItem : bankAccountList) {
                    if (dtoItem != null) {
                        String pair = dtoItem.getAccountNumber() + "|" + dtoItem.getSourceCode();
                        if (existingAccountSourcePairs.contains(pair)) {
                            throw new IllegalStateException("Duplicate account number and source code in the database");
                        }
                    }
                }

                List<BankAccountEntity> bankAccountEntities = bankAccountList.stream()
                        .map(dtoItem -> {
                            BankAccountEntity entity = bankAccountMapper.toEntityFromCD(dtoItem);
                            if (entity != null) {
                                entity.setCustomerId(customerEntity.getId());
                                entity.setStatus(bankAccountService.determineStatus(entity, OffsetDateTime.now()));
                            }
                            return entity;
                        })
                        .collect(Collectors.toList());

                if (!bankAccountEntities.isEmpty()) {
                    bankAccountRepository.saveAll(bankAccountEntities);
                }
            }
        } catch (InsertException e) {
            throw e;
        } catch (Exception e) {
            String originalMessage = e.getMessage();
            throw new RuntimeException("Failed to create Customer: " + originalMessage, e);
        }
    }

    @Override
    @Transactional
    public void update(UpdateCustomer updateCustomer) {
        try {
            CustomerEntity customerEntity = customerRepository.findById(updateCustomer.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + updateCustomer.getId()));

            if (customerRepository.existsByTaxNoAndIdNotAndDeletedAtIsNull(updateCustomer.getTaxNo(), updateCustomer.getId())) {
                throw new IllegalStateException("tax existed.");
            }
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
                Long bankAccountId = updateBankAccount.getId();
                if (bankAccountIdSet.contains(bankAccountId)) {
                    BankAccountEntity existingEntity = bankAccountMap.get(bankAccountId);
                    if (existingEntity != null) {
                        BankAccountEditHistoryEntity history = new BankAccountEditHistoryEntity();
                        history.setBankAccountId(bankAccountId);
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
                    newEntity.setCustomerId(updateCustomer.getId());
                    bankAccountsToSave.add(newEntity);
                }
            });

            bankAccountRepository.saveAll(bankAccountsToUpdate);
            bankAccountRepository.saveAll(bankAccountsToSave);
            bankAccountEditHistoryRepository.saveAll(historyEntities);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException("Failed to update Customer", e);
        }

    }

    @Override
    public CustomerDetail getCustomerDetail(Long id) {
        try {
            CustomerEntity customerEntity = customerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
            CustomerDetail customerDetail = customerMapper.toDetail(customerEntity);
            List<BankAccountEntity> bankAccountEntities = bankAccountRepository.getListBankAccountByCustomerId(id);
            List<BankAccount> bankAccounts = bankAccountMapper.toDTOs(bankAccountEntities);
            customerDetail.setBankAccounts(bankAccounts);
            return customerDetail;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Customer", e);
        }
    }

    @Override
    @Transactional
    public void deleteByListId(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        List<Long> bankAccountIds = bankAccountRepository.getListBankAccountIdByCustomerIds(ids);
        List<Long> accountIds = accountRepository.getListAccountIdByCustomerIds(ids);
        List<Long> CustomerConcerned = accountRepository.getListCustomerConcernedByCustomerIds(ids);

        List<Long> TransactionContentIds = systemConfigurationTransactionContentRepository.getListTransactionContentIdByCustomerIds(ids);
        List<TransactionManageEntity> transactionManageEntities = transactionManageRepository.getListByAccountNumberAndCustomerIdIn(ids);
        List<TransactionManage> transactionManages = transactionManageEntities.stream()
                .map(transactionManageMapper::toDTO)
                .collect(Collectors.toList());


        if (TransactionContentIds != null && !TransactionContentIds.isEmpty()) {
            throw new RuntimeException("Delete fail , Config Transaction Content existed");
        }
        if (!transactionManages.isEmpty()) {
            throw new RuntimeException("Delete fail , Transaction existed");
        }
        if (accountIds != null && !accountIds.isEmpty()) {
            throw new RuntimeException("Delete fail , Account existed");
        }
        if (CustomerConcerned != null && !CustomerConcerned.isEmpty()) {
            throw new RuntimeException("Delete fail , Customer data reference existed");
        }
        if (bankAccountIds != null && !bankAccountIds.isEmpty()) {
            try {
                bankAccountEditHistoryRepository.deleteByBankAccountIdIn(bankAccountIds);
                bankAccountRepository.deleteByCustomerIdIn(ids);
            } catch (Exception e) {
                throw new DeleteException("Delete fail");
            }
            throw new RuntimeException("Delete fail");
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

    @Override
    public PaginationRS<Customer> getListCustomerByAccount(Long accountId, SearchCustomerRQ searchRQ) {
        if (searchRQ == null) {
            searchRQ = new SearchCustomerRQ();
        }

        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + accountId));

        List<Long> customerConcernedIds = account.getCustomerConcerned();
        if (customerConcernedIds == null || customerConcernedIds.isEmpty()) {
            if (account.getPartnerConcerned() == null || account.getPartnerConcerned().isEmpty()) {
                customerConcernedIds = customerRepository.getListCustomerId();
            } else customerConcernedIds = Collections.emptyList();
        }

        Pageable pageable = PageRequest.of(
                searchRQ.getPage() != null ? searchRQ.getPage() : 0,
                searchRQ.getSize() != null ? searchRQ.getSize() : 10,
                Sort.by(Sort.Direction.fromString(
                                searchRQ.getSortDirection() != null ? searchRQ.getSortDirection() : "DESC"),
                        searchRQ.getSortBy() != null ? searchRQ.getSortBy() : "id")
        );

        Page<CustomerEntity> customerEntities = customerRepository.searchCustomers(
                searchRQ,
                searchRQ.getTerm(),
                customerConcernedIds,
                pageable
        );

        List<Customer> customers = customerEntities.getContent()
                .stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());

        PaginationRS<Customer> result = new PaginationRS<>();
        result.setContent(customers);
        result.setPageNumber(customerEntities.getNumber());
        result.setPageSize(customerEntities.getSize());
        result.setTotalElements(customerEntities.getTotalElements());
        result.setTotalPages(customerEntities.getTotalPages());

        return result;
    }


    @Override
    public PaginationRS<TransactionManage> getCustomerTransactionDetail(SearchTransactionManageRQ searchRQ, Long id) {
        if (searchRQ == null) {
            searchRQ = new SearchTransactionManageRQ();
        }

        Pageable pageable = PageRequest.of(
                searchRQ.getPage() != null ? searchRQ.getPage() : 0,
                searchRQ.getSize() != null ? searchRQ.getSize() : 10,
                Sort.by(Sort.Direction.fromString(
                                searchRQ.getSortDirection() != null ? searchRQ.getSortDirection() : "DESC"),
                        searchRQ.getSortBy() != null ? searchRQ.getSortBy() : "id")
        );


        Page<TransactionManageEntity> transactionManageEntities = transactionManageRepository.searchCustomerTransactions(
                id,
                searchRQ,
                searchRQ.getTerm(),
                pageable
        );

        List<TransactionManage> transactionManages = transactionManageEntities.getContent()
                .stream()
                .map(transactionManageMapper::toDTO)
                .collect(Collectors.toList());

        PaginationRS<TransactionManage> result = new PaginationRS<>();
        result.setContent(transactionManages);
        result.setPageNumber(transactionManageEntities.getNumber());
        result.setPageSize(transactionManageEntities.getSize());
        result.setTotalElements(transactionManageEntities.getTotalElements());
        result.setTotalPages(transactionManageEntities.getTotalPages());

        return result;
    }

}