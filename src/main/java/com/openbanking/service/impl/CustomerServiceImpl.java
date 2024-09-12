package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.comon.PaginationRS;
import com.openbanking.entity.*;
import com.openbanking.exception.delete_exception.DeleteExceptionEnum;
import com.openbanking.exception.delete_exception.DeleteExceptionService;
import com.openbanking.exception.insert_exception.InsertExceptionEnum;
import com.openbanking.exception.insert_exception.InsertExceptionService;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionEnum;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionService;
import com.openbanking.mapper.BankAccountMapper;
import com.openbanking.mapper.CustomerMapper;
import com.openbanking.mapper.TransactionManageMapper;
import com.openbanking.model.bank_account.BankAccount;
import com.openbanking.model.bank_account.BankAccountProjection;
import com.openbanking.model.bank_account.CreateBankAccount;
import com.openbanking.model.bank_account.UpdateBankAccount;
import com.openbanking.model.customer.*;
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
import java.time.LocalDate;
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
    @Transactional
    public void create(CreateCustomer createCustomer) {
        try {
            if (customerRepository.existsByTaxNoAndDeletedAtIsNull(createCustomer.getTaxNo())) {
                throw new InsertExceptionService(InsertExceptionEnum.INSERT_CUSTOMER_ERROR,"Tax exists.");
            }

            CustomerEntity customerEntity = customerMapper.toEntityFromCD(createCustomer);
            customerRepository.save(customerEntity);

            List<CreateBankAccount> bankAccountList = createCustomer.getBankAccountList();
            validateBankAccountDateRanges(bankAccountList);

            for (CreateBankAccount bankAccount : bankAccountList) {
                if (bankAccountRepository.existsByPartnerIdAndAccountNumber(bankAccount.getPartnerId(), bankAccount.getAccountNumber())) {
                    throw new InsertExceptionService(InsertExceptionEnum.INSERT_BANK_ACC_ERROR, "" );
                }
            }

            List<BankAccountEntity> bankAccountEntities = bankAccountList.stream()
                    .map(dtoItem -> {
                        BankAccountEntity entity = bankAccountMapper.toEntityFromCD(dtoItem);
                        if (entity != null) {
                            entity.setCustomerId(customerEntity.getId());
                            entity.setStatus(bankAccountService.determineStatus(entity, LocalDate.now()));
                        }
                        return entity;
                    })
                    .collect(Collectors.toList());
                bankAccountRepository.saveAll(bankAccountEntities);

        }  catch (InsertExceptionService e) {
            throw e;
        }catch (Exception e){
            throw new InsertExceptionService(InsertExceptionEnum.INSERT_CUSTOMER_ERROR, "");
        }
    }

    private void validateBankAccountDateRanges(List<? extends BankAccountProjection> bankAccountList) {
        try{
            for (int i = 0; i < bankAccountList.size(); i++) {
                BankAccountProjection bankAccount = bankAccountList.get(i);
                OffsetDateTime fromDate = bankAccount.getFromDate();
                OffsetDateTime toDate = bankAccount.getToDate();

                if (fromDate == null) {
                    throw new InsertExceptionService(InsertExceptionEnum.INSERT_FROM_DATE_ERROR, "");
                }

                for (int j = i + 1; j < bankAccountList.size(); j++) {
                    BankAccountProjection bankAccountCompare = bankAccountList.get(j);
                    OffsetDateTime fromDateCompare = bankAccountCompare.getFromDate();
                    OffsetDateTime toDateCompare = bankAccountCompare.getToDate();
                    if (areAccountsIdentical(bankAccount, bankAccountCompare)) {
                        if (toDateCompare == null) {
                            if (fromDateCompare.isBefore(toDate)) {
                                throw new InsertExceptionService(InsertExceptionEnum.INSERT_DATE_RANGE_ERROR, "FromDate cannot be within the range of another account with a null end date.");
                            }
                        } else {
                            if (isOverlap(fromDate, toDate, fromDateCompare, toDateCompare)) {
                                throw new InsertExceptionService(InsertExceptionEnum.INSERT_DATE_RANGE_ERROR, "");
                            }
                        }
                    }
                }
            }
        }catch (InsertExceptionService e){
            throw e;
        }
    }

    private boolean isOverlap(OffsetDateTime fromDate, OffsetDateTime toDate, OffsetDateTime fromDateCompare, OffsetDateTime toDateCompare) {
        if (toDate == null) {
            toDate = OffsetDateTime.MAX;
        }
        if (toDateCompare == null) {
            toDateCompare = OffsetDateTime.MAX;
        }
        return fromDate.isBefore(toDateCompare) && fromDateCompare.isBefore(toDate);
    }




    private boolean areAccountsIdentical(BankAccountProjection bankAccountProjection, BankAccountProjection bankAccountProjectionCompare) {
        return bankAccountProjection.getPartnerId().equals(bankAccountProjectionCompare.getPartnerId()) &&
                bankAccountProjection.getAccountNumber().equals(bankAccountProjectionCompare.getAccountNumber()) &&
                bankAccountProjection.getSourceId().equals(bankAccountProjectionCompare.getSourceId());
    }


    @Override
    @Transactional
    public void update(UpdateCustomer updateCustomer) {
        try {
            CustomerEntity customerEntity = customerRepository.findById(updateCustomer.getId())
                    .orElseThrow(() -> new ResourceNotFoundExceptionService(ResourceNotFoundExceptionEnum.RNF_CUS, "with id " + updateCustomer.getId()));

            if (customerRepository.existsByTaxNoAndIdNotAndDeletedAtIsNull(updateCustomer.getTaxNo(), updateCustomer.getId())) {
                throw new InsertExceptionService(InsertExceptionEnum.INSERT_CUSTOMER_ERROR,"Tax exists.");
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
            List<UpdateBankAccount> updateBankAccounts = updateCustomer.getListUpdateBankAccounts();

            validateBankAccountDateRanges(updateBankAccounts);

            for (UpdateBankAccount updateBankAccount : updateBankAccounts) {
                Long bankAccountId = updateBankAccount.getId();
                boolean isNew = !bankAccountIdSet.contains(bankAccountId);

                String newPair = updateBankAccount.getPartnerId() + "-" + updateBankAccount.getAccountNumber();

                boolean isDuplicateWithOtherCustomer = bankAccountRepository.existsByPartnerIdAndAccountNumberAndCustomerIdNot(updateBankAccount.getPartnerId(), updateBankAccount.getAccountNumber(), updateCustomer.getId());

                if (isDuplicateWithOtherCustomer) {
                    throw new InsertExceptionService(InsertExceptionEnum.INSERT_BANK_ACC_ERROR, "" );
                }

                if (isNew) {
                    BankAccountEntity newEntity = bankAccountMapper.getEntity(updateBankAccount);
                    newEntity.setCustomerId(updateCustomer.getId());
                    newEntity.setStatus(bankAccountService.determineStatus(newEntity, LocalDate.now()));
                    bankAccountsToSave.add(newEntity);
                } else {
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
                }
            }

            if (!bankAccountsToUpdate.isEmpty()) {
                for (BankAccountEntity bankAccount : bankAccountsToUpdate) {
                    bankAccount.setStatus(bankAccountService.determineStatus(bankAccount, LocalDate.now()));
                }
                bankAccountRepository.saveAll(bankAccountsToUpdate);
            }
            if (!bankAccountsToSave.isEmpty()) {
                bankAccountRepository.saveAll(bankAccountsToSave);
            }
            if (!historyEntities.isEmpty()) {
                bankAccountEditHistoryRepository.saveAll(historyEntities);
            }

        }catch (ResourceNotFoundExceptionService e){
            throw  e;
        }catch (InsertExceptionService e){
            throw e;
        }
        catch (Exception e) {
            throw new ResourceNotFoundExceptionService(ResourceNotFoundExceptionEnum.RNF_UDP_CUS, "");
        }
    }



    @Override
    public CustomerDetail getCustomerDetail(Long id) {
        try {
            CustomerEntity customerEntity = customerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundExceptionService( ResourceNotFoundExceptionEnum.RNF_CUS,"with id " + id));
            CustomerDetail customerDetail = customerMapper.toDetail(customerEntity);
            List<BankAccountEntity> bankAccountEntities = bankAccountRepository.getListBankAccountByCustomerId(id);
            List<BankAccount> bankAccounts = bankAccountMapper.toDTOs(bankAccountEntities);
            customerDetail.setBankAccounts(bankAccounts);
            return customerDetail;
        } catch (ResourceNotFoundExceptionService e) {
            throw e;
        }
        catch(Exception e) {
            throw new ResourceNotFoundExceptionService(ResourceNotFoundExceptionEnum.RNF_CUS, "");
        }
    }



    @Override
    @Transactional
    public void deleteByListId(List<Long> ids) {
        try {
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
                throw new DeleteExceptionService(DeleteExceptionEnum.DELETE_TRANS_CONTENT_ERROR, "");
            }
            if (!transactionManages.isEmpty()) {
                throw new DeleteExceptionService(DeleteExceptionEnum.DELETE_TRANS_ERROR, "");
            }
            if (accountIds != null && !accountIds.isEmpty()) {
                throw new DeleteExceptionService(DeleteExceptionEnum.DELETE_ACC_ERROR, "");
            }
            if (CustomerConcerned != null && !CustomerConcerned.isEmpty()) {
                throw new DeleteExceptionService(DeleteExceptionEnum.DELETE_CUS_CON_ERROR, "");
            }
            if (bankAccountIds != null && !bankAccountIds.isEmpty()) {
                try {
                    bankAccountEditHistoryRepository.deleteByBankAccountIdIn(bankAccountIds);
                    bankAccountRepository.deleteByCustomerIdIn(ids);
                } catch (Exception e) {
                    throw new DeleteExceptionService(DeleteExceptionEnum.DELETE_BANK_ACC_ERROR, "");
                }
            }

            List<CustomerEntity> customerEntities = customerRepository.findByIdIn(ids);
            if (customerEntities != null && !customerEntities.isEmpty()) {
                customerEntities.forEach(customerEntity -> customerEntity.setDeletedAt(OffsetDateTime.now()));
                try {
                    customerRepository.saveAll(customerEntities);
                } catch (Exception e) {
                    throw new DeleteExceptionService(DeleteExceptionEnum.DELETE_CUS_ERROR, "");
                }
            }
        }catch (DeleteExceptionService e){
            throw e;
        }
    }

    @Override
    public PaginationRS<Customer> getListCustomerByAccount(Long accountId, SearchCustomerRQ searchRQ) {
        if (searchRQ == null) {
            searchRQ = new SearchCustomerRQ();
        }

        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundExceptionService(  ResourceNotFoundExceptionEnum.RNF_ACC,"with id " + accountId));

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
    public List<Customer> getListParentCustomers() {
        try {
            List<CustomerEntity> customerEntities = customerRepository.getListParentCustomers();
            List<Customer> customers = customerMapper.toDTOs(customerEntities);
            return customers;
        } catch (Exception e) {
            throw new ResourceNotFoundExceptionService(ResourceNotFoundExceptionEnum.RNF_PAR_CUS , "");
        }
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
                searchRQ.getTransactionDate(),
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