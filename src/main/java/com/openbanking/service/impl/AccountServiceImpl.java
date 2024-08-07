package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.comon.SearchCriteria;
import com.openbanking.entity.AccountEntity;
import com.openbanking.mapper.AccountMapper;
import com.openbanking.model.Account;
import com.openbanking.repository.AccountRepository;
import com.openbanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl extends BaseServiceImpl<AccountEntity, Account, Long> implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountMapper accountMapper;

    public AccountServiceImpl(BaseRepository<AccountEntity, Long> repository, BaseMapper<AccountEntity, Account> mapper) {
        super(repository, mapper);
    }

//    @Override
//    public List<Account> getAll(SearchCriteria searchCriteria){
//        if (searchCriteria == null || (searchCriteria.getTerms() == null && searchCriteria.getPage() == null && searchCriteria.getSize() == null)) {
//            return accountRepository.findAll().stream().map(accountMapper::toDTO).collect(Collectors.toList());
//        }
//
//        Pageable pageable = PageRequest.of(
//                searchCriteria.getPage() != null ? searchCriteria.getPage() : 0,
//                searchCriteria.getSize() != null ? searchCriteria.getSize() : 10,
//                Sort.Direction.fromString(searchCriteria.getSortDirection() != null ? searchCriteria.getSortDirection() : "ASC"),
//                searchCriteria.getSortBy() != null ? searchCriteria.getSortBy() : "id"
//        );
//
//        Specification<AccountEntity> spec = (root, query, builder) -> {
//            Predicate finalPredicate = builder.conjunction();
//
//            if (searchCriteria.getTerms() != null && !searchCriteria.getTerms().isEmpty()) {
//                Predicate[] keywordPredicates = searchCriteria.getTerms().stream()
//                        .map(keyword -> builder.like(builder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"))
//                        .toArray(Predicate[]::new);
//                finalPredicate = builder.and(finalPredicate, builder.or(keywordPredicates));
//            }
//
//            if (searchCriteria.getFromDate() != null) {
//                finalPredicate = builder.and(finalPredicate, builder.greaterThanOrEqualTo(root.get("createdAt"), searchCriteria.getFromDate()));
//            }
//
//            if (searchCriteria.getToDate() != null) {
//                finalPredicate = builder.and(finalPredicate, builder.lessThanOrEqualTo(root.get("createdAt"), searchCriteria.getToDate()));
//            }
//
//            return finalPredicate;
//        };
//
//        return accountRepository.findAll(spec, pageable).getContent().stream().map(accountMapper::toDTO).collect(Collectors.toList());
//    }
}
