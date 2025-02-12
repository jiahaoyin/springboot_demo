package com.example.demo.common.aspect;

import com.example.demo.common.BaseQuery;
import com.example.demo.common.annotation.DataScope;
import com.example.demo.entity.SysRole;
import com.example.demo.entity.SysUser;
import com.example.demo.security.utils.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

@Aspect
@Component
public class DataScopeAspect {
    
    @Before("@annotation(dataScope)")
    public void doBefore(JoinPoint point, DataScope dataScope) {
        handleDataScope(point, dataScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint, DataScope dataScope) {
        // 获取当前用户
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        if (currentUser != null && !currentUser.isAdmin()) {
            dataScopeFilter(joinPoint, currentUser, dataScope.deptAlias(), dataScope.userAlias());
        }
    }

    /**
     * 数据范围过滤
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user, String deptAlias, String userAlias) {
        StringBuilder sqlString = new StringBuilder();

        for (SysRole role : user.getRoles()) {
            String dataScope = role.getDataScope();
            if ("1".equals(dataScope)) {
                // 全部数据权限
                sqlString = new StringBuilder();
                break;
            } else if ("2".equals(dataScope)) {
                // 自定数据权限
                sqlString.append(String.format(
                    " OR %s.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = %d ) ", 
                    deptAlias, role.getRoleId()));
            } else if ("3".equals(dataScope)) {
                // 本部门数据权限
                sqlString.append(String.format(
                    " OR %s.dept_id = %d ", 
                    deptAlias, user.getDeptId()));
            } else if ("4".equals(dataScope)) {
                // 本部门及以下数据权限
                sqlString.append(String.format(
                    " OR %s.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = %d OR find_in_set( %d , ancestors ) )", 
                    deptAlias, user.getDeptId(), user.getDeptId()));
            }
        }

        String sql = sqlString.toString();
        if (StringUtils.isNotBlank(sql)) {
            Object params = joinPoint.getArgs()[0];
            if (params instanceof BaseQuery) {
                BaseQuery baseQuery = (BaseQuery) params;
                baseQuery.getParams().put("dataScope", " AND (" + sql.substring(4) + ")");
            }
        }
    }
} 