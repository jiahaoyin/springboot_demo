package com.example.demo.controller;

import com.example.demo.common.R;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.SysUser;
import com.example.demo.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/user")
@RequiredArgsConstructor
public class SysUserController {

    private final ISysUserService userService;

    @GetMapping("/list")
    @PreAuthorize("hasPermission('system:user:list')")
    public R<List<SysUser>> list(UserDTO userDTO) {
        List<SysUser> list = userService.selectUserList(userDTO);
        return R.ok(list);
    }

    @PostMapping
    @PreAuthorize("hasPermission('system:user:add')")
    public R<Void> add(@Validated @RequestBody UserDTO user) {
        userService.insertUser(user);
        return R.ok();
    }

    @PutMapping
    @PreAuthorize("hasPermission('system:user:edit')")
    public R<Void> edit(@Validated @RequestBody UserDTO user) {
        userService.updateUser(user);
        return R.ok();
    }

    @DeleteMapping("/{userIds}")
    @PreAuthorize("hasPermission('system:user:remove')")
    public R<Void> remove(@PathVariable Long[] userIds) {
        userService.deleteUserByIds(userIds);
        return R.ok();
    }

    @PutMapping("/resetPwd")
    @PreAuthorize("hasPermission('system:user:resetPwd')")
    public R<Void> resetPassword(@RequestParam Long userId, @RequestParam String password) {
        userService.resetPassword(userId, password);
        return R.ok();
    }

    @PutMapping("/changeStatus")
    @PreAuthorize("hasPermission('system:user:edit')")
    public R<Void> changeStatus(@RequestParam Long userId, @RequestParam String status) {
        userService.updateUserStatus(userId, status);
        return R.ok();
    }
} 