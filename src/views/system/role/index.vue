<template>
  <div class="app-container">
    <el-card class="box-card">
      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="角色名称" prop="roleName">
          <el-input
            v-model="queryParams.roleName"
            placeholder="请输入角色名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="权限字符" prop="roleKey">
          <el-input
            v-model="queryParams.roleKey"
            placeholder="请输入权限字符"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="角色状态" clearable>
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
            v-has-perm="['system:role:add']"
            @click="handleAdd"
          >新增</el-button>
        </el-col>
      </el-row>

      <!-- 表格区域 -->
      <el-table v-loading="loading" :data="roleList">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="角色编号" prop="roleId" width="100" />
        <el-table-column label="角色名称" prop="roleName" :show-overflow-tooltip="true" />
        <el-table-column label="权限字符" prop="roleKey" :show-overflow-tooltip="true" />
        <el-table-column label="显示顺序" prop="roleSort" width="100" />
        <el-table-column label="状态" align="center" width="100">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              active-value="0"
              inactive-value="1"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
        <el-table-column label="操作" align="center" width="220" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button
              type="text"
              v-has-perm="['system:role:edit']"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              type="text"
              v-has-perm="['system:role:remove']"
              @click="handleDelete(scope.row)"
            >删除</el-button>
            <el-button
              type="text"
              v-has-perm="['system:role:edit']"
              @click="handleDataScope(scope.row)"
            >数据权限</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>

    <!-- 添加或修改角色对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="roleForm" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="权限字符" prop="roleKey">
          <el-input v-model="form.roleKey" placeholder="请输入权限字符" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="roleSort">
          <el-input-number v-model="form.roleSort" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单权限">
          <el-tree
            ref="menuRef"
            :data="menuOptions"
            show-checkbox
            node-key="id"
            :props="{ label: 'label', children: 'children' }"
            :default-checked-keys="menuIds"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 分配数据权限对话框 -->
    <el-dialog title="分配数据权限" v-model="openDataScope" width="500px" append-to-body>
      <el-form :model="form" label-width="80px">
        <el-form-item label="角色名称">
          <el-input v-model="form.roleName" :disabled="true" />
        </el-form-item>
        <el-form-item label="权限范围">
          <el-select v-model="form.dataScope">
            <el-option label="全部数据权限" value="1" />
            <el-option label="自定数据权限" value="2" />
            <el-option label="本部门数据权限" value="3" />
            <el-option label="本部门及以下数据权限" value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据权限" v-show="form.dataScope == 2">
          <el-tree
            ref="deptRef"
            :data="deptOptions"
            show-checkbox
            node-key="id"
            :props="{ label: 'label', children: 'children' }"
            :default-checked-keys="deptIds"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitDataScope">确 定</el-button>
          <el-button @click="cancelDataScope">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listRole, getRole, addRole, updateRole, deleteRole, changeRoleStatus, dataScope } from '@/api/role'
import { treeselect as menuTreeselect } from '@/api/menu'
import { treeselect as deptTreeselect } from '@/api/dept'

const loading = ref(false)
const total = ref(0)
const roleList = ref([])
const open = ref(false)
const openDataScope = ref(false)
const title = ref('')
const menuOptions = ref([])
const menuIds = ref([])
const deptOptions = ref([])
const deptIds = ref([])

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  roleName: undefined,
  roleKey: undefined,
  status: undefined
})

const form = ref({
  roleId: undefined,
  roleName: undefined,
  roleKey: undefined,
  roleSort: 0,
  status: '0',
  menuIds: [],
  deptIds: [],
  dataScope: '1',
  remark: undefined
})

const rules = {
  roleName: [
    { required: true, message: '角色名称不能为空', trigger: 'blur' }
  ],
  roleKey: [
    { required: true, message: '权限字符不能为空', trigger: 'blur' }
  ],
  roleSort: [
    { required: true, message: '显示顺序不能为空', trigger: 'blur' }
  ]
}

/** 查询角色列表 */
async function getList() {
  loading.value = true
  try {
    const res = await listRole(queryParams.value)
    roleList.value = res.data
    total.value = res.total
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
    roleId: undefined,
    roleName: undefined,
    roleKey: undefined,
    roleSort: 0,
    status: '0',
    menuIds: [],
    deptIds: [],
    dataScope: '1',
    remark: undefined
  }
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    roleName: undefined,
    roleKey: undefined,
    status: undefined
  }
  handleQuery()
}

/** 新增按钮操作 */
async function handleAdd() {
  reset()
  const res = await menuTreeselect()
  menuOptions.value = res.data
  open.value = true
  title.value = '添加角色'
}

/** 修改按钮操作 */
async function handleUpdate(row) {
  reset()
  const roleId = row.roleId
  const [roleRes, menuRes] = await Promise.all([
    getRole(roleId),
    menuTreeselect()
  ])
  form.value = roleRes.data
  menuOptions.value = menuRes.data
  menuIds.value = roleRes.data.menuIds
  open.value = true
  title.value = '修改角色'
}

/** 提交按钮 */
async function submitForm() {
  try {
    if (form.value.roleId) {
      await updateRole(form.value)
      ElMessage.success('修改成功')
    } else {
      await addRole(form.value)
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
  ElMessageBox.confirm('是否确认删除名称为"' + row.roleName + '"的数据项?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteRole(row.roleId)
    getList()
    ElMessage.success('删除成功')
  })
}

/** 状态修改 */
async function handleStatusChange(row) {
  try {
    await changeRoleStatus(row.roleId, row.status)
    ElMessage.success('修改成功')
  } catch (error) {
    row.status = row.status === '0' ? '1' : '0'
  }
}

/** 分配数据权限操作 */
async function handleDataScope(row) {
  reset()
  const [roleRes, deptRes] = await Promise.all([
    getRole(row.roleId),
    deptTreeselect()
  ])
  form.value = roleRes.data
  deptOptions.value = deptRes.data
  deptIds.value = roleRes.data.deptIds
  openDataScope.value = true
}

/** 提交按钮（数据权限） */
async function submitDataScope() {
  try {
    await dataScope(form.value)
    openDataScope.value = false
    getList()
    ElMessage.success('修改成功')
  } catch (error) {
    console.error(error)
  }
}

/** 取消按钮（数据权限） */
function cancelDataScope() {
  openDataScope.value = false
  reset()
}

onMounted(() => {
  getList()
})
</script> 