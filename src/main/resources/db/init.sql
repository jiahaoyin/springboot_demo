-- 用户表
CREATE TABLE sys_user (
    user_id         BIGINT(20)   NOT NULL AUTO_INCREMENT    COMMENT '用户ID',
    dept_id         BIGINT(20)                              COMMENT '部门ID',
    username        VARCHAR(30)   NOT NULL                   COMMENT '用户账号',
    nickname        VARCHAR(30)   NOT NULL                   COMMENT '用户昵称',
    email          VARCHAR(50)    DEFAULT ''                COMMENT '用户邮箱',
    phone          VARCHAR(11)    DEFAULT ''                COMMENT '手机号码',
    gender         CHAR(1)       DEFAULT '0'                COMMENT '用户性别（0男 1女 2未知）',
    avatar         VARCHAR(100)   DEFAULT ''                COMMENT '头像地址',
    password       VARCHAR(100)   DEFAULT ''                COMMENT '密码',
    status         CHAR(1)       DEFAULT '0'                COMMENT '帐号状态（0正常 1停用）',
    del_flag       CHAR(1)       DEFAULT '0'                COMMENT '删除标志（0代表存在 1代表删除）',
    login_ip       VARCHAR(128)   DEFAULT ''                COMMENT '最后登录IP',
    login_date     DATETIME                                 COMMENT '最后登录时间',
    create_by      VARCHAR(64)    DEFAULT ''                COMMENT '创建者',
    create_time    DATETIME                                 COMMENT '创建时间',
    update_by      VARCHAR(64)    DEFAULT ''                COMMENT '更新者',
    update_time    DATETIME                                 COMMENT '更新时间',
    remark         VARCHAR(500)   DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT = '用户信息表';

-- 部门表
CREATE TABLE sys_dept (
    dept_id         BIGINT(20)    NOT NULL AUTO_INCREMENT    COMMENT '部门id',
    parent_id       BIGINT(20)     DEFAULT 0                 COMMENT '父部门id',
    ancestors       VARCHAR(50)     DEFAULT ''               COMMENT '祖级列表',
    dept_name       VARCHAR(30)     DEFAULT ''               COMMENT '部门名称',
    order_num       INT(4)         DEFAULT 0                 COMMENT '显示顺序',
    leader          VARCHAR(20)     DEFAULT NULL             COMMENT '负责人',
    phone           VARCHAR(11)     DEFAULT NULL             COMMENT '联系电话',
    email           VARCHAR(50)     DEFAULT NULL             COMMENT '邮箱',
    status          CHAR(1)        DEFAULT '0'               COMMENT '部门状态（0正常 1停用）',
    del_flag        CHAR(1)        DEFAULT '0'               COMMENT '删除标志（0代表存在 1代表删除）',
    create_by       VARCHAR(64)     DEFAULT ''               COMMENT '创建者',
    create_time     DATETIME                                 COMMENT '创建时间',
    update_by       VARCHAR(64)     DEFAULT ''               COMMENT '更新者',
    update_time     DATETIME                                 COMMENT '更新时间',
    PRIMARY KEY (dept_id)
) ENGINE=InnoDB AUTO_INCREMENT=200 COMMENT = '部门表';

-- 岗位表
CREATE TABLE sys_post (
    post_id        BIGINT(20)     NOT NULL AUTO_INCREMENT    COMMENT '岗位ID',
    post_code      VARCHAR(64)     NOT NULL                   COMMENT '岗位编码',
    post_name      VARCHAR(50)     NOT NULL                   COMMENT '岗位名称',
    post_sort      INT(4)         NOT NULL                   COMMENT '显示顺序',
    status         CHAR(1)        NOT NULL                   COMMENT '状态（0正常 1停用）',
    create_by      VARCHAR(64)     DEFAULT ''                COMMENT '创建者',
    create_time    DATETIME                                  COMMENT '创建时间',
    update_by      VARCHAR(64)     DEFAULT ''                COMMENT '更新者',
    update_time    DATETIME                                  COMMENT '更新时间',
    remark         VARCHAR(500)    DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (post_id)
) ENGINE=InnoDB AUTO_INCREMENT=10 COMMENT = '岗位信息表';

-- 角色表
CREATE TABLE sys_role (
    role_id              BIGINT(20)    NOT NULL AUTO_INCREMENT    COMMENT '角色ID',
    role_name            VARCHAR(30)    NOT NULL                   COMMENT '角色名称',
    role_key             VARCHAR(100)   NOT NULL                   COMMENT '角色权限字符串',
    role_sort            INT(4)        NOT NULL                   COMMENT '显示顺序',
    data_scope           CHAR(1)       DEFAULT '1'                COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    menu_check_strictly  TINYINT(1)    DEFAULT 1                  COMMENT '菜单树选择项是否关联显示',
    dept_check_strictly  TINYINT(1)    DEFAULT 1                  COMMENT '部门树选择项是否关联显示',
    status              CHAR(1)        NOT NULL                   COMMENT '角色状态（0正常 1停用）',
    del_flag            CHAR(1)        DEFAULT '0'                COMMENT '删除标志（0代表存在 1代表删除）',
    create_by           VARCHAR(64)     DEFAULT ''                COMMENT '创建者',
    create_time         DATETIME                                  COMMENT '创建时间',
    update_by           VARCHAR(64)     DEFAULT ''                COMMENT '更新者',
    update_time         DATETIME                                  COMMENT '更新时间',
    remark              VARCHAR(500)    DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (role_id)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT = '角色信息表';

-- 菜单表
CREATE TABLE sys_menu (
    menu_id         BIGINT(20)    NOT NULL AUTO_INCREMENT    COMMENT '菜单ID',
    menu_name       VARCHAR(50)    NOT NULL                   COMMENT '菜单名称',
    parent_id       BIGINT(20)     DEFAULT 0                 COMMENT '父菜单ID',
    order_num       INT(4)         DEFAULT 0                 COMMENT '显示顺序',
    path            VARCHAR(200)    DEFAULT ''               COMMENT '路由地址',
    component       VARCHAR(255)    DEFAULT NULL             COMMENT '组件路径',
    query           VARCHAR(255)    DEFAULT NULL             COMMENT '路由参数',
    is_frame        INT(1)         DEFAULT 1                 COMMENT '是否为外链（0是 1否）',
    is_cache        INT(1)         DEFAULT 0                 COMMENT '是否缓存（0缓存 1不缓存）',
    menu_type       CHAR(1)        DEFAULT ''               COMMENT '菜单类型（M目录 C菜单 F按钮）',
    visible         CHAR(1)        DEFAULT '0'               COMMENT '菜单状态（0显示 1隐藏）',
    status          CHAR(1)        DEFAULT '0'               COMMENT '菜单状态（0正常 1停用）',
    perms           VARCHAR(100)    DEFAULT NULL             COMMENT '权限标识',
    icon            VARCHAR(100)    DEFAULT '#'              COMMENT '菜单图标',
    create_by       VARCHAR(64)     DEFAULT ''               COMMENT '创建者',
    create_time     DATETIME                                 COMMENT '创建时间',
    update_by       VARCHAR(64)     DEFAULT ''               COMMENT '更新者',
    update_time     DATETIME                                 COMMENT '更新时间',
    remark          VARCHAR(500)    DEFAULT ''               COMMENT '备注',
    PRIMARY KEY (menu_id)
) ENGINE=InnoDB AUTO_INCREMENT=2000 COMMENT = '菜单权限表';

-- 用户和角色关联表
CREATE TABLE sys_user_role (
    user_id   BIGINT(20) NOT NULL COMMENT '用户ID',
    role_id   BIGINT(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY(user_id, role_id)
) ENGINE=InnoDB COMMENT = '用户和角色关联表';

-- 角色和菜单关联表
CREATE TABLE sys_role_menu (
    role_id   BIGINT(20) NOT NULL COMMENT '角色ID',
    menu_id   BIGINT(20) NOT NULL COMMENT '菜单ID',
    PRIMARY KEY(role_id, menu_id)
) ENGINE=InnoDB COMMENT = '角色和菜单关联表';

-- 角色和部门关联表
CREATE TABLE sys_role_dept (
    role_id   BIGINT(20) NOT NULL COMMENT '角色ID',
    dept_id   BIGINT(20) NOT NULL COMMENT '部门ID',
    PRIMARY KEY(role_id, dept_id)
) ENGINE=InnoDB COMMENT = '角色和部门关联表';

-- 用户与岗位关联表
CREATE TABLE sys_user_post (
    user_id   BIGINT(20) NOT NULL COMMENT '用户ID',
    post_id   BIGINT(20) NOT NULL COMMENT '岗位ID',
    PRIMARY KEY (user_id, post_id)
) ENGINE=InnoDB COMMENT = '用户与岗位关联表';

-- 初始化管理员用户
INSERT INTO sys_user (
    user_id, username, nickname, password, status, create_time, remark
) VALUES (
    1, 
    'admin', 
    '系统管理员', 
    '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2',  -- 密码为 admin123
    '0',
    NOW(),
    '系统管理员'
);

-- 初始化超级管理员角色
INSERT INTO sys_role (
    role_id, role_name, role_key, role_sort, data_scope, status, create_time, remark
) VALUES (
    1,
    '超级管理员',
    'admin',
    1,
    '1',
    '0',
    NOW(),
    '超级管理员'
);

-- 初始化用户和角色关联关系
INSERT INTO sys_user_role (
    user_id, role_id
) VALUES (
    1, 1
);

-- 初始化部门
INSERT INTO sys_dept (
    dept_id, parent_id, ancestors, dept_name, order_num, leader, status, create_time
) VALUES (
    100, 
    0, 
    '0', 
    '总公司', 
    0, 
    'admin', 
    '0',
    NOW()
);

-- 更新管理员的部门ID
UPDATE sys_user SET dept_id = 100 WHERE user_id = 1; 