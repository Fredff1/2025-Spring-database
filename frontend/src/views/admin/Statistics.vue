<template>
  <div class="statistics">
    <div class="page-header">
      <h2 class="page-title">统计报表</h2>
      <div class="header-actions">
        <el-button type="primary" @click="handleExport">
          <el-icon><download /></el-icon>
          导出报表
        </el-button>
      </div>
    </div>

    <!-- 时间范围选择 -->
    <el-card shadow="hover" class="filter-card">
      <el-form :inline="true" :model="filterForm">
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :shortcuts="dateShortcuts"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 概览数据 -->
    <div class="overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card shadow="hover" class="overview-card">
            <template #header>
              <div class="card-header">
                <span>总订单数</span>
                <el-tag type="info">累计</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">{{ overview.totalOrders }}</div>
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
                <span>总收入</span>
                <el-tag type="success">累计</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">¥{{ overview.totalIncome.toFixed(2) }}</div>
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
                <span>总支出</span>
                <el-tag type="danger">累计</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">¥{{ overview.totalExpense.toFixed(2) }}</div>
              <div class="trend" :class="{ 'up': overview.expenseTrend > 0 }">
                {{ overview.expenseTrend > 0 ? '+' : '' }}{{ overview.expenseTrend }}%
                <el-icon><arrow-up /></el-icon>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="overview-card">
            <template #header>
              <div class="card-header">
                <span>净利润</span>
                <el-tag type="warning">累计</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">¥{{ overview.totalProfit.toFixed(2) }}</div>
              <div class="trend" :class="{ 'up': overview.profitTrend > 0 }">
                {{ overview.profitTrend > 0 ? '+' : '' }}{{ overview.profitTrend }}%
                <el-icon><arrow-up /></el-icon>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <template #header>
              <div class="card-header">
                <span>订单趋势</span>
                <el-radio-group v-model="orderTrendType" size="small">
                  <el-radio-button label="day">日</el-radio-button>
                  <el-radio-button label="week">周</el-radio-button>
                  <el-radio-button label="month">月</el-radio-button>
                </el-radio-group>
              </div>
            </template>
            <div class="chart-container">
              <!-- 这里放置订单趋势图表 -->
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <template #header>
              <div class="card-header">
                <span>收支趋势</span>
                <el-radio-group v-model="financeTrendType" size="small">
                  <el-radio-button label="day">日</el-radio-button>
                  <el-radio-button label="week">周</el-radio-button>
                  <el-radio-button label="month">月</el-radio-button>
                </el-radio-group>
              </div>
            </template>
            <div class="chart-container">
              <!-- 这里放置收支趋势图表 -->
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 详细数据 -->
    <div class="details">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card shadow="hover" class="detail-card">
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
        <el-col :span="12">
          <el-card shadow="hover" class="detail-card">
            <template #header>
              <div class="card-header">
                <span>收入构成</span>
              </div>
            </template>
            <div class="chart-container">
              <!-- 这里放置收入构成图表 -->
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 排行榜 -->
    <div class="rankings">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card shadow="hover" class="ranking-card">
            <template #header>
              <div class="card-header">
                <span>维修人员业绩排行</span>
              </div>
            </template>
            <el-table :data="repairmanRanking" style="width: 100%">
              <el-table-column type="index" label="排名" width="80" />
              <el-table-column prop="name" label="姓名" width="120" />
              <el-table-column prop="orderCount" label="订单数" width="100" />
              <el-table-column prop="income" label="收入">
                <template #default="{ row }">
                  ¥{{ row.income.toFixed(2) }}
                </template>
              </el-table-column>
              <el-table-column prop="rating" label="评分" width="100">
                <template #default="{ row }">
                  <el-rate v-model="row.rating" disabled show-score />
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover" class="ranking-card">
            <template #header>
              <div class="card-header">
                <span>配件使用排行</span>
              </div>
            </template>
            <el-table :data="partsRanking" style="width: 100%">
              <el-table-column type="index" label="排名" width="80" />
              <el-table-column prop="name" label="配件名称" min-width="150" />
              <el-table-column prop="type" label="类型" width="120">
                <template #default="{ row }">
                  {{ getTypeText(row.type) }}
                </template>
              </el-table-column>
              <el-table-column prop="usageCount" label="使用次数" width="100" />
              <el-table-column prop="amount" label="金额">
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
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Download, ArrowUp } from '@element-plus/icons-vue'
import { admin } from '@/api'

// 时间范围选择
const filterForm = ref({
  dateRange: []
})

// 日期快捷选项
const dateShortcuts = [
  {
    text: '最近一周',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
      return [start, end]
    }
  },
  {
    text: '最近一个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
      return [start, end]
    }
  },
  {
    text: '最近三个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
      return [start, end]
    }
  }
]

// 图表类型
const orderTrendType = ref('month')
const financeTrendType = ref('month')

// 概览数据
const overview = ref({
  totalOrders: 0,
  orderTrend: 0,
  totalIncome: 0,
  incomeTrend: 0,
  totalExpense: 0,
  expenseTrend: 0,
  totalProfit: 0,
  profitTrend: 0
})

// 排行榜数据
const repairmanRanking = ref([])
const partsRanking = ref([])

// 获取类型文本
const getTypeText = (type) => {
  const map = {
    engine: '发动机配件',
    brake: '制动系统',
    suspension: '悬挂系统',
    electrical: '电气系统',
    other: '其他'
  }
  return map[type] || type
}

// 获取统计数据
const fetchStatistics = async () => {
  try {
    const [overviewRes, repairmanRes, partsRes] = await Promise.all([
      admin.getStatisticsOverview(filterForm.value),
      admin.getRepairmanRanking(filterForm.value),
      admin.getPartsRanking(filterForm.value)
    ])
    overview.value = overviewRes
    repairmanRanking.value = repairmanRes
    partsRanking.value = partsRes
  } catch (error) {
    console.error('获取统计数据失败:', error)
    ElMessage.error('获取统计数据失败')
  }
}

// 查询
const handleFilter = () => {
  fetchStatistics()
}

// 重置
const resetFilter = () => {
  filterForm.value.dateRange = []
  fetchStatistics()
}

// 导出报表
const handleExport = async () => {
  try {
    await admin.exportStatistics(filterForm.value)
    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  }
}

onMounted(() => {
  fetchStatistics()
})
</script>

<style scoped>
.statistics {
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

.filter-card {
  margin-bottom: 24px;
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

.trend {
  font-size: 14px;
  color: var(--text-secondary);
}

.trend.up {
  color: var(--el-color-success);
}

.charts {
  margin-bottom: 24px;
}

.chart-card {
  height: 400px;
}

.chart-container {
  height: 320px;
}

.details {
  margin-bottom: 24px;
}

.detail-card {
  height: 400px;
}

.rankings {
  margin-bottom: 24px;
}

.ranking-card {
  height: 100%;
}
</style> 