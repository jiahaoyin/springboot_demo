package com.example.demo.security.service;

import com.example.demo.entity.SysUser;
import com.example.demo.security.model.LoginUser;
import com.example.demo.service.ISysMenuService;
import com.example.demo.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ISysUserService userService;
    private final ISysMenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        
        // 获取用户权限列表
        Set<String> permissions = menuService.getMenuPermission(user);
        
        return new LoginUser(user, permissions);
    }
} 