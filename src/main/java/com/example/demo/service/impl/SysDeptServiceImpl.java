package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.exception.ServiceException;
import com.example.demo.entity.SysDept;
import com.example.demo.dto.DeptDTO;
import com.example.demo.mapper.SysDeptMapper;
import com.example.demo.service.ISysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Override
    public List<SysDept> selectDeptList(DeptDTO deptDTO) {
        return list(new LambdaQueryWrapper<SysDept>()
                .like(deptDTO.getDeptName() != null, SysDept::getDeptName, deptDTO.getDeptName())
                .eq(deptDTO.getStatus() != null, SysDept::getStatus, deptDTO.getStatus())
                .eq(SysDept::getDelFlag, "0")
                .orderByAsc(SysDept::getParentId, SysDept::getOrderNum));
    }

    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<>();
        List<Long> tempList = depts.stream().map(SysDept::getDeptId).collect(Collectors.toList());
        for (SysDept dept : depts) {
            // 如果是顶级节点，遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    @Override
    public List<Long> selectDeptListByRoleId(Long roleId) {
        return baseMapper.selectDeptListByRoleId(roleId);
    }

    @Override
    @Transactional
    public void insertDept(DeptDTO deptDTO) {
        SysDept dept = new SysDept();
        dept.setDeptName(deptDTO.getDeptName());
        dept.setParentId(deptDTO.getParentId());
        dept.setOrderNum(deptDTO.getOrderNum());
        dept.setLeader(deptDTO.getLeader());
        dept.setPhone(deptDTO.getPhone());
        dept.setEmail(deptDTO.getEmail());
        dept.setStatus(deptDTO.getStatus());
        dept.setDelFlag("0");
        
        if (dept.getParentId() != 0) {
            SysDept parent = getById(dept.getParentId());
            dept.setAncestors(parent.getAncestors() + "," + dept.getParentId());
        } else {
            dept.setAncestors("0");
        }
        save(dept);
    }

    @Override
    @Transactional
    public void updateDept(DeptDTO deptDTO) {
        SysDept newParentDept = getById(deptDTO.getParentId());
        SysDept oldDept = getById(deptDTO.getDeptId());
        if (newParentDept != null && oldDept != null) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            oldDept.setAncestors(newAncestors);
            updateDeptChildren(oldDept.getDeptId(), newAncestors, oldAncestors);
        }
        
        SysDept dept = new SysDept();
        dept.setDeptId(deptDTO.getDeptId());
        dept.setDeptName(deptDTO.getDeptName());
        dept.setParentId(deptDTO.getParentId());
        dept.setOrderNum(deptDTO.getOrderNum());
        dept.setLeader(deptDTO.getLeader());
        dept.setPhone(deptDTO.getPhone());
        dept.setEmail(deptDTO.getEmail());
        dept.setStatus(deptDTO.getStatus());
        
        updateById(dept);
    }

    @Override
    public void deleteDeptById(Long deptId) {
        if (baseMapper.checkDeptExistUser(deptId) > 0) {
            throw new ServiceException("部门存在用户,不允许删除");
        }
        if (count(new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, deptId)) > 0) {
            throw new ServiceException("存在下级部门,不允许删除");
        }
        
        SysDept dept = getById(deptId);
        dept.setDelFlag("1");
        updateById(dept);
    }

    @Override
    public void updateDeptStatus(Long deptId, String status) {
        SysDept dept = getById(deptId);
        dept.setStatus(status);
        updateById(dept);
    }

    /**
     * 修改子元素关系
     */
    private void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = baseMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (!children.isEmpty()) {
            baseMapper.updateDeptChildren(children);
        }
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        return list.stream()
                .filter(n -> n.getParentId().equals(t.getDeptId()))
                .collect(Collectors.toList());
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return !getChildList(list, t).isEmpty();
    }
} 