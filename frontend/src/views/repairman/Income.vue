<template>
  <div class="income">
    <div class="page-header">
      <h2 class="page-title">收入统计</h2>
    </div>

    <!-- 时间范围选择 -->
    <el-card shadow="hover" class="filter-card">
      <el-form :model="filterForm" inline>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            :shortcuts="dateShortcuts"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 收入概览 -->
    <div class="overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card shadow="hover" class="overview-card">
            <template #header>
              <div class="card-header">
                <span>总收入</span>
                <el-tag type="success">总计</el-tag>
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
                <span>订单数</span>
                <el-tag type="primary">总计</el-tag>
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
                <span>平均订单金额</span>
                <el-tag type="warning">平均</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">¥{{ overview.averageAmount.toFixed(2) }}</div>
              <div class="trend" :class="{ 'up': overview.amountTrend > 0 }">
                {{ overview.amountTrend > 0 ? '+' : '' }}{{ overview.amountTrend }}%
                <el-icon><arrow-up /></el-icon>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="overview-card">
            <template #header>
              <div class="card-header">
                <span>平均评分</span>
                <el-tag type="info">平均</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">{{ overview.averageRating.toFixed(1) }}</div>
              <div class="sub-value">分</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts">
      <el-row :gutter="20">
        <!-- 收入趋势 -->
        <el-col :span="16">
          <el-card shadow="hover" class="chart-card">
            <template #header>
              <div class="card-header">
                <span>收入趋势</span>
                <el-radio-group v-model="trendType" size="small">
                  <el-radio-button label="day">日</el-radio-button>
                  <el-radio-button label="week">周</el-radio-button>
                  <el-radio-button label="month">月</el-radio-button>
                </el-radio-group>
              </div>
            </template>
            <div class="chart-container">
              <!-- 这里放置收入趋势图表 -->
            </div>
          </el-card>
        </el-col>

        <!-- 维修类型分布 -->
        <el-col :span="8">
          <el-card shadow="hover" class="chart-card">
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

    <!-- 收入明细 -->
    <el-card shadow="hover" class="table-card">
      <template #header>
        <div class="card-header">
          <span>收入明细</span>
          <el-button type="primary" link @click="handleExport">导出</el-button>
        </div>
      </template>
      <el-table :data="details" style="width: 100%" v-loading="loading">
        <el-table-column prop="orderNumber" label="订单号" width="180" />
        <el-table-column prop="plateNumber" label="车牌号" width="120" />
        <el-table-column prop="repairType" label="维修类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getRepairTypeTag(row.repairType)">
              {{ getRepairTypeText(row.repairType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">
            ¥{{ row.amount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="rating" label="评分" width="120">
          <template #default="{ row }">
            <el-rate
              v-model="row.rating"
              disabled
              show-score
              text-color="#ff9900"
            />
          </template>
        </el-table-column>
        <el-table-column prop="completeTime" label="完成时间" width="180" />
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
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
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { ArrowUp } from '@element-plus/icons-vue'
import { repairman } from '@/api'

// 时间范围快捷选项
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

// 筛选表单
const filterForm = reactive({
  dateRange: []
})

// 概览数据
const overview = ref({
  totalIncome: 0,
  incomeTrend: 0,
  totalOrders: 0,
  orderTrend: 0,
  averageAmount: 0,
  amountTrend: 0,
  averageRating: 0
})

// 趋势类型
const trendType = ref('day')

// 收入明细数据
const loading = ref(false)
const details = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

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
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      startDate: filterForm.dateRange?.[0],
      endDate: filterForm.dateRange?.[1],
      trendType: trendType.value
    }
    const [overviewRes, detailsRes] = await Promise.all([
      repairman.getIncomeOverview(params),
      repairman.getIncomeDetails(params)
    ])
    overview.value = overviewRes
    details.value = detailsRes.list
    total.value = detailsRes.total
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 筛选
const handleFilter = () => {
  currentPage.value = 1
  fetchData()
}

// 重置筛选
const handleReset = () => {
  filterForm.dateRange = []
  handleFilter()
}

// 分页
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchData()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchData()
}

// 导出
const handleExport = async () => {
  try {
    await repairman.exportIncome({
      startDate: filterForm.dateRange?.[0],
      endDate: filterForm.dateRange?.[1]
    })
    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  }
}

// 监听趋势类型变化
watch(trendType, () => {
  fetchData()
})

// 初始化
fetchData()
</script>

<style scoped>
.income {
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

.charts {
  margin-bottom: 24px;
}

.chart-card {
  margin-bottom: 24px;
}

.chart-container {
  height: 300px;
}

.table-card {
  margin-bottom: 24px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 