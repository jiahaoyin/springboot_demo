package com.example.demo.security.service;

import com.example.demo.security.model.LoginUser;

public interface IAuthService {
    
    String login(String username, String password);
    
    void logout(LoginUser loginUser);
    
    void refreshToken(LoginUser loginUser);
} 