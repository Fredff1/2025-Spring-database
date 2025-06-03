<template>
  <div class="repairmen">
    <div class="page-header">
      <h2 class="page-title">维修人员管理</h2>
      <!-- <el-button type="primary" @click="handleAdd">添加维修人员</el-button> -->
       <!--暂时不支持增删改-->
    </div>

    <!-- 维修人员列表 -->
    <el-card shadow="hover">
      <el-table :data="repairmen" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="specialty" label="专长" width="120">
          <template #default="{ row }">
            <el-tag :type="getRepairTypeTag(row.specialty)">{{ getRepairTypeText(row.specialty)}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="hourlyMoneyRate" label="时薪" width="120">
          <template #default="{ row }">
            ￥{{ row.hourlyMoneyRate }}
          </template>
        </el-table-column>
        <el-table-column prop="userStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getUserStatusType(row.userStatus)">
              {{ getUserStatusText(row.userStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.updateTime) }}
          </template>
        </el-table-column>
        <!-- <el-table-column label="操作" fixed="right" width="150">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column> -->
      <!--暂时不支持增删改-->
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 维修人员表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加维修人员' : '编辑维修人员'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="专长" prop="specialty">
          <el-select v-model="form.specialty" placeholder="请选择专长">
            <el-option label="发动机" value="ENGINE" />
            <el-option label="变速箱" value="TRANSMISSION" />
            <el-option label="制动系统" value="BRAKE" />
            <el-option label="电气系统" value="ELECTRICAL" />
            <el-option label="空调系统" value="AC" />
            <el-option label="轮胎" value="TIRE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="时薪" prop="hourlyMoneyRate">
          <el-input-number 
            v-model="form.hourlyMoneyRate" 
            :min="0" 
            :precision="2" 
            :step="10"
          />
        </el-form-item>
        <el-form-item label="状态" prop="userStatus">
          <el-select v-model="form.userStatus" placeholder="请选择状态">
            <el-option label="正常" value="ACTIVE" />
            <el-option label="未激活" value="INACTIVE" />
            <el-option label="已封禁" value="BLOCKED" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { admin } from '@/api'
import dayjs from 'dayjs'

// 表格数据
const loading = ref(false)
const repairmen = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 对话框相关
const dialogVisible = ref(false)
const dialogType = ref('add')
const submitting = ref(false)
const formRef = ref(null)

// 表单数据
const form = reactive({
  username: '',
  phone: '',
  email: '',
  specialty: '',
  hourlyMoneyRate: 0,
  userStatus: 'ACTIVE'
})

// 表单验证规则
const validatePhone = (rule, value, callback) => {
  if (value && !/^1[3-9]\d{9}$/.test(value)) {
    callback(new Error('请输入正确的手机号'))
  } else {
    callback()
  }
}

const validateEmail = (rule, value, callback) => {
  if (value && !/^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/.test(value)) {
    callback(new Error('请输入正确的邮箱地址'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { validator: validatePhone, trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { validator: validateEmail, trigger: 'blur' }
  ],
  specialty: [
    { required: true, message: '请选择专长', trigger: 'change' }
  ],
  hourlyMoneyRate: [
    { required: true, message: '请输入时薪', trigger: 'blur' }
  ],
  userStatus: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

const getRepairTypeText = (type) => {
  const map = {
    MAINTENANCE: '常规保养',
    REPAIR: '故障维修',
    PAINT: '钣金喷漆',
    TIRE: '轮胎更换',
    OTHER: '其他'
  }
  return map[type] || type
}

const getRepairTypeTag = (type) => {
  const map = {
    MAINTENANCE: 'success',
    REPAIR: 'danger',
    PAINT: 'warning',
    TIRE: 'warning',
    OTHER: 'info'
  }
  return map[type] || 'info'
}

// 获取用户状态类型
const getUserStatusType = (status) => {
  const map = {
    ACTIVE: 'success',
    INACTIVE: 'info',
    BLOCKED: 'danger'
  }
  return map[status] || 'info'
}

// 获取用户状态文本
const getUserStatusText = (status) => {
  const map = {
    ACTIVE: '正常',
    INACTIVE: '未激活',
    BLOCKED: '已封禁'
  }
  return map[status] || status
}

// 格式化日期时间
const formatDateTime = (datetime) => {
  if (!datetime) return '-'
  return dayjs(datetime).format('YYYY-MM-DD HH:mm:ss')
}

// 获取维修人员列表
const fetchRepairmen = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      limit: pageSize.value
    }
    const res = await admin.getRepairmen(params)
    repairmen.value = res.list || []
    total.value = res.total || 0
  } catch (error) {
    console.error('获取维修人员列表失败:', error)
    ElMessage.error('获取维修人员列表失败')
  } finally {
    loading.value = false
  }
}

// 添加维修人员
const handleAdd = () => {
  dialogType.value = 'add'
  form.username = ''
  form.phone = ''
  form.email = ''
  form.specialty = ''
  form.hourlyMoneyRate = 0
  form.userStatus = 'ACTIVE'
  dialogVisible.value = true
}

// 编辑维修人员
const handleEdit = (row) => {
  dialogType.value = 'edit'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 删除维修人员
const handleDelete = (row) => {
  ElMessageBox.confirm(
    '确定要删除该维修人员吗？',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await admin.deleteRepairman(row.id)
      ElMessage.success('删除成功')
      fetchRepairmen()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (dialogType.value === 'add') {
          await admin.createRepairman(form)
          ElMessage.success('添加成功')
        } else {
          await admin.updateRepairman(form.id, form)
          ElMessage.success('更新成功')
        }
        dialogVisible.value = false
        fetchRepairmen()
      } catch (error) {
        console.error('保存失败:', error)
        ElMessage.error('保存失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

// 分页大小变化
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchRepairmen()
}

// 页码变化
const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchRepairmen()
}

onMounted(() => {
  fetchRepairmen()
})
</script>

<style scoped>
.repairmen {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 