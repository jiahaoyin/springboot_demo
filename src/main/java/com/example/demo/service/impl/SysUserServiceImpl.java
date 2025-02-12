package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.SysUser;
import com.example.demo.dto.UserDTO;
import com.example.demo.mapper.SysUserMapper;
import com.example.demo.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.common.annotation.DataScope;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public SysUser getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
                .eq(SysUser::getDelFlag, "0"));
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(UserDTO userDTO) {
        return baseMapper.selectUserList(userDTO);
    }

    @Override
    @Transactional
    public void insertUser(UserDTO userDTO) {
        SysUser user = new SysUser();
        // 复制属性
        // TODO: 使用MapStruct进行对象转换
        user.setUsername(userDTO.getUsername());
        user.setNickname(userDTO.getNickname());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setDeptId(userDTO.getDeptId());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setStatus("0");
        user.setDelFlag("0");
        
        save(user);
        
        // TODO: 保存用户角色关��
        // TODO: 保存用户岗位关系
    }

    @Override
    @Transactional
    public void updateUser(UserDTO userDTO) {
        SysUser user = getById(userDTO.getUserId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 更新用户信息
        user.setNickname(userDTO.getNickname());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        
        updateById(user);
        
        // TODO: 更新用户角色关系
        // TODO: 更新用户岗位关系
    }

    @Override
    @Transactional
    public void deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            // 逻辑删除
            SysUser user = getById(userId);
            user.setDelFlag("1");
            updateById(user);
        }
    }

    @Override
    public void resetPassword(Long userId, String password) {
        SysUser user = getById(userId);
        user.setPassword(passwordEncoder.encode(password));
        updateById(user);
    }

    @Override
    public void updateUserStatus(Long userId, String status) {
        SysUser user = getById(userId);
        user.setStatus(status);
        updateById(user);
    }
} 