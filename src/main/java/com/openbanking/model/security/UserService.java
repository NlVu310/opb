package com.openbanking.model.security;

import com.openbanking.entity.AccountEntity;
import com.openbanking.model.jwt.JwtTokenProvider;
import com.openbanking.service.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private JwtTokenProvider tokenProvider;

    public UserPrincipal getCurrentUser() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof UserDetails) {
            UserDetailsImpl userDetail = (UserDetailsImpl) user;
            AccountEntity accountEntity = userDetail.getAccount();
            UserPrincipal userPrincipal = new UserPrincipal().setId(accountEntity.getId()).
                    setName(accountEntity.getName())
                    .setUsername(accountEntity.getUsername())
                    .setPassword(accountEntity.getPassword())
                    .setAuthorities(userDetail.getAuthorities());
            return userPrincipal;

        }
        return null;
    }

    public String getCurrentUserToken() {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (tokenProvider.validateToken(token)) {
                return token;
            }
        }
        return null;
    }
}