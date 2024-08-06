package com.openbanking.model.security;

import com.openbanking.model.jwt.JwtTokenProvider;
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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserPrincipal) principal;
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