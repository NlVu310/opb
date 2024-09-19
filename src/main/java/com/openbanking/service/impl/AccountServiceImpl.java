package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.comon.PaginationRS;
import com.openbanking.entity.AccountEntity;
import com.openbanking.entity.AccountTypeEntity;
import com.openbanking.entity.CustomerEntity;
import com.openbanking.entity.PartnerEntity;
import com.openbanking.exception.delete_exception.DeleteExceptionEnum;
import com.openbanking.exception.delete_exception.DeleteException;
import com.openbanking.exception.insert_exception.InsertExceptionEnum;
import com.openbanking.exception.insert_exception.InsertException;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionEnum;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundException;
import com.openbanking.mapper.AccountMapper;
import com.openbanking.mapper.AccountTypeMapper;
import com.openbanking.mapper.CustomerMapper;
import com.openbanking.mapper.PartnerMapper;
import com.openbanking.model.account.*;
import com.openbanking.model.auth.PasswordProperties;
import com.openbanking.model.customer.Customer;
import com.openbanking.model.partner.Partner;
import com.openbanking.repository.AccountRepository;
import com.openbanking.repository.AccountTypeRepository;
import com.openbanking.repository.CustomerRepository;
import com.openbanking.repository.PartnerRepository;
import com.openbanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl extends BaseServiceImpl<AccountEntity, Account, CreateAccount, UpdateAccount, Long> implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private PartnerMapper partnerMapper;

    @Autowired
    private AccountTypeMapper accountTypeMapper;

    @Autowired
    private PasswordProperties passwordProperties;


    public AccountServiceImpl(BaseRepository<AccountEntity, Long> repository, BaseMapper<AccountEntity, Account, CreateAccount, UpdateAccount> mapper) {
        super(repository, mapper);
    }

    @Override
    public Account getById(Long id) {
        AccountEntity accountEntity = accountRepository.findByIdAndDeletedAtNull(id);
        if (accountEntity == null) {
            throw new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_ACC,"with id " + id);
        }

        Account account = accountMapper.toDTO(accountEntity);

        if (accountEntity.getCustomerId() != null) {
            CustomerEntity customerEntity = customerRepository.findById(accountEntity.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_CUS,"with id " + accountEntity.getCustomerId()));
            account.setCustomer(customerMapper.toDTO(customerEntity));
        }

        if (accountEntity.getAccountTypeId() != null) {
            AccountTypeEntity accountTypeEntity = accountTypeRepository.findById(accountEntity.getAccountTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_ACC_TYPE, "with id " + accountEntity.getAccountTypeId()));
            account.setAccountType(accountTypeMapper.toDTO(accountTypeEntity));
        }

        if (accountEntity.getCustomerConcerned() != null && !accountEntity.getCustomerConcerned().isEmpty()) {
            List<CustomerEntity> customerEntities = customerRepository.findByIdIn(accountEntity.getCustomerConcerned());
            List<Customer> customer = customerEntities.stream()
                    .map(customerMapper::toDTO)
                    .collect(Collectors.toList());
            account.setCustomerConcerns(customer);
        }

        if (accountEntity.getPartnerConcerned() != null && !accountEntity.getPartnerConcerned().isEmpty()) {
            List<PartnerEntity> partnerEntities = partnerRepository.findByIdIn(accountEntity.getPartnerConcerned());
            List<Partner> partners = partnerEntities.stream()
                    .map(partnerMapper::toDTO)
                    .collect(Collectors.toList());
            account.setPartnerConcerns(partners);
        }
        return account;
    }


    @Override
    public Account create(CreateAccount dto, Long id) {
        List<String> usernames = accountRepository.findDistinctUsernames();

        if (usernames.contains(dto.getUsername())) {
            throw new InsertException(InsertExceptionEnum.INSERT_USER_NAME_ERROR, "");
        }

        AccountEntity account = accountMapper.toEntityFromCD(dto);

        String encodedPassword = passwordEncoder.encode(passwordProperties.getDefaultPassword());
        account.setPassword(encodedPassword);
        account.setIsChangedPassword(false);
        account.setCreatedBy(id);

        try {
            AccountEntity savedEntity = accountRepository.save(account);
            return accountMapper.toDTO(savedEntity);
        } catch (Exception e) {
            throw new InsertException(InsertExceptionEnum.INSERT_CRE_ACC_ERROR, e.getMessage());
        }
    }


    @Override
    @Transactional
    public void resetPassword(Long id) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_ACC, " with id: "  +id));

        try {
            String encodedPassword = passwordEncoder.encode(passwordProperties.getDefaultPassword());
            account.setPassword(encodedPassword);
            account.setIsChangedPassword(false);
            accountRepository.save(account);
        }catch (ResourceNotFoundException e){
            throw e;
        }
        catch (Exception e) {
            throw new InsertException(InsertExceptionEnum.INSERT_RES_ACC_ERROR, "");
        }
    }

    @Override
    public PaginationRS<Account> getAll(SearchAccountRQ rq) {
        Pageable pageable = PageRequest.of(
                (rq != null && rq.getPage() != null) ? rq.getPage() : 0,
                (rq != null && rq.getSize() != null) ? rq.getSize() : 10,
                Sort.Direction.fromString((rq != null && rq.getSortDirection() != null) ? rq.getSortDirection() : "DESC"),
                (rq != null && rq.getSortBy() != null) ? rq.getSortBy() : "id"
        );

        if (rq == null) {
            try {
                List<AccountEntity> allEntities = accountRepository.findAllByDeletedAtNull();
                List<Account> accounts = allEntities.stream()
                        .map(entity -> accountMapper.toDTO(entity))
                        .collect(Collectors.toList());

                PaginationRS<Account> response = new PaginationRS<>();
                response.setContent(accounts);
                response.setPageNumber(0);
                response.setPageSize(accounts.size());
                response.setTotalElements(accounts.size());
                response.setTotalPages(1);

                return response;
            } catch (Exception e) {
                throw new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_ACC_GET,"");
            }
        }
        String status = rq.getStatus() != null ? rq.getStatus().toString() : null;
        try {
            Page<AccountInfo> page = accountRepository.searchAccounts(
                    rq.getId(),
                    rq.getTerm(),
                    rq.getName(),
                    rq.getUserName(),
                    rq.getEmail(),
                    rq.getAccountTypeName(),
                    rq.getCustomerName(),
                    status,
                    rq.getCreatedBy(),
                    rq.getCreatedAt(),
                    pageable
            );

            return mapPageToPaginationRS(page);
        } catch (Exception e) {
            throw new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_ACC_SCH,"");
        }
    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        List<Long> createdByIds = accountRepository.findAllCreatedByIds();
        List<Long> invalidIds = ids.stream()
                .filter(createdByIds::contains)
                .collect(Collectors.toList());

        if (!invalidIds.isEmpty()) {
            throw new DeleteException(DeleteExceptionEnum.DELETE_ACC_REF_ERROR, "");
        }

        try {
            List<AccountEntity> accountEntities = accountRepository.findByIdIn(ids);
            if (accountEntities != null && !accountEntities.isEmpty()) {
                accountEntities.forEach(accountEntity -> accountEntity.setDeletedAt(OffsetDateTime.now()));
                try {
                    accountRepository.saveAll(accountEntities);
                } catch (Exception e) {
                    throw new DeleteException(DeleteExceptionEnum.DELETE_CUS_ERROR, "");
                }
            }
        } catch (DeleteException e) {
            System.err.println("An error occurred: " + e.getMessage());
            throw e;
        }
    }


    private PaginationRS<Account> mapPageToPaginationRS(Page<AccountInfo> page) {
        Map<Long, String> accountIdToNameMap = accountRepository.findAll().stream()
                .collect(Collectors.toMap(AccountEntity::getId, AccountEntity::getName));

        List<Account> accounts = page.getContent().stream()
                .map(accountInfo -> {
                    Account account = accountMapper.toDTOFromDetail(accountInfo);
                    String createdByName = accountIdToNameMap.get(accountInfo.getCreatedBy());
                    account.setCreatedByName(createdByName);
                    return account;
                })
                .collect(Collectors.toList());

        PaginationRS<Account> response = new PaginationRS<>();
        response.setContent(accounts);
        response.setPageNumber(page.getNumber() + 1);
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());

        return response;
    }

}
