package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {
    
    /**
     * 根据角色ID查询部门树信息
     */
    List<Long> selectDeptListByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 根据ID查询所有子部门
     */
    List<SysDept> selectChildrenDeptById(Long deptId);
    
    /**
     * 修改子元素关系
     */
    void updateDeptChildren(@Param("depts") List<SysDept> depts);
    
    /**
     * 查询部门是否存在用户
     */
    int checkDeptExistUser(Long deptId);
} 