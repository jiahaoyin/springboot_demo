package com.example.demo.dto;

import com.example.demo.common.BaseQuery;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends BaseQuery {
    
    private Long userId;
    
    private Long deptId;
    
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 3, max = 30, message = "用户账号长度必须在3到30个字符之间")
    private String username;
    
    @NotBlank(message = "用户昵称不能为空")
    @Size(min = 2, max = 30, message = "用户昵称长度必须在2到30个字符之间")
    private String nickname;
    
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    private String email;
    
    @Size(max = 11, message = "手机号码长度不能超过11个字符")
    private String phone;
    
    private String gender;
    
    private String avatar;
    
    private String password;
    
    private String status;
    
    private Long[] roleIds;
    
    private Long[] postIds;
    
    private String remark;
} 