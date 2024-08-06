package com.openbanking.controller;

import com.openbanking.comon.ResponseBuilder;
import com.openbanking.model.login.LoginRQ;
import com.openbanking.model.login.RegisterRQ;
import com.openbanking.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseBuilder<?> login(@RequestBody LoginRQ rq) {
        var rs = authService.login(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }

    @PostMapping("/register")
    public ResponseBuilder<?> register(@RequestBody RegisterRQ rq) {
        var rs = authService.register(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}
