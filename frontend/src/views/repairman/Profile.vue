<template>
  <div class="profile">
    <div class="page-header">
      <h2 class="page-title">个人资料</h2>
    </div>

    <el-row :gutter="20">
      <!-- 基本信息 -->
      <el-col :span="16">
        <el-card shadow="hover" class="info-card">
          <template #header>
            <div class="card-header">
              <span>基本信息</span>
            </div>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="用户名">{{ form.username }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ form.phone }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ form.email }}</el-descriptions-item>
            <el-descriptions-item label="账号状态">
              <el-tag :type="getUserStatusType(form.userStatus)">
                {{ getUserStatusText(form.userStatus) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="专长">
              <el-tag type="success">{{ getFaultTypeText(form.specialty) }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="时薪">￥{{ form.hourlyMoneyRate?.toFixed(2) }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ formatDateTime(form.createTime) }}</el-descriptions-item>
            <el-descriptions-item label="更新时间">{{ formatDateTime(form.updateTime) }}</el-descriptions-item>
          </el-descriptions>
        </el-card>

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
            <el-form-item label="专长">
              <el-select v-model="editForm.specialty" placeholder="请选择专长">
                <el-option label="常规保养" value="MAINTENANCE" />
                <el-option label="通用维修" value="REPAIR" />
                <el-option label="钣金喷漆" value="PAINT" />
                <el-option label="轮胎更换" value="TIRE" />
                <el-option label="电路系统修复" value="ELECTRICAL" />
                <el-option label="车身修复" value="BODYWORK" />
                <el-option label="发动机维修" value="ENGINE" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-form>
          <template #footer>
            <el-button @click="editDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitEdit">保存</el-button>
          </template>
        </el-dialog>

      </el-col>

      <!-- 统计信息 -->
      <el-col :span="8">
        <el-card shadow="hover" class="stats-card">
          <template #header>
            <div class="card-header">
              <span>工作统计</span>
              <el-radio-group v-model="statsTimeRange" size="small">
                <el-radio-button label="all">全部</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div class="stats-content">
            <div class="stat-item">
              <div class="stat-label">完成订单</div>
              <div class="stat-value">{{ stats.completedOrders }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">维修时长</div>
              <div class="stat-value">{{ stats.repairHours }}小时</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">总收入</div>
              <div class="stat-value">¥{{ stats.totalIncome.toFixed(2) }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">平均评分</div>
              <div class="stat-value">{{ stats.averageRating.toFixed(1) }}分</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { repairman } from '@/api'
import dayjs from 'dayjs'

// 表单数据
const form = reactive({
  username: '',
  phone: '',
  email: '',
  createTime: '',
  updateTime: '',
  userStatus: '',
  specialty: '',
  hourlyMoneyRate: 0
})

// 统计信息
const statsTimeRange = ref('all')
const stats = ref({
  completedOrders: 0,
  repairHours: 0,
  totalIncome: 0,
  averageRating: 0
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

// 获取故障类型文本
const getFaultTypeText = (type) => {
  const map = {
    MAINTENANCE: '常规保养',
    REPAIR: '通用维修',
    PAINT: '钣金喷漆',
    TIRE: '轮胎更换',
    ELECTRICAL: '电路系统修复',
    BODYWORK: '车身修复',
    ENGINE: '发动机维修',
    OTHER: '其他'

  }
  return map[type] || type
}

// 格式化日期时间
const formatDateTime = (datetime) => {
  if (!datetime) return '-'
  return dayjs(datetime).format('YYYY-MM-DD HH:mm:ss')
}

// 获取个人信息
const fetchProfile = async () => {
  try {
    const res = await repairman.getProfile()
    Object.assign(form, res)
  } catch (error) {
    console.error('获取个人信息失败:', error)
    ElMessage.error('获取个人信息失败')
  }
}

// 获取统计信息
const fetchStats = async () => {
  try {
    const params = {
      startTime: statsTimeRange.value === 'month' 
        ? dayjs().startOf('month').format('YYYY-MM-DDTHH:mm:ss')
        : null
    }
    const res = await repairman.getStats(params)
    stats.value = res
  } catch (error) {
    console.error('获取统计信息失败:', error)
    ElMessage.error('获取统计信息失败')
  }
}

const editDialogVisible = ref(false)
const editForm = reactive({
  username: '',
  phone: '',
  email: '',
  specialty: ''
})

// 打开编辑对话框
const openEditDialog = () => {
  Object.assign(editForm, {
    username: form.username,
    phone: form.phone,
    email: form.email,
    specialty: form.specialty
  })
  editDialogVisible.value = true
}

// 提交编辑
const submitEdit = async () => {
  try {
    const response = await repairman.updateProfile(editForm)
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


// 监听统计时间范围变化
watch(statsTimeRange, () => {
  fetchStats()
})

onMounted(() => {
  fetchProfile()
fetchStats()
})


// 初始化

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

.info-card {
  margin-bottom: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stats-card {
  margin-bottom: 24px;
}

.stats-content {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  padding: 20px 0;
}

.stat-item {
  text-align: center;
}

.stat-label {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 8px;
}

.stat-value {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
}
</style> 