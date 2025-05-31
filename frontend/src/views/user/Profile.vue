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