<template>
  <div class="profile">
    <div class="page-header">
      <h2 class="page-title">个人资料</h2>
    </div>

    <el-card shadow="hover">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="用户名">{{ form.username }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ form.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ form.email }}</el-descriptions-item>
        <el-descriptions-item label="账号状态">
          <el-tag :type="getUserStatusType(form.userStatus)">
            {{ getUserStatusText(form.userStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDateTime(form.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ formatDateTime(form.updateTime) }}</el-descriptions-item>
      </el-descriptions>

      <div style="margin-top: 16px; text-align: right">
        <el-button type="primary" @click="openEditDialog">编辑资料</el-button>
      </div>

      <el-dialog v-model="editDialogVisible" title="编辑个人资料" width="500px" destroy-on-close>
        <el-form :model="editForm" label-width="80px">
          <el-form-item label="用户名">
            <el-input v-model="editForm.username" />
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="editForm.phone" />
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="editForm.email" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEdit">保存</el-button>
        </template>
      </el-dialog>

    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { user } from '@/api'
import dayjs from 'dayjs'

const form = reactive({
  username: '',
  phone: '',
  email: '',
  createTime: '',
  updateTime: '',
  userStatus: ''
})

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

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    const res = await user.getProfile()
    Object.assign(form, res)
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败')
  }
}

// 编辑资料对话框
const editDialogVisible = ref(false)
const editForm = reactive({
  username: '',
  phone: '',
  email: ''
})

// 打开编辑对话框
const openEditDialog = () => {
  Object.assign(editForm, {
    username: form.username,
    phone: form.phone,
    email: form.email
  })
  editDialogVisible.value = true
}

// 提交编辑
const submitEdit = async () => {
  try {
    const response = await user.updateProfile(editForm) // 需要在 api/user.js 中定义对应方法
    console.log('后端返回的 response:', response)
    const token = response.token


    // res 是字符串类型的新 token
    if (typeof token === 'string') {
      localStorage.setItem('token', token) // 替换旧 token
      ElMessage.success('更新成功，正在刷新信息...')
      location.reload() // 强制刷新页面使新 token 生效
    } else {
      ElMessage.warning('更新成功但未收到新 token，请重新登录')
    }
    editDialogVisible.value = false
    
  } catch (error) {
    console.error('更新失败:', error)
    ElMessage.error('更新失败')
  }
}

// 初始化
fetchUserInfo()
</script>

<style scoped>
.profile {
  padding: 20px;
}

.page-header {
  margin-bottom: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}
</style> 