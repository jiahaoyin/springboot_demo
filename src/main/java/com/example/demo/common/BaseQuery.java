package com.example.demo.common;

import lombok.Data;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class BaseQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 分页参数 */
    private Integer pageNum = 1;
    private Integer pageSize = 10;

    /** 查询参数 */
    private Map<String, Object> params;

    public BaseQuery() {
        this.params = new HashMap<>();
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }
} 