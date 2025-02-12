package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.SysUser;
import com.example.demo.dto.UserDTO;

import java.util.List;

public interface ISysUserService extends IService<SysUser> {
    
    /**
     * 根据用户名查询用户
     */
    SysUser getByUsername(String username);
    
    /**
     * 查询用户列表
     */
    List<SysUser> selectUserList(UserDTO userDTO);
    
    /**
     * 新增用户
     */
    void insertUser(UserDTO user);
    
    /**
     * 修改用户
     */
    void updateUser(UserDTO user);
    
    /**
     * 删除用户
     */
    void deleteUserByIds(Long[] userIds);
    
    /**
     * 重置密码
     */
    void resetPassword(Long userId, String password);
    
    /**
     * 更新用户状态
     */
    void updateUserStatus(Long userId, String status);
} 