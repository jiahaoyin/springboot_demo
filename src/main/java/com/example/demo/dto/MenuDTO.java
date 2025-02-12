package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MenuDTO {
    
    private Long menuId;
    
    private Long parentId;
    
    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 2, max = 50, message = "菜单名称长度必须在2到50个字符之间")
    private String menuName;
    
    private Integer orderNum;
    
    private String path;
    
    private String component;
    
    private String query;
    
    private Integer isFrame;
    
    private Integer isCache;
    
    @NotBlank(message = "菜单类型不能为空")
    private String menuType;
    
    private String visible;
    
    private String status;
    
    private String perms;
    
    private String icon;
    
    private String remark;
} 