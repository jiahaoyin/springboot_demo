package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.SysDept;
import com.example.demo.dto.DeptDTO;

import java.util.List;

public interface ISysDeptService extends IService<SysDept> {
    
    /**
     * 查询部门列表
     */
    List<SysDept> selectDeptList(DeptDTO dept);
    
    /**
     * 构建前端所需要的部门树结构
     */
    List<SysDept> buildDeptTree(List<SysDept> depts);
    
    /**
     * 根据角色ID查询部门树信息
     */
    List<Long> selectDeptListByRoleId(Long roleId);
    
    /**
     * 新增部门
     */
    void insertDept(DeptDTO dept);
    
    /**
     * 修改部门
     */
    void updateDept(DeptDTO dept);
    
    /**
     * 删除部门
     */
    void deleteDeptById(Long deptId);
    
    /**
     * 修改部门状态
     */
    void updateDeptStatus(Long deptId, String status);
} 