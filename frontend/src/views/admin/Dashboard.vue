<template>
  <div class="dashboard">
    <div class="page-header">
      <h2 class="page-title">工作台</h2>
    </div>

    <!-- 概览数据 -->
    <div class="overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card shadow="hover" class="overview-card">
            <template #header>
              <div class="card-header">
                <span>今日订单</span>
                <el-tag type="info">今日</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">{{ overview.todayOrders }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="overview-card">
            <template #header>
              <div class="card-header">
                <span>未完成</span>
                <el-tag type="warning">未完成</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">{{ overview.unfinishedOrders }}</div>
              <div class="sub-value">个待处理订单</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="overview-card">
            <template #header>
              <div class="card-header">
                <span>本月费用</span>
                <el-tag type="success">本月</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">¥{{ overview.monthlyCost }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="overview-card">
            <template #header>
              <div class="card-header">
                <span>活跃维修人员数</span>
                <el-tag type="primary">维修人员数</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">{{ overview.activeRepairmen }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 工作区域 -->
    <div class="work-area">
      <el-row :gutter="20">
        <!-- 待处理订单 -->
        <el-col :span="18">
          <el-card shadow="hover" class="work-card">
            <template #header>
              <div class="card-header">
                <span>待处理订单</span>
                <el-button type="primary" link @click="handleViewAll">查看全部</el-button>
              </div>
            </template>
            <el-table :data="unfinishedOrders" style="width: 100%">
              <el-table-column prop="orderNo" label="订单号" width="90" />
              <el-table-column prop="vehiclePlate" label="车牌号" width="120" />
              <el-table-column prop="repairType" label="维修类型" width="120">
                <template #default="{ row }">
                  <el-tag :type="getRepairTypeTag(row.repairType)">
                    {{ getRepairTypeText(row.repairType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="订单状态" width="120">
                <template #default="{ row }">
                  <el-tag :type="getOrderStatusTag(row.status)">
                    {{ getOrderStatusText(row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="problem" label="问题描述" min-width="200" show-overflow-tooltip />
              <el-table-column prop="createTime" label="创建时间" width="180" />
            </el-table>
          </el-card>
        </el-col>

        <!-- 右侧信息 -->
        <el-col :span="6">
          <!-- 系统状态 -->
          <el-card shadow="hover" class="work-card">
            <template #header>
              <div class="card-header">
                <span>系统状态</span>
                <el-button type="primary" link @click="handleRefreshStatus">刷新</el-button>
              </div>
            </template>
            <div class="system-status">
              <div class="status-item">
                <span class="label">CPU使用率</span>
                <el-progress :percentage="systemStatus.cpu" :status="getProgressStatus(systemStatus.cpu)" />
              </div>
              <div class="status-item">
                <span class="label">内存使用率</span>
                <el-progress :percentage="systemStatus.memory" :status="getProgressStatus(systemStatus.memory)" />
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowUp } from '@element-plus/icons-vue'
import { admin } from '@/api'

const router = useRouter()

// 概览数据
const overview = ref({
  todayOrders: 0,
  unfinishedOrders: 0,
  monthlyCost: 0,
  activeRepairmen: 0
})

// 待处理订单
const unfinishedOrders = ref([])

// 系统状态
const systemStatus = ref({
  cpu: 0,
  memory: 0,
})

const getOrderStatusTag = (status) => {
  const map = {
    PENDING: 'info',
    PROCESSING: 'success',
    COMPLETED: 'success',
    CANCELLED: 'danger'
  }
  return map[status] || 'info'
}

const getOrderStatusText = (status) => {
  const map = {
    PENDING: '待处理',
    PROCESSING: '维修中',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

const getRepairTypeTag = (type) => {
  const map = {
    MAINTENANCE: 'success',
    REPAIR: 'warning',
    PAINT: 'danger',
    TIRE: 'info',
    ELECTRICAL: 'warning',
    BODYWORK: 'danger',
    ENGINE: 'danger',
    OTHER: 'info'
  }
  return map[type] || 'info'
}

const getRepairTypeText = (type) => {
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

const getProgressStatus = (percentage) => {
  if (percentage >= 90) return 'exception'
  if (percentage >= 70) return 'warning'
  return 'success'
}

// 获取数据
const fetchData = async () => {
  try {
    const [overviewRes, unfinishedRes, systemRes] = await Promise.all([
      admin.getOverview(),
      admin.getUnfinishedOrders(),
      admin.getSystemStatus()
    ])
    overview.value = overviewRes
    unfinishedOrders.value = unfinishedRes
    systemStatus.value = systemRes
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  }
}

const handleViewAll = () => {
  router.push('/admin/orders')
}


const handleRefreshStatus = async () => {
  try {
    const res = await admin.getSystemStatus()
    systemStatus.value = res
    ElMessage.success('刷新成功')
  } catch (error) {
    ElMessage.error('刷新失败')
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.dashboard {
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

.overview {
  margin-bottom: 24px;
}

.overview-card {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-content {
  text-align: center;
}

.value {
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 8px;
}

.sub-value {
  font-size: 14px;
  color: var(--text-secondary);
}

.trend {
  font-size: 14px;
  color: var(--text-secondary);
}

.trend.up {
  color: var(--el-color-success);
}

.work-area {
  margin-bottom: 24px;
}

.work-card {
  margin-bottom: 24px;
}

.repairman-status {
  padding: 10px 0;
}

.repairman-item {
  padding: 12px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.repairman-item:last-child {
  border-bottom: none;
}

.repairman-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.repairman-info .name {
  font-weight: 500;
}

.repairman-stats {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: var(--text-secondary);
}

.system-status {
  padding: 10px 0;
}

.status-item {
  margin-bottom: 16px;
}

.status-item:last-child {
  margin-bottom: 0;
}

.status-item .label {
  display: block;
  margin-bottom: 8px;
  color: var(--text-secondary);
}

.status-item .value {
  font-size: 16px;
  font-weight: 500;
}
</style> 