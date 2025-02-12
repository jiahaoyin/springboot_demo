package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleDTO {
    
    private Long roleId;
    
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 2, max = 30, message = "角色名称长度必须在2到30个字符之间")
    private String roleName;
    
    @NotBlank(message = "权限字符不能为空")
    @Size(min = 2, max = 100, message = "权限字符长度必须在2到100个字符之间")
    private String roleKey;
    
    @NotNull(message = "显示顺序不能为空")
    private Integer roleSort;
    
    private String dataScope;
    
    private String status;
    
    private Long[] menuIds;
    
    private Long[] deptIds;
    
    private String remark;
} 