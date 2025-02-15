package com.openbanking.service.impl;

import com.openbanking.comon.BaseMapper;
import com.openbanking.comon.BaseRepository;
import com.openbanking.comon.BaseServiceImpl;
import com.openbanking.entity.AccountEntity;
import com.openbanking.entity.AccountTypeEntity;
import com.openbanking.entity.PermissionEntity;
import com.openbanking.enums.AccountStatus;
import com.openbanking.exception.authen_exception.AuthenExceptionEnum;
import com.openbanking.exception.authen_exception.AuthenException;
import com.openbanking.exception.base_exception.ValidationException;
import com.openbanking.exception.insert_exception.InsertExceptionEnum;
import com.openbanking.exception.insert_exception.InsertException;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundExceptionEnum;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundException;
import com.openbanking.mapper.AccountMapper;
import com.openbanking.mapper.PermissionMapper;
import com.openbanking.model.account.Account;
import com.openbanking.model.account.CreateAccount;
import com.openbanking.model.account.UpdateAccount;
import com.openbanking.model.auth.*;
import com.openbanking.model.jwt.JwtTokenProvider;
import com.openbanking.model.permission.Permission;
import com.openbanking.repository.AccountRepository;
import com.openbanking.repository.AccountTypeRepository;
import com.openbanking.repository.PermissionRepository;
import com.openbanking.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    private PermissionRepository permissionRepository;
    @Autowired
    private PermissionMapper permissionMapper;
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
                    .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_USER , "with username: " + userDetails.getUsername()));
            List<PermissionEntity> permissionEntities = permissionRepository.getPermissionsByAccountId(account.getId());
            List<Permission> permissions = permissionEntities.stream().map(permissionMapper::toDTO).collect(Collectors.toList());

            return new LoginRS(
                    token,
                    refreshToken,
                    account.getId(),
                    account.getUsername(),
                    account.getName(),
                    account.getIsChangedPassword(),
                    permissions
            );
        } catch(ResourceNotFoundException | AuthenException e){
            throw e;
        } catch (BadCredentialsException e) {
            throw new AuthenException(AuthenExceptionEnum.AUTH_CHECK_ERROR ,"");
        }
        catch (Exception e) {
            throw new AuthenException(AuthenExceptionEnum.AUTH_LOG_ERROR, "");
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
            throw new InsertException( InsertExceptionEnum.INSERT_ACC_ERROR,"" +e.getMessage());
        }
    }

    @Override
    public LoginRS refreshToken(String refreshToken) {
        try {
            String newToken = jwtTokenProvider.generateTokenFromRefreshToken(refreshToken);
            String username = jwtTokenProvider.getUsernameFromJWT(newToken);
            AccountEntity account = accountRepository.findByUsernameAndDeletedAtNull(username)
                    .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_USER ,"with username: " + username));
            List<PermissionEntity> permissionEntities = permissionRepository.getPermissionsByAccountId(account.getId());
            List<Permission> permissions = permissionEntities.stream().map(permissionMapper::toDTO).collect(Collectors.toList());
            return new LoginRS(
                    newToken,
                    refreshToken,
                    account.getId(),
                    account.getUsername(),
                    account.getName(),
                    account.getIsChangedPassword(),
                    permissions
            );
        } catch (ResourceNotFoundException e){
            throw e;
        }catch (AuthenException e){
            throw new AuthenException(AuthenExceptionEnum.AUTH_REF_ERROR, "");
        }
    }
    @Override
    public void changePassword(ChangePasswordRQ rq) {
        if (rq.getNewPassword().equals(passwordProperties.getDefaultPassword()))
            throw new AuthenException(AuthenExceptionEnum.AUTH_PASS_DEF_ERROR,"");

        if (!rq.getNewPassword().equals(rq.getReEnterNewPassword()))
            throw new AuthenException(AuthenExceptionEnum.AUTH_PASS_NEW_ERROR ,"");

        AccountEntity accountEntity = accountRepository.findByIdAndDeletedAtNull(rq.getId());
        if (accountEntity == null) {
            throw new ResourceNotFoundException(ResourceNotFoundExceptionEnum.RNF_ACC,"with id " + rq.getId());
        }

        accountEntity.setPassword(passwordEncoder.encode(rq.getNewPassword()));
        accountEntity.setIsChangedPassword(true);
        accountRepository.save(accountEntity);
    }
}
