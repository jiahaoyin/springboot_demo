package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.SysMenu;
import com.example.demo.entity.SysUser;

import java.util.List;
import java.util.Set;

public interface ISysMenuService extends IService<SysMenu> {
    
    /**
     * 根据用户查询菜单权限
     */
    Set<String> getMenuPermission(SysUser user);
    
    /**
     * 根据用户ID查询菜单树信息
     */
    List<SysMenu> selectMenuTreeByUserId(Long userId);
    
    /**
     * 构建前端所需要的菜单树
     */
    List<SysMenu> buildMenuTree(List<SysMenu> menus);
    
    /**
     * 根据角色ID查询菜单树信息
     */
    List<Long> selectMenuListByRoleId(Long roleId);
} 