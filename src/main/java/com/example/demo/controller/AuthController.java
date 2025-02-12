package com.example.demo.controller;

import com.example.demo.common.R;
import com.example.demo.security.model.LoginUser;
import com.example.demo.security.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/login")
    public R<String> login(@RequestParam String username, @RequestParam String password) {
        String token = authService.login(username, password);
        return R.ok(token);
    }

    @PostMapping("/logout")
    public R<Void> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            authService.logout(loginUser);
        }
        return R.ok();
    }

    @PostMapping("/refresh")
    public R<Void> refresh() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            authService.refreshToken(loginUser);
        }
        return R.ok();
    }
} 