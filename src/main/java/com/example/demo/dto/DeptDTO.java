package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.example.demo.common.BaseQuery;

@Data
@EqualsAndHashCode(callSuper = false)
public class DeptDTO extends BaseQuery {
    
    private Long deptId;
    
    private Long parentId;
    
    @NotBlank(message = "部门名称不能为空")
    @Size(min = 2, max = 30, message = "部门名称长度必须在2到30个字符之间")
    private String deptName;
    
    private Integer orderNum;
    
    @Size(max = 20, message = "负责人长度不能超过20个字符")
    private String leader;
    
    @Size(max = 11, message = "联系电话长度不能超过11个字符")
    private String phone;
    
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    private String email;
    
    private String status;
    
    private String remark;
} 