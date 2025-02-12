package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.R;
import com.example.demo.entity.SysMenu;
import com.example.demo.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final ISysMenuService menuService;

    /**
     * 获取菜单列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasPermission('system:menu:list')")
    public R<List<SysMenu>> list() {
        List<SysMenu> menus = menuService.list();
        return R.ok(menus);
    }

    /**
     * 根据用户ID获取菜单树
     */
    @GetMapping("/treeByUser")
    public R<List<SysMenu>> getMenuTreeByUserId(@RequestParam Long userId) {
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return R.ok(menus);
    }

    /**
     * 根据角色ID获取菜单树
     */
    @GetMapping("/treeByRole/{roleId}")
    @PreAuthorize("hasPermission('system:menu:list')")
    public R<List<Long>> getMenuIdsByRoleId(@PathVariable Long roleId) {
        List<Long> menuIds = menuService.selectMenuListByRoleId(roleId);
        return R.ok(menuIds);
    }

    /**
     * 新增菜单
     */
    @PostMapping
    @PreAuthorize("hasPermission('system:menu:add')")
    public R<Void> add(@RequestBody SysMenu menu) {
        if (!menuService.save(menu)) {
            return R.error("新增菜单失败");
        }
        return R.ok();
    }

    /**
     * 修改菜单
     */
    @PutMapping
    @PreAuthorize("hasPermission('system:menu:edit')")
    public R<Void> edit(@RequestBody SysMenu menu) {
        if (!menuService.updateById(menu)) {
            return R.error("修改菜单失败");
        }
        return R.ok();
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasPermission('system:menu:remove')")
    public R<Void> remove(@PathVariable Long menuId) {
        if (menuService.count(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, menuId)) > 0) {
            return R.error("存在子菜单,不允许删除");
        }
        if (!menuService.removeById(menuId)) {
            return R.error("删除菜单失败");
        }
        return R.ok();
    }

    /**
     * 获取菜单树形结构
     */
    @GetMapping("/treeselect")
    public R<List<SysMenu>> treeselect() {
        List<SysMenu> menus = menuService.list();
        return R.ok(menuService.buildMenuTree(menus));
    }
} 