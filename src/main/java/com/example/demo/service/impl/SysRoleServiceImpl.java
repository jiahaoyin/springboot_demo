package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.SysRole;
import com.example.demo.dto.RoleDTO;
import com.example.demo.mapper.SysRoleMapper;
import com.example.demo.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        return baseMapper.selectRolesByUserId(userId);
    }

    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<String> perms = baseMapper.selectRolePermissionByUserId(userId);
        return new HashSet<>(perms);
    }

    @Override
    public List<SysRole> selectRoleList(RoleDTO roleDTO) {
        return list(new LambdaQueryWrapper<SysRole>()
                .like(roleDTO.getRoleName() != null, SysRole::getRoleName, roleDTO.getRoleName())
                .eq(roleDTO.getStatus() != null, SysRole::getStatus, roleDTO.getStatus())
                .eq(SysRole::getDelFlag, "0")
                .orderByAsc(SysRole::getRoleSort));
    }

    @Override
    @Transactional
    public void insertRole(RoleDTO roleDTO) {
        SysRole role = new SysRole();
        role.setRoleName(roleDTO.getRoleName());
        role.setRoleKey(roleDTO.getRoleKey());
        role.setRoleSort(roleDTO.getRoleSort());
        role.setDataScope(roleDTO.getDataScope());
        role.setStatus(roleDTO.getStatus());
        role.setDelFlag("0");
        
        save(role);
        
        if (roleDTO.getMenuIds() != null) {
            baseMapper.insertRoleMenu(role.getRoleId(), roleDTO.getMenuIds());
        }
        if (roleDTO.getDeptIds() != null) {
            baseMapper.insertRoleDept(role.getRoleId(), roleDTO.getDeptIds());
        }
    }

    @Override
    @Transactional
    public void updateRole(RoleDTO roleDTO) {
        SysRole role = getById(roleDTO.getRoleId());
        role.setRoleName(roleDTO.getRoleName());
        role.setRoleKey(roleDTO.getRoleKey());
        role.setRoleSort(roleDTO.getRoleSort());
        role.setDataScope(roleDTO.getDataScope());
        role.setStatus(roleDTO.getStatus());
        
        updateById(role);
        
        // 更新角色菜单关系
        baseMapper.deleteRoleMenuByRoleIds(new Long[]{role.getRoleId()});
        if (roleDTO.getMenuIds() != null) {
            baseMapper.insertRoleMenu(role.getRoleId(), roleDTO.getMenuIds());
        }
        
        // 更新角色部门关系
        baseMapper.deleteRoleDeptByRoleIds(new Long[]{role.getRoleId()});
        if (roleDTO.getDeptIds() != null) {
            baseMapper.insertRoleDept(role.getRoleId(), roleDTO.getDeptIds());
        }
    }

    @Override
    public void updateRoleStatus(Long roleId, String status) {
        SysRole role = getById(roleId);
        role.setStatus(status);
        updateById(role);
    }

    @Override
    @Transactional
    public void deleteRoleByIds(Long[] roleIds) {
        // 删除角色与菜单关联
        baseMapper.deleteRoleMenuByRoleIds(roleIds);
        // 删除角色与部门关联
        baseMapper.deleteRoleDeptByRoleIds(roleIds);
        // 删除角色
        for (Long roleId : roleIds) {
            SysRole role = getById(roleId);
            role.setDelFlag("1");
            updateById(role);
        }
    }
} 