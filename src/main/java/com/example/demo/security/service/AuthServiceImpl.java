package com.example.demo.security.service;

import com.example.demo.security.model.LoginUser;
import com.example.demo.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public String login(String username, String password) {
        // 用户验证
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );
        
        // 生成令牌
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return jwtUtils.generateToken(loginUser);
    }

    @Override
    public void logout(LoginUser loginUser) {
        // 可以在这��实现登出逻辑，比如将token加入黑名单等
    }

    @Override
    public void refreshToken(LoginUser loginUser) {
        // 可以在这里实现刷新token的逻辑
    }
} 