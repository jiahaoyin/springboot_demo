package com.example.demo.controller;

import com.example.demo.common.R;
import com.example.demo.dto.DeptDTO;
import com.example.demo.entity.SysDept;
import com.example.demo.service.ISysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/dept")
@RequiredArgsConstructor
public class SysDeptController {

    private final ISysDeptService deptService;

    /**
     * 获取部门列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasPermission('system:dept:list')")
    public R<List<SysDept>> list(DeptDTO deptDTO) {
        List<SysDept> depts = deptService.selectDeptList(deptDTO);
        return R.ok(depts);
    }

    /**
     * 获取部门树形结构
     */
    @GetMapping("/treeselect")
    public R<List<SysDept>> treeselect(DeptDTO deptDTO) {
        List<SysDept> depts = deptService.selectDeptList(deptDTO);
        return R.ok(deptService.buildDeptTree(depts));
    }

    /**
     * 根据角色ID查询部门树结构
     */
    @GetMapping("/roleDeptTreeselect/{roleId}")
    public R<List<Long>> roleDeptTreeselect(@PathVariable Long roleId) {
        List<Long> deptIds = deptService.selectDeptListByRoleId(roleId);
        return R.ok(deptIds);
    }

    /**
     * 新增部门
     */
    @PostMapping
    @PreAuthorize("hasPermission('system:dept:add')")
    public R<Void> add(@Validated @RequestBody DeptDTO dept) {
        deptService.insertDept(dept);
        return R.ok();
    }

    /**
     * 修改部门
     */
    @PutMapping
    @PreAuthorize("hasPermission('system:dept:edit')")
    public R<Void> edit(@Validated @RequestBody DeptDTO dept) {
        deptService.updateDept(dept);
        return R.ok();
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{deptId}")
    @PreAuthorize("hasPermission('system:dept:remove')")
    public R<Void> remove(@PathVariable Long deptId) {
        deptService.deleteDeptById(deptId);
        return R.ok();
    }

    /**
     * 修改部门状态
     */
    @PutMapping("/changeStatus")
    @PreAuthorize("hasPermission('system:dept:edit')")
    public R<Void> changeStatus(@RequestParam Long deptId, @RequestParam String status) {
        deptService.updateDeptStatus(deptId, status);
        return R.ok();
    }
} 