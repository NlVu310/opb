package com.openbanking.service.impl;

import com.openbanking.entity.AccountEntity;
import com.openbanking.exception.AuthenticateException;
import com.openbanking.model.jwt.JwtTokenProvider;
import com.openbanking.model.login.LoginRQ;
import com.openbanking.model.login.LoginRS;
import com.openbanking.repository.AccountRepository;
import com.openbanking.service.AuthService;
import com.openbanking.service.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;

    @Override
    public LoginRS login(LoginRQ rq) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(rq.getUsername(), rq.getPassword())
            );
            String token = jwtTokenProvider.generateToken(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            AccountEntity account = accountRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userDetails.getUsername()));

            return new LoginRS(
                    token,
                    account.getId(),
                    account.getUsername(),
                    account.getName()
            );
        } catch (Exception e) {
            throw new AuthenticateException(e.getMessage());
        }
    }
}
