package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.comon.PaginationRS;
import com.openbanking.entity.AccountEntity;
import com.openbanking.entity.AccountTypeEntity;
import com.openbanking.entity.CustomerEntity;
import com.openbanking.exception.InvalidInputException;
import com.openbanking.exception.ResourceNotFoundException;
import com.openbanking.mapper.AccountMapper;
import com.openbanking.mapper.AccountTypeMapper;
import com.openbanking.mapper.CustomerMapper;
import com.openbanking.model.account.*;
import com.openbanking.repository.AccountRepository;
import com.openbanking.repository.AccountTypeRepository;
import com.openbanking.repository.CustomerRepository;
import com.openbanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private AccountTypeMapper accountTypeMapper;

    private final String newPassword = "123123";

    public AccountServiceImpl(BaseRepository<AccountEntity, Long> repository, BaseMapper<AccountEntity, Account, CreateAccount, UpdateAccount> mapper) {
        super(repository, mapper);
    }

    @Override
    public Account getById(Long id) {
        AccountEntity accountEntity = accountRepository.findByIdAndDeletedAtNull(id);
        if (accountEntity == null) {
            throw new ResourceNotFoundException("Account not found with id " + id);
        }

        Account account = accountMapper.toDTO(accountEntity);

        if (accountEntity.getCustomerId() != null) {
            CustomerEntity customerEntity = customerRepository.findById(accountEntity.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + accountEntity.getCustomerId()));
            account.setCustomer(customerMapper.toDTO(customerEntity));
        }

        if (accountEntity.getAccountTypeId() != null) {
            AccountTypeEntity accountTypeEntity = accountTypeRepository.findById(accountEntity.getAccountTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account type not found with id " + accountEntity.getAccountTypeId()));
            account.setAccountType(accountTypeMapper.toDTO(accountTypeEntity));
        }

        return account;
    }


    @Override
    public Account create(CreateAccount dto, Long id) {
        List<String> usernames = accountRepository.findDistinctUsernames();
        if (usernames.contains(dto.getUsername())) {
            throw new InvalidInputException("Username already exists");
        }

        AccountEntity entity = accountMapper.toEntityFromCD(dto);
        entity.setCreatedBy(id);

        try {
            AccountEntity savedEntity = accountRepository.save(entity);
            return accountMapper.toDTO(savedEntity);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while creating the account", e);
        }
    }

    @Override
    @Transactional
    public void resetPassword(Long id) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        try {
            String encodedPassword = passwordEncoder.encode(newPassword);
            account.setPassword(encodedPassword);
            accountRepository.save(account);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while resetting the password", e);
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
                throw new RuntimeException("An error occurred while fetching all accounts", e);
            }
        }

        try {
            Page<AccountInfo> page = accountRepository.searchAccounts(
                    rq.getId(),
                    rq.getName(),
                    rq.getUserName(),
                    rq.getEmail(),
                    rq.getAccountTypeName(),
                    rq.getCustomerName(),
                    rq.getStatus(),
                    rq.getCreatedBy(),
                    pageable
            );

            return mapPageToPaginationRS(page);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while searching accounts", e);
        }
    }


    private PaginationRS<Account> mapPageToPaginationRS(Page<AccountInfo> page) {
        List<Account> accounts = page.getContent().stream()
                .map(entity -> accountMapper.toDTOFromDetail(entity))
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
