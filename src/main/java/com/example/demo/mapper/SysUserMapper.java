package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.SysUser;
import com.example.demo.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    
    /**
     * 根据用户ID查询角色ID列表
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID查询岗位ID列表
     */
    List<Long> selectPostIdsByUserId(@Param("userId") Long userId);
    
    /**
     * 批量删除用户和角色关联
     */
    void deleteUserRoleByUserIds(@Param("userIds") Long[] userIds);
    
    /**
     * 批量删除用户和岗位关联
     */
    void deleteUserPostByUserIds(@Param("userIds") Long[] userIds);
    
    /**
     * 新增用户角色信息
     */
    void insertUserRole(@Param("userId") Long userId, @Param("roleIds") Long[] roleIds);
    
    /**
     * 新增用户岗位信息
     */
    void insertUserPost(@Param("userId") Long userId, @Param("postIds") Long[] postIds);
    
    /**
     * 查询用户列表
     */
    List<SysUser> selectUserList(UserDTO userDTO);
} 