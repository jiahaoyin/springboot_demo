package com.example.demo.security.handler;

import com.example.demo.common.exception.ServiceException;
import com.example.demo.security.model.LoginUser;
import com.example.demo.security.utils.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("ph")
public class PermissionHandler {

    public boolean hasPermission(String permission) {
        if (!StringUtils.hasText(permission)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null || loginUser.getPermissions() == null) {
            return false;
        }
        return loginUser.getPermissions().contains(permission);
    }
} 