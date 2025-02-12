package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    
    /**
     * 根据用户ID查询角色
     */
    List<SysRole> selectRolesByUserId(Long userId);
    
    /**
     * 根据用户ID查询角色权限
     */
    List<String> selectRolePermissionByUserId(Long userId);
    
    /**
     * 批量删除角色菜单关联
     */
    void deleteRoleMenuByRoleIds(@Param("roleIds") Long[] roleIds);
    
    /**
     * 批量删除角色部门关联
     */
    void deleteRoleDeptByRoleIds(@Param("roleIds") Long[] roleIds);
    
    /**
     * 新增角色菜单关联
     */
    void insertRoleMenu(@Param("roleId") Long roleId, @Param("menuIds") Long[] menuIds);
    
    /**
     * 新增角色部门关联
     */
    void insertRoleDept(@Param("roleId") Long roleId, @Param("deptIds") Long[] deptIds);
} 