package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends BaseEntity {
    
    /** 角色ID */
    @TableId(type = IdType.AUTO)
    private Long roleId;
    
    /** 角色名称 */
    private String roleName;
    
    /** 角色权限字符串 */
    private String roleKey;
    
    /** 显示顺序 */
    private Integer roleSort;
    
    /** 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限） */
    private String dataScope = "1";
    
    /** 角色状态（0正常 1停用） */
    private String status;
    
    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;
    
    /** 菜单组 */
    @TableField(exist = false)
    private Long[] menuIds;
    
    /** 部门组 */
    @TableField(exist = false)
    private Long[] deptIds;
} 