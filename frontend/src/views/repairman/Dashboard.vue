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
                <span>待处理任务分配</span>
                <el-tag type="info">任务分配</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">{{ overview.totalAssignments }}个待处理分配</div>
              <!-- <div class="trend" :class="{ 'up': overview.orderTrend > 0 }">
                {{ overview.orderTrend > 0 ? '+' : '' }}{{ overview.orderTrend }}%
                <el-icon><arrow-up /></el-icon>
              </div> -->
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="overview-card">
            <template #header>
              <div class="card-header">
                <span>待处理订单</span>
                <el-tag type="warning">订单</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">{{ overview.processingOrders }}个待处理订单</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="overview-card">
            <template #header>
              <div class="card-header">
                <span>本月收入</span>
                <el-tag type="success">本月</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">¥{{ overview.monthlyIncome.toFixed(2) }}</div>
              <!-- <div class="trend" :class="{ 'up': overview.incomeTrend > 0 }">
                {{ overview.incomeTrend > 0 ? '+' : '' }}{{ overview.incomeTrend }}%
                <el-icon><arrow-up /></el-icon>
              </div> -->
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="overview-card">
            <template #header>
              <div class="card-header">
                <span>评分</span>
                <el-tag type="primary">平均</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">{{ overview.rating.toFixed(1) }}分</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 工作区域 -->
    <div class="work-area">
      <el-row :gutter="20">
        <!-- 任务分配 -->
        <el-col :span="24">
          <el-card shadow="hover" class="work-card">
            <template #header>
              <div class="card-header">
                <span>任务分配</span>
                <el-button type="primary" link @click="handleViewAllAssignments">查看全部</el-button>
              </div>
            </template>
            <el-table :data="assignments" style="width: 100%">
              <el-table-column prop="orderNumber" label="订单号" width="180" />
              <el-table-column prop="plateNumber" label="车牌号" width="120" />
              <el-table-column prop="repairType" label="维修类型" width="120">
                <template #default="{ row }">
                  <el-tag :type="getRepairTypeTag(row.repairType)">
                    {{ getRepairTypeText(row.repairType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="problem" label="问题描述" min-width="200" show-overflow-tooltip />
              <el-table-column prop="assignmentTime" label="分配时间" width="180" />
              <el-table-column prop="status" label="状态" width="100">
                <template #default="{ row }">
                  <el-tag :type="getAssignmentStatusTag(row.status)">
                    {{ getAssignmentStatusText(row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200" fixed="right">
                <template #default="{ row }">
                  <el-button
              v-if="row.status === 'PENDING'"
              type="success"
              link
              @click="handleAcceptAssignment(row)"
            >
              接受
            </el-button>
            <el-button
              v-if="row.status === 'PENDING'"
              type="danger"
              link
              @click="handleRejectAssignment(row)"
            >
              拒绝
            </el-button>
            <el-button
              v-if="row.status === 'ACCEPTED'"
              type="primary"
              link
              @click="handleShowDetail(row)"
            >
              查看详情
            </el-button>
                  
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>

        <!-- 待处理订单 -->
        <el-col :span="24">
          <el-card shadow="hover" class="work-card">
            <template #header>
              <div class="card-header">
                <span>待处理订单</span>
                <el-button type="primary" link @click="handleViewAll">查看全部</el-button>
              </div>
            </template>
            <el-table :data="pendingOrders" style="width: 100%">
              <el-table-column prop="orderNo" label="订单号" width="180" />
              <el-table-column prop="vehiclePlate" label="车牌号" width="120" />
              <el-table-column prop="repairType" label="维修类型" width="120">
                <template #default="{ row }">
                  <el-tag :type="getRepairTypeTag(row.repairType)">
                    {{ getRepairTypeText(row.repairType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="problem" label="问题描述" min-width="200" show-overflow-tooltip />
              <el-table-column prop="createTime" label="创建时间" width="180" />
              <el-table-column label="操作" width="120" fixed="right">
                <template #default="{ row }">
                  <el-button type="primary" link @click="handleShowDetail(row)">查看详情</el-button>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template #default="{ row }">
                  <el-tag :type="getOrderStatusTag(row.status)">
                    {{ getOrderStatusText(row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="amount" label="金额" width="120">
                <template #default="{ row }">
                  ¥{{ row.amount.toFixed(2) }}
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowUp } from '@element-plus/icons-vue'
import { repairman } from '@/api'

const router = useRouter()

// 概览数据
const overview = ref({
  todayOrders: 0,
  orderTrend: 0,
  pendingOrders: 0,
  monthlyIncome: 0,
  incomeTrend: 0,
  rating: 0
})

// 待处理订单
const pendingOrders = ref([])

// 工作统计
const statisticsType = ref('week')
const statistics = ref({
  completedOrders: 0,
  repairHours: 0,
  income: 0,
  rating: 0
})

// 任务分配列表
const assignments = ref([])

// 获取维修类型标签
const getRepairTypeTag = (type) => {
  const map = {
    MAINTENANCE: 'info',
    REPAIR: 'warning',
    ACCIDENT: 'danger'
  }
  return map[type] || 'info'
}

// 获取维修类型文本
const getRepairTypeText = (type) => {
  const map = {
    MAINTENANCE: '常规保养',
    REPAIR: '故障维修',
    ACCIDENT: '事故维修'
  }
  return map[type] || type
}

// 获取任务分配状态标签
const getAssignmentStatusTag = (status) => {
  const map = {
    PENDING: 'warning',
    ACCEPTED: 'success',
    REJECTED: 'danger',
    CANCELED: 'danger',
  }
  return map[status] || status
}

// 获取任务分配状态文本
const getAssignmentStatusText = (status) => {
  const map = {
    PENDING: '待处理',
    ACCEPTED: '已接受',
    REJECTED: '已拒绝',
    CANCELED: '已取消',
  }
  return map[status] || status
}

// 获取状态标签
const getOrderStatusTag = (status) => {
  const map = {
    PENDING: 'info',
    PROCESSING: 'success',
    COMPLETED: 'success',
    CANCELLED: 'danger'
  }
  return map[status] || 'info'
}

// 获取状态文本
const getOrderStatusText = (status) => {
  const map = {
    PENDING: '待处理',
    PROCESSING: '维修中',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

// 获取数据
const fetchData = async () => {
  try {
    const [overviewRes, pendingRes, assignmentsRes] = await Promise.all([
      repairman.getOverview(),
      repairman.getOrders(),
      repairman.getAssignments()
    ])
    overview.value = overviewRes
    pendingOrders.value = pendingRes.list
    assignments.value = assignmentsRes.list
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  }
}

// 开始维修
const handleShowDetail = async (row) => {
  router.push('/repairman/orders')
}

// 接受任务分配
const handleAcceptAssignment = async (row) => {
  try {
    await ElMessageBox.confirm('确定接受此任务分配吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await repairman.acceptAssignment(row.assignmentId)
    ElMessage.success('已接受任务')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('操作失败:', error)
      ElMessage.error('操作失败')
    }
  }
}

// 拒绝任务分配
const handleRejectAssignment = async (row) => {
  try {
    await ElMessageBox.confirm('确定拒绝此任务分配吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await repairman.rejectAssignment(row.assignmentId)
    ElMessage.success('已拒绝任务')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('操作失败:', error)
      ElMessage.error('操作失败')
    }
  }
}

// 查看全部任务分配
const handleViewAllAssignments = () => {
  router.push('/repairman/assignments')
}

// 查看全部订单
const handleViewAll = () => {
  router.push('/repairman/orders')
}

// 监听统计类型变化
watch(statisticsType, () => {
  fetchData()
})

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

.statistics-content {
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

.chart-container {
  height: 300px;
}
</style> 