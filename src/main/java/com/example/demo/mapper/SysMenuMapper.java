package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    
    /**
     * 根据用户ID查询权限
     */
    List<String> selectMenuPermsByUserId(Long userId);
    
    /**
     * 查询系统菜单列表
     */
    List<SysMenu> selectMenuTreeAll();
    
    /**
     * 根据用户ID查询菜单
     */
    List<SysMenu> selectMenuTreeByUserId(Long userId);
    
    /**
     * 根据角色ID查询菜单树信息
     */
    List<Long> selectMenuListByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 根据角色ID查询菜单ID
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
} 