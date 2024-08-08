package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.comon.PaginationRS;
import com.openbanking.entity.AccountEntity;
import com.openbanking.entity.AccountTypeEntity;
import com.openbanking.entity.CustomerEntity;
import com.openbanking.exception.ResourceNotFoundException;
import com.openbanking.mapper.AccountMapper;
import com.openbanking.model.account.Account;
import com.openbanking.model.account.CreateAccount;
import com.openbanking.model.account.SearchAccountRQ;
import com.openbanking.model.account.UpdateAccount;
import com.openbanking.repository.AccountRepository;
import com.openbanking.repository.AccountTypeRepository;
import com.openbanking.repository.CustomerRepository;
import com.openbanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
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

    private final String newPassword = "123123";

    public AccountServiceImpl(BaseRepository<AccountEntity, Long> repository, BaseMapper<AccountEntity, Account, CreateAccount, UpdateAccount> mapper) {
        super(repository, mapper);
    }
    @Override
    public Account getById(Long id) {
        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));

        Account account = accountMapper.toDTO(accountEntity);

        if (accountEntity.getCustomerId() != null) {
            CustomerEntity customerEntity = customerRepository.findById(accountEntity.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + accountEntity.getCustomerId()));
            account.setCustomerName(customerEntity.getName());
        }

        if (accountEntity.getAccountTypeId() != null) {
            AccountTypeEntity accountTypeEntity = accountTypeRepository.findById(accountEntity.getAccountTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account type not found with id " + accountEntity.getAccountTypeId()));
            account.setAccountTypeName(accountTypeEntity.getName());
        }

        return account;
    }


    @Override
    public Account create(CreateAccount dto, Long id) {
        List<String> usernames = accountRepository.findDistinctUsernames();
        if (usernames.contains(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        AccountEntity entity = accountMapper.toEntityFromCD(dto);
        entity.setCreatedBy(id);
        AccountEntity savedEntity = accountRepository.save(entity);
        return accountMapper.toDTO(savedEntity);
    }

    @Override
    @Transactional
    public void resetPassword(Long id) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        String encodedPassword = passwordEncoder.encode(newPassword);
        account.setPassword(encodedPassword);
        accountRepository.save(account);
    }

    @Override
    public PaginationRS<Account> getAll(SearchAccountRQ rq) {
        // Xác định Pageable dựa trên SearchAccountRQ hoặc giá trị mặc định nếu rq là null
        Pageable pageable = PageRequest.of(
                (rq != null && rq.getPage() != null) ? rq.getPage() : 0,
                (rq != null && rq.getSize() != null) ? rq.getSize() : 10,
                Sort.Direction.fromString((rq != null && rq.getSortDirection() != null) ? rq.getSortDirection() : "DESC"),
                (rq != null && rq.getSortBy() != null) ? rq.getSortBy() : "id"
        );

        // Xây dựng Specification chỉ khi rq không phải là null
        Specification<AccountEntity> spec = (root, query, builder) -> {
            Predicate finalPredicate = builder.conjunction();

            if (rq != null) {
                // Join với CustomerEntity và AccountTypeEntity
                Join<AccountEntity, CustomerEntity> customerJoin = root.join("customer", JoinType.LEFT);
                Join<AccountEntity, AccountTypeEntity> accountTypeJoin = root.join("accountType", JoinType.LEFT);

                // Điều kiện tìm kiếm từ rq
                if (rq.getId() != null) {
                    finalPredicate = builder.and(finalPredicate, builder.equal(root.get("id"), rq.getId()));
                }

                if (rq.getName() != null && !rq.getName().isEmpty()) {
                    finalPredicate = builder.and(finalPredicate, builder.like(builder.lower(root.get("name")), "%" + rq.getName().toLowerCase() + "%"));
                }

                if (rq.getUserName() != null && !rq.getUserName().isEmpty()) {
                    finalPredicate = builder.and(finalPredicate, builder.like(builder.lower(root.get("username")), "%" + rq.getUserName().toLowerCase() + "%"));
                }

                if (rq.getEmail() != null && !rq.getEmail().isEmpty()) {
                    finalPredicate = builder.and(finalPredicate, builder.like(builder.lower(root.get("email")), "%" + rq.getEmail().toLowerCase() + "%"));
                }

                if (rq.getAccountTypeName() != null && !rq.getAccountTypeName().isEmpty()) {
                    finalPredicate = builder.and(finalPredicate, builder.like(builder.lower(accountTypeJoin.get("name")), "%" + rq.getAccountTypeName().toLowerCase() + "%"));
                }

                if (rq.getCustomerName() != null && !rq.getCustomerName().isEmpty()) {
                    finalPredicate = builder.and(finalPredicate, builder.like(builder.lower(customerJoin.get("name")), "%" + rq.getCustomerName().toLowerCase() + "%"));
                }

                if (rq.getStatus() != null && !rq.getStatus().isEmpty()) {
                    finalPredicate = builder.and(finalPredicate, builder.like(builder.lower(root.get("status")), "%" + rq.getStatus().toLowerCase() + "%"));
                }

                if (rq.getCreatedBy() != null) {
                    finalPredicate = builder.and(finalPredicate, builder.equal(root.get("createdBy"), rq.getCreatedBy()));
                }

                if (rq.getCreatedAt() != null) {
                    finalPredicate = builder.and(finalPredicate, builder.equal(root.get("createdAt"), rq.getCreatedAt()));
                }

                // Tìm kiếm theo từ khóa
                if (rq.getTerm() != null && !rq.getTerm().isEmpty()) {
                    Predicate namePredicate = builder.like(builder.lower(root.get("name")), "%" + rq.getTerm().toLowerCase() + "%");
                    Predicate usernamePredicate = builder.like(builder.lower(root.get("username")), "%" + rq.getTerm().toLowerCase() + "%");
                    Predicate emailPredicate = builder.like(builder.lower(root.get("email")), "%" + rq.getTerm().toLowerCase() + "%");
                    Predicate statusPredicate = builder.like(builder.lower(root.get("status")), "%" + rq.getTerm().toLowerCase() + "%");
                    Predicate accountTypePredicate = builder.like(builder.lower(accountTypeJoin.get("name")), "%" + rq.getTerm().toLowerCase() + "%");
                    Predicate customerPredicate = builder.like(builder.lower(customerJoin.get("name")), "%" + rq.getTerm().toLowerCase() + "%");

                    finalPredicate = builder.and(finalPredicate, builder.or(namePredicate, usernamePredicate, emailPredicate, statusPredicate, accountTypePredicate, customerPredicate));
                }
            }

            return finalPredicate;
        };

        // Lấy dữ liệu từ repository, không cần áp dụng specification nếu rq là null
        Page<AccountEntity> page = (rq == null)
                ? accountRepository.findAll(pageable)
                : accountRepository.findAll(spec, pageable);

        List<Account> accounts = page.getContent().stream()
                .map(entity -> accountMapper.toDTO(entity))
                .collect(Collectors.toList());

        // Tạo đối tượng PaginationResponse
        PaginationRS<Account> response = new PaginationRS<>();
        response.setContent(accounts);
        response.setPageNumber(page.getNumber() + 1);
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());

        return response;
    }


}
