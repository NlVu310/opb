package com.openbanking.service.security;

import com.openbanking.entity.AccountEntity;
import com.openbanking.entity.AccountTypePermissionEntity;
import com.openbanking.exception.authen_exception.AuthenExceptionEnum;
import com.openbanking.exception.authen_exception.AuthenException;
import com.openbanking.repository.AccountRepository;
import com.openbanking.repository.AccountTypePermissionRepository;
import com.openbanking.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final AccountTypePermissionRepository accountTypePermissionRepository;
    private final PermissionRepository permissionRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountEntity account = accountRepository.findByUsernameAndDeletedAtNull(username)
                .orElseThrow(() -> new AuthenException(AuthenExceptionEnum.AUTH_TOK_ERROR ,"with username: " + username));
        List<GrantedAuthority> authorities = Collections.emptyList();

        if (account.getAccountTypeId() != null) {
            List<Long> permissionIds = accountTypePermissionRepository.findByAccountTypeId(account.getAccountTypeId())
                    .stream()
                    .map(AccountTypePermissionEntity::getPermissionId)
                    .collect(Collectors.toList());

            authorities = permissionRepository.findAllById(permissionIds)
                    .stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getCode()))
                    .collect(Collectors.toList());
        }

        return new UserDetailsImpl(account, authorities);
    }
}