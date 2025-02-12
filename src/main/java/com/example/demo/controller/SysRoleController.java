package com.example.demo.controller;

import com.example.demo.common.R;
import com.example.demo.dto.RoleDTO;
import com.example.demo.entity.SysRole;
import com.example.demo.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final ISysRoleService roleService;

    /**
     * 获取角色列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasPermission('system:role:list')")
    public R<List<SysRole>> list(RoleDTO roleDTO) {
        List<SysRole> list = roleService.selectRoleList(roleDTO);
        return R.ok(list);
    }

    /**
     * 根据用户ID获取角色列表
     */
    @GetMapping("/listByUser/{userId}")
    public R<List<SysRole>> listByUser(@PathVariable Long userId) {
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        return R.ok(roles);
    }

    /**
     * 新增角色
     */
    @PostMapping
    @PreAuthorize("hasPermission('system:role:add')")
    public R<Void> add(@Validated @RequestBody RoleDTO role) {
        roleService.insertRole(role);
        return R.ok();
    }

    /**
     * 修改角色
     */
    @PutMapping
    @PreAuthorize("hasPermission('system:role:edit')")
    public R<Void> edit(@Validated @RequestBody RoleDTO role) {
        roleService.updateRole(role);
        return R.ok();
    }

    /**
     * 修改角色状态
     */
    @PutMapping("/changeStatus")
    @PreAuthorize("hasPermission('system:role:edit')")
    public R<Void> changeStatus(@RequestParam Long roleId, @RequestParam String status) {
        roleService.updateRoleStatus(roleId, status);
        return R.ok();
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{roleIds}")
    @PreAuthorize("hasPermission('system:role:remove')")
    public R<Void> remove(@PathVariable Long[] roleIds) {
        roleService.deleteRoleByIds(roleIds);
        return R.ok();
    }
} 