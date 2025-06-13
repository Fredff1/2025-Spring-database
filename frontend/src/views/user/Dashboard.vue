<template>
  <div class="dashboard">
    <h2 class="page-title">首页</h2>
    
    <!-- 统计卡片 -->
    <div class="stat-cards">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card shadow="hover" class="stat-card">
            <template #header>
              <div class="card-header">
                <span>我的车辆</span>
                <el-icon><Van /></el-icon>
              </div>
            </template>
            <div class="card-content">
              <div class="number">{{ stats.vehicleCount }}</div>
              <div class="label">辆</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="stat-card">
            <template #header>
              <div class="card-header">
                <span>维修订单</span>
                <el-icon><Tools /></el-icon>
              </div>
            </template>
            <div class="card-content">
              <div class="number">{{ stats.orderCount }}</div>
              <div class="label">个</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="stat-card">
            <template #header>
              <div class="card-header">
                <span>维修历史</span>
                <el-icon><Document /></el-icon>
              </div>
            </template>
            <div class="card-content">
              <div class="number">{{ stats.historyCount }}</div>
              <div class="label">次</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 最近订单 -->
    <el-card class="recent-orders" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>最近订单</span>
          <el-button type="primary" link @click="goToOrders">查看全部</el-button>
        </div>
      </template>
      <el-table :data="recentOrders" style="width: 100%">
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="vehiclePlate" label="车牌号" width="120" />
        <el-table-column label="维修类型" width="120">
          <template #default="{ row }">
            {{ getRepairTypeText(row.repairType) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="amount" label="金额" width="120">
          <!-- <template #default="{ row }">
            ¥{{ row.amount.toFixed(2) }}
          </template> -->
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Van, Tools, Document } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { user } from '@/api'

const router = useRouter()

// 统计数据
const stats = ref({
  vehicleCount: 0,
  orderCount: 0,
  historyCount: 0
})

// 最近订单
const recentOrders = ref([])


const goToOrders = () => {
  router.push('/user/repair-orders')
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

const getStatusType = (status) => {
  const map = {
    PENDING: 'warning',
    PROCESSING: 'success',
    COMPLETED: 'success',
    CANCELLED: 'danger',
  }
  return map[status] || status
}

const getStatusText = (status) => {
  const map = {
    PENDING: '待处理',
    PROCESSING: '维修中',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

const fetchData = async () => {
  try {
    const statsRes = await user.getStats()
    stats.value = statsRes

    const ordersRes = await user.getRepairOrders({ page: 1, limit: 5 })
    recentOrders.value = ordersRes.list
    
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
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

.page-title {
  margin-bottom: 24px;
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
}

.stat-cards {
  margin-bottom: 24px;
}

.stat-card {
  height: 240px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100px;
}

.number {
  font-size: 36px;
  font-weight: 600;
  color: var(--primary);
}

.label {
  margin-top: 8px;
  color: var(--text-secondary);
}

.recent-orders {
  margin-bottom: 24px;
}
</style> 