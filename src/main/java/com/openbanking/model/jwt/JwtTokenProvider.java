package com.openbanking.model.jwt;

import com.openbanking.exception.authen_exception.AuthenExceptionEnum;
import com.openbanking.exception.authen_exception.AuthenExceptionService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {
    private final String JWT_SECRET = "ApecQuyetTam";
    public static final long JWT_EXPIRATION = 604800000L;

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("username").toString();
        } catch (Exception e) {
            throw new AuthenExceptionService(AuthenExceptionEnum.AUTH_TOK_ERROR, "");
        }
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            throw new AuthenExceptionService(AuthenExceptionEnum.AUTH_SIG_ERROR, "");
        } catch (Exception e) {
            throw new AuthenExceptionService(AuthenExceptionEnum.AUTH_TOK_ERROR, "");
        }
    }

    public String generateTokenFromRefreshToken(String refreshToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(refreshToken)
                    .getBody();

            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                    .compact();
        } catch (Exception e) {
            throw new AuthenExceptionService(AuthenExceptionEnum.AUTH_REF_ERROR, "");
        }
    }

}
