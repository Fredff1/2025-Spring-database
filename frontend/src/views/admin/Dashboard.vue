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
              <div class="trend" :class="{ 'up': overview.orderTrend > 0 }">
                {{ overview.orderTrend > 0 ? '+' : '' }}{{ overview.orderTrend }}%
                <el-icon><arrow-up /></el-icon>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="overview-card">
            <template #header>
              <div class="card-header">
                <span>待处理</span>
                <el-tag type="warning">待处理</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">{{ overview.pendingOrders }}</div>
              <div class="sub-value">个待处理订单</div>
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
              <div class="trend" :class="{ 'up': overview.incomeTrend > 0 }">
                {{ overview.incomeTrend > 0 ? '+' : '' }}{{ overview.incomeTrend }}%
                <el-icon><arrow-up /></el-icon>
              </div>
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
              <div class="value">{{ overview.rating.toFixed(1) }}</div>
              <div class="sub-value">分</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 工作区域 -->
    <div class="work-area">
      <el-row :gutter="20">
        <!-- 待处理订单 -->
        <el-col :span="16">
          <el-card shadow="hover" class="work-card">
            <template #header>
              <div class="card-header">
                <span>待处理订单</span>
                <el-button type="primary" link @click="handleViewAll">查看全部</el-button>
              </div>
            </template>
            <el-table :data="pendingOrders" style="width: 100%">
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
              <el-table-column prop="createTime" label="创建时间" width="180" />
              <el-table-column label="操作" width="120" fixed="right">
                <template #default="{ row }">
                  <el-button type="primary" link @click="handleStartRepair(row)">开始维修</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>

        <!-- 工作统计 -->
        <el-col :span="8">
          <el-card shadow="hover" class="work-card">
            <template #header>
              <div class="card-header">
                <span>工作统计</span>
                <el-radio-group v-model="statisticsType" size="small">
                  <el-radio-button label="week">本周</el-radio-button>
                  <el-radio-button label="month">本月</el-radio-button>
                </el-radio-group>
              </div>
            </template>
            <div class="statistics-content">
              <div class="stat-item">
                <div class="stat-label">完成订单</div>
                <div class="stat-value">{{ statistics.completedOrders }}</div>
              </div>
              <div class="stat-item">
                <div class="stat-label">维修时长</div>
                <div class="stat-value">{{ statistics.repairHours }}小时</div>
              </div>
              <div class="stat-item">
                <div class="stat-label">收入</div>
                <div class="stat-value">¥{{ statistics.income.toFixed(2) }}</div>
              </div>
              <div class="stat-item">
                <div class="stat-label">评分</div>
                <div class="stat-value">{{ statistics.rating.toFixed(1) }}分</div>
              </div>
            </div>
          </el-card>

          <!-- 维修类型分布 -->
          <el-card shadow="hover" class="work-card">
            <template #header>
              <div class="card-header">
                <span>维修类型分布</span>
              </div>
            </template>
            <div class="chart-container">
              <!-- 这里放置维修类型分布图表 -->
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

// 获取维修类型标签
const getRepairTypeTag = (type) => {
  const map = {
    maintenance: 'info',
    repair: 'warning',
    accident: 'danger'
  }
  return map[type] || 'info'
}

// 获取维修类型文本
const getRepairTypeText = (type) => {
  const map = {
    maintenance: '常规保养',
    repair: '故障维修',
    accident: '事故维修'
  }
  return map[type] || type
}

// 获取数据
const fetchData = async () => {
  try {
    const [overviewRes, pendingRes, statisticsRes] = await Promise.all([
      admin.getOverview(),
      admin.getPendingOrders(),
      admin.getStatistics({ type: statisticsType.value })
    ])
    overview.value = overviewRes
    pendingOrders.value = pendingRes
    statistics.value = statisticsRes
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  }
}

// 开始维修
const handleStartRepair = async (row) => {
  try {
    await admin.startRepair(row.id)
    ElMessage.success('已开始维修')
    fetchData()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
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

// 获取统计数据
const fetchStatistics = async () => {
  try {
    const res = await admin.getStatistics()
    statistics.value = res
  } catch (error) {
    console.error('获取统计数据失败:', error)
    ElMessage.error('获取统计数据失败')
  }
}

// 获取最近订单
const fetchRecentOrders = async () => {
  try {
    const res = await admin.getRecentOrders()
    recentOrders.value = res
  } catch (error) {
    console.error('获取最近订单失败:', error)
    ElMessage.error('获取最近订单失败')
  }
}

// 获取系统状态
const fetchSystemStatus = async () => {
  try {
    const res = await admin.getSystemStatus()
    systemStatus.value = res
  } catch (error) {
    console.error('获取系统状态失败:', error)
    ElMessage.error('获取系统状态失败')
  }
}
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