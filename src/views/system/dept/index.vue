<template>
  <div class="app-container">
    <el-card class="box-card">
      <!-- 搜索区域 -->
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="部门名称" prop="deptName">
          <el-input
            v-model="queryParams.deptName"
            placeholder="请输入部门名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="部门状态" clearable>
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
            v-has-perm="['system:dept:add']"
            @click="handleAdd"
          >新增</el-button>
        </el-col>
      </el-row>

      <!-- 表格区域 -->
      <el-table
        v-loading="loading"
        :data="deptList"
        row-key="deptId"
        default-expand-all
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column prop="deptName" label="部门名称" width="260" />
        <el-table-column prop="orderNum" label="排序" width="200" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              active-value="0"
              inactive-value="1"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="200" />
        <el-table-column label="操作" align="center" width="200">
          <template #default="scope">
            <el-button
              type="text"
              v-has-perm="['system:dept:add']"
              @click="handleAdd(scope.row)"
            >新增</el-button>
            <el-button
              type="text"
              v-has-perm="['system:dept:edit']"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              type="text"
              v-has-perm="['system:dept:remove']"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改部门对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="deptForm" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级部门">
              <el-tree-select
                v-model="form.parentId"
                :data="deptOptions"
                :props="{ label: 'deptName', value: 'deptId', children: 'children' }"
                value-key="deptId"
                placeholder="选择上级部门"
                check-strictly
                :render-after-expand="false"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门名称" prop="deptName">
              <el-input v-model="form.deptName" placeholder="请输入部���名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="leader">
              <el-input v-model="form.leader" placeholder="请输入负责人" maxlength="20" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门状态">
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
import { listDept, getDept, addDept, updateDept, deleteDept, changeDeptStatus } from '@/api/dept'

const loading = ref(false)
const open = ref(false)
const title = ref('')
const deptList = ref([])
const deptOptions = ref([])

const queryParams = ref({
  deptName: undefined,
  status: undefined
})

const form = ref({
  deptId: undefined,
  parentId: 0,
  deptName: undefined,
  orderNum: 0,
  leader: undefined,
  phone: undefined,
  email: undefined,
  status: '0'
})

const rules = {
  deptName: [
    { required: true, message: '部门名称不能为空', trigger: 'blur' }
  ],
  orderNum: [
    { required: true, message: '显示顺序不能为空', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

/** 查询部门列表 */
async function getList() {
  loading.value = true
  try {
    const res = await listDept(queryParams.value)
    deptList.value = res.data
    deptOptions.value = [{
      deptId: 0,
      deptName: '主类目',
      children: res.data
    }]
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
    deptId: undefined,
    parentId: 0,
    deptName: undefined,
    orderNum: 0,
    leader: undefined,
    phone: undefined,
    email: undefined,
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
    deptName: undefined,
    status: undefined
  }
  handleQuery()
}

/** 新增按钮操作 */
function handleAdd(row) {
  reset()
  if (row != null && row.deptId) {
    form.value.parentId = row.deptId
  }
  open.value = true
  title.value = '添加部门'
}

/** 修改按钮操作 */
async function handleUpdate(row) {
  reset()
  const deptId = row.deptId
  const res = await getDept(deptId)
  form.value = res.data
  open.value = true
  title.value = '修改部门'
}

/** 提交按钮 */
async function submitForm() {
  try {
    if (form.value.deptId) {
      await updateDept(form.value)
      ElMessage.success('修改成功')
    } else {
      await addDept(form.value)
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
  ElMessageBox.confirm('是否确认删除名称为"' + row.deptName + '"的数据项?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteDept(row.deptId)
    getList()
    ElMessage.success('删除成功')
  })
}

/** 状态修改 */
async function handleStatusChange(row) {
  try {
    await changeDeptStatus(row.deptId, row.status)
    ElMessage.success('修改成功')
  } catch (error) {
    row.status = row.status === '0' ? '1' : '0'
  }
}

onMounted(() => {
  getList()
})
</script> 