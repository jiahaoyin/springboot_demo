<template>
  <div class="app-container">
    <el-card class="box-card">
      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="菜单名称" prop="menuName">
          <el-input
            v-model="queryParams.menuName"
            placeholder="请输入菜单名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="菜单状态" clearable>
            <el-option label="正常" value="0" />
            <el-option label="停用" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮区域 -->
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            v-has-perm="['system:menu:add']"
            @click="handleAdd"
          >新增</el-button>
        </el-col>
      </el-row>

      <!-- 表格区域 -->
      <el-table
        v-loading="loading"
        :data="menuList"
        row-key="menuId"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column prop="menuName" label="菜单名称" :show-overflow-tooltip="true" width="160" />
        <el-table-column prop="icon" label="图标" align="center" width="100">
          <template #default="scope">
            <el-icon v-if="scope.row.icon">
              <component :is="scope.row.icon" />
            </el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="排序" width="60" align="center" />
        <el-table-column prop="perms" label="权限标识" :show-overflow-tooltip="true" />
        <el-table-column prop="path" label="路由地址" :show-overflow-tooltip="true" />
        <el-table-column prop="component" label="组件路径" :show-overflow-tooltip="true" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === '0' ? 'success' : 'info'">
              {{ scope.row.status === '0' ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
        <el-table-column label="操作" align="center" width="200">
          <template #default="scope">
            <el-button
              type="text"
              v-has-perm="['system:menu:add']"
              @click="handleAdd(scope.row)"
            >新增</el-button>
            <el-button
              type="text"
              v-has-perm="['system:menu:edit']"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              type="text"
              v-has-perm="['system:menu:remove']"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" v-model="open" width="680px" append-to-body>
      <el-form ref="menuForm" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级菜单">
              <el-tree-select
                v-model="form.parentId"
                :data="menuOptions"
                :props="{ label: 'menuName', value: 'menuId', children: 'children' }"
                value-key="menuId"
                placeholder="选择上级菜单"
                check-strictly
                :render-after-expand="false"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio label="M">目录</el-radio>
                <el-radio label="C">菜单</el-radio>
                <el-radio label="F">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单名称" prop="menuName">
              <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="权限标识" prop="perms">
              <el-input v-model="form.perms" placeholder="请输入权限标识" maxlength="100" />
              <span class="help-block">
                例如：system:user:list
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="路由地址" prop="path">
              <el-input v-model="form.path" placeholder="请输入路由地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组件路径" prop="component">
              <el-input v-model="form.component" placeholder="请输入组件路径" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单状态">
              <el-radio-group v-model="form.status">
                <el-radio label="0">正常</el-radio>
                <el-radio label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listMenu, getMenu, addMenu, updateMenu, deleteMenu } from '@/api/menu'

const loading = ref(false)
const open = ref(false)
const title = ref('')
const menuList = ref([])
const menuOptions = ref([])

const queryParams = ref({
  menuName: undefined,
  status: undefined
})

const form = ref({
  menuId: undefined,
  parentId: 0,
  menuName: undefined,
  orderNum: 0,
  path: undefined,
  component: undefined,
  perms: undefined,
  icon: undefined,
  menuType: 'M',
  status: '0'
})

const rules = {
  menuName: [
    { required: true, message: '菜单名称不能为空', trigger: 'blur' }
  ],
  orderNum: [
    { required: true, message: '显示顺序不能为空', trigger: 'blur' }
  ],
  path: [
    { required: true, message: '路由地址不能为空', trigger: 'blur' }
  ]
}

/** 查询菜单列表 */
async function getList() {
  loading.value = true
  try {
    const res = await listMenu(queryParams.value)
    menuList.value = res.data
  } catch (error) {
    console.error(error)
  }
  loading.value = false
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    menuId: undefined,
    parentId: 0,
    menuName: undefined,
    orderNum: 0,
    path: undefined,
    component: undefined,
    perms: undefined,
    icon: undefined,
    menuType: 'M',
    status: '0'
  }
}

/** 搜索按钮操作 */
function handleQuery() {
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.value = {
    menuName: undefined,
    status: undefined
  }
  handleQuery()
}

/** 新增按钮操作 */
function handleAdd(row) {
  reset()
  if (row != null && row.menuId) {
    form.value.parentId = row.menuId
  }
  open.value = true
  title.value = '添加菜单'
}

/** 修改按钮操作 */
async function handleUpdate(row) {
  reset()
  const menuId = row.menuId
  const res = await getMenu(menuId)
  form.value = res.data
  open.value = true
  title.value = '修改菜单'
}

/** 提交按钮 */
async function submitForm() {
  try {
    if (form.value.menuId) {
      await updateMenu(form.value)
      ElMessage.success('修改成功')
    } else {
      await addMenu(form.value)
      ElMessage.success('新增成功')
    }
    open.value = false
    getList()
  } catch (error) {
    console.error(error)
  }
}

/** 删除按钮操作 */
function handleDelete(row) {
  ElMessageBox.confirm('是否确认删除名称为"' + row.menuName + '"的数据项?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteMenu(row.menuId)
    getList()
    ElMessage.success('删除成功')
  })
}

onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
.help-block {
  display: block;
  margin-top: 5px;
  margin-bottom: 10px;
  color: #737373;
}
</style> 