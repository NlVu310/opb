package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.AccountEntity;
import com.openbanking.enums.AccountStatus;
import com.openbanking.exception.AuthenticateException;
import com.openbanking.exception.ChangePasswordException;
import com.openbanking.exception.ResourceNotFoundException;
import com.openbanking.exception.ValidationException;
import com.openbanking.mapper.AccountMapper;
import com.openbanking.model.account.Account;
import com.openbanking.model.account.CreateAccount;
import com.openbanking.model.account.UpdateAccount;
import com.openbanking.model.auth.*;
import com.openbanking.model.jwt.JwtTokenProvider;
import com.openbanking.repository.AccountRepository;
import com.openbanking.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    @Autowired
    private PasswordProperties passwordProperties;

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
            String refreshToken = jwtTokenProvider.generateToken(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            AccountEntity account = accountRepository.findByUsernameAndDeletedAtNull(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + userDetails.getUsername()));

            return new LoginRS(
                    token,
                    refreshToken,
                    account.getId(),
                    account.getUsername(),
                    account.getName(),
                    account.getIsChangedPassword()
            );
        } catch (AuthenticateException e) {
            throw new AuthenticateException("Invalid username or password");
        }
        catch (BadCredentialsException e) {
            throw new AuthenticateException("Username or password incorrect");
        }
        catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public LoginRS refreshToken(String refreshToken) {
        try {
            String newToken = jwtTokenProvider.generateTokenFromRefreshToken(refreshToken);
            String username = jwtTokenProvider.getUsernameFromJWT(newToken);
            AccountEntity account = accountRepository.findByUsernameAndDeletedAtNull(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

            return new LoginRS(
                    newToken,
                    refreshToken,
                    account.getId(),
                    account.getUsername(),
                    account.getName(),
                    account.getIsChangedPassword()
            );
        } catch (Exception e) {
            throw new AuthenticateException("Invalid refresh token");
        }
    }
    @Override
    public void changePassword(ChangePasswordRQ rq) {
        if (rq.getNewPassword().equals(passwordProperties.getDefaultPassword()))
            throw new ChangePasswordException("New password must be different from default password");

        if (!rq.getNewPassword().equals(rq.getReEnterNewPassword()))
            throw new ChangePasswordException("New password is not the same as re-enter new password");

        AccountEntity accountEntity = accountRepository.findByIdAndDeletedAtNull(rq.getId());
        if (accountEntity == null) {
            throw new ResourceNotFoundException("Account not found with id " + rq.getId());
        }

        accountEntity.setPassword(passwordEncoder.encode(rq.getNewPassword()));
        accountEntity.setIsChangedPassword(true);
        accountRepository.save(accountEntity);
    }
}
