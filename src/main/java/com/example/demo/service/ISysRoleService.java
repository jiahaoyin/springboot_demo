package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.SysRole;
import com.example.demo.dto.RoleDTO;

import java.util.List;
import java.util.Set;

public interface ISysRoleService extends IService<SysRole> {
    
    /**
     * 根据用户ID查询角色列表
     */
    List<SysRole> selectRolesByUserId(Long userId);
    
    /**
     * 根据用户ID查询角色权限
     */
    Set<String> selectRolePermissionByUserId(Long userId);
    
    /**
     * 查询所有角色
     */
    List<SysRole> selectRoleList(RoleDTO role);
    
    /**
     * 新增角色
     */
    void insertRole(RoleDTO role);
    
    /**
     * 修改角色
     */
    void updateRole(RoleDTO role);
    
    /**
     * 修改角色状态
     */
    void updateRoleStatus(Long roleId, String status);
    
    /**
     * 批量删除角色
     */
    void deleteRoleByIds(Long[] roleIds);
} 