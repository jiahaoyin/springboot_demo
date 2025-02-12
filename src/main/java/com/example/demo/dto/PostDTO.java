package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDTO {
    
    private Long postId;
    
    @NotBlank(message = "岗位编码不能为空")
    @Size(min = 2, max = 64, message = "岗位编码长度必须在2到64个字符之间")
    private String postCode;
    
    @NotBlank(message = "岗位名称不能为空")
    @Size(min = 2, max = 50, message = "岗位名称长度必须在2到50个字符之间")
    private String postName;
    
    @NotNull(message = "显示顺序不能为空")
    private Integer postSort;
    
    private String status;
    
    private String remark;
} 