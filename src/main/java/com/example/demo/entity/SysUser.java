package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entity.SysRole;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {
    
    /** 用户ID */
    @TableId(type = IdType.AUTO)
    private Long userId;
    
    /** 部门ID */
    private Long deptId;
    
    /** 用户账号 */
    private String username;
    
    /** 用户昵称 */
    private String nickname;
    
    /** 用户邮箱 */
    private String email;
    
    /** 手机号码 */
    private String phone;
    
    /** 用户性别 */
    private String gender;
    
    /** 头像地址 */
    private String avatar;
    
    /** 密码 */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    /** 帐号状态（0正常 1停用） */
    private String status;
    
    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;
    
    /** 最后登录IP */
    private String loginIp;
    
    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginDate;
    
    /** 部门对象 */
    @TableField(exist = false)
    private transient SysDept dept;
    
    /** 角色对象 */
    @TableField(exist = false)
    private transient List<SysRole> roles;
    
    /** 角色组 */
    @TableField(exist = false)
    private Long[] roleIds;
    
    /** 岗位组 */
    @TableField(exist = false)
    private Long[] postIds;

    /** 
     * 判断是否为管理员
     */
    @JsonIgnore
    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    /** 
     * 判断是否为管理员
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
} 