package com.openbanking.controller;

import com.openbanking.comon.ResponseBuilder;
import com.openbanking.model.auth.*;
import com.openbanking.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseBuilder<?> login(@Valid @RequestBody LoginRQ rq) {
        var rs = authService.login(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @PostMapping("/register")
    public ResponseBuilder<?> register(@RequestBody RegisterRQ rq) {
        var rs = authService.register(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
    @PostMapping("/refresh-token")
    public ResponseBuilder<LoginRS> refreshToken(@RequestBody RefreshTokenRQ rq) {
        var rs = authService.refreshToken(rq.getRefreshToken());
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
    @PostMapping("/change-password")
    public ResponseBuilder<?> changePassword(@Valid @RequestBody ChangePasswordRQ rq) {
        authService.changePassword(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", "Password changed successfully");
    }
}
