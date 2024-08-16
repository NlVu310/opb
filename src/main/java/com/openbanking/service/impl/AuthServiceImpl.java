package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.AccountEntity;
import com.openbanking.enums.AccountStatus;
import com.openbanking.exception.AuthenticateException;
import com.openbanking.exception.ResourceNotFoundException;
import com.openbanking.exception.ValidationException;
import com.openbanking.mapper.AccountMapper;
import com.openbanking.model.account.Account;
import com.openbanking.model.account.CreateAccount;
import com.openbanking.model.account.UpdateAccount;
import com.openbanking.model.jwt.JwtTokenProvider;
import com.openbanking.model.login.LoginRQ;
import com.openbanking.model.login.LoginRS;
import com.openbanking.model.login.RegisterRQ;
import com.openbanking.repository.AccountRepository;
import com.openbanking.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl extends BaseServiceImpl<AccountEntity, Account, CreateAccount, UpdateAccount, Long> implements AuthService {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountMapper accountMapper;

    public AuthServiceImpl(BaseRepository<AccountEntity, Long> repository, BaseMapper<AccountEntity, Account, CreateAccount, UpdateAccount> mapper) {
        super(repository, mapper);
    }

    @Override
    public LoginRS login(LoginRQ rq) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(rq.getUsername(), rq.getPassword())
            );
            String token = jwtTokenProvider.generateToken(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            AccountEntity account = accountRepository.findByUsernameAndDeletedAtNull(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + userDetails.getUsername()));

            return new LoginRS(
                    token,
                    account.getId(),
                    account.getUsername(),
                    account.getName()
            );
        } catch (AuthenticateException e) {
            throw new AuthenticateException("Invalid username or password");
        } catch (Exception e) {
            throw new AuthenticateException("Login failed");
        }
    }

    @Override
    public Account register(RegisterRQ rq) {
        if (accountRepository.findByUsernameAndDeletedAtNull(rq.getUsername()).isPresent()) {
            throw new ValidationException("Username is already taken");
        }

        try {
            AccountEntity account = new AccountEntity();
            account.setUsername(rq.getUsername());
            account.setPassword(passwordEncoder.encode(rq.getPassword()));
            account.setName(rq.getName());
            account.setEmail(rq.getEmail());
            account.setPhone(rq.getPhone());
            account.setStatus(AccountStatus.ACTIVE);

            AccountEntity savedAccount = accountRepository.save(account);
            return accountMapper.toDTO(savedAccount);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register account", e);
        }
    }
}
