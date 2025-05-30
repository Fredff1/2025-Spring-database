<template>
  <div class="income">
    <div class="page-header">
      <h2 class="page-title">工时费收入</h2>
    </div>

    <!-- 时间范围选择 -->
    <el-card shadow="hover" class="filter-card">
      <el-form :model="filterForm" inline>
        <el-form-item label="月份">
          <el-date-picker
            v-model="filterForm.month"
            type="month"
            placeholder="选择月份"
            value-format="YYYY-MM"
            :shortcuts="monthShortcuts"
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
        <el-col :span="8">
          <el-card shadow="hover" class="overview-card">
            <template #header>
              <div class="card-header">
                <span>本月总收入</span>
                <el-tag type="success">总计</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">¥{{ overview.totalIncome?.toFixed(2) || '0.00' }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="overview-card">
            <template #header>
              <div class="card-header">
                <span>本月工时</span>
                <el-tag type="primary">总计</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">{{ overview.totalHours?.toFixed(1) || '0.0' }}</div>
              <div class="sub-value">小时</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="overview-card">
            <template #header>
              <div class="card-header">
                <span>时薪</span>
                <el-tag type="warning">当前</el-tag>
              </div>
            </template>
            <div class="card-content">
              <div class="value">¥{{ overview.hourlyMoneyRate?.toFixed(2) || '0.00' }}</div>
              <div class="sub-value">/小时</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 收入明细 -->
    <el-card shadow="hover" class="table-card">
      <template #header>
        <div class="card-header">
          <span>工时费记录</span>
          <el-button type="primary" link @click="handleExport">导出</el-button>
        </div>
      </template>
      <el-table :data="details" style="width: 100%" v-loading="loading">
        <el-table-column prop="orderId" label="订单号" width="180" />
        <el-table-column prop="month" label="月份" width="120">
          <template #default="{ row }">
            {{ formatMonth(row.month) }}
          </template>
        </el-table-column>
        <el-table-column prop="hourlyMoneyRate" label="时薪" width="120">
          <template #default="{ row }">
            ¥{{ row.hourlyMoneyRate?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="totalHours" label="工时" width="120">
          <template #default="{ row }">
            {{ row.totalHours?.toFixed(1) }}h
          </template>
        </el-table-column>
        <el-table-column prop="totalIncome" label="收入" width="120">
          <template #default="{ row }">
            ¥{{ row.totalIncome?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="settleTime" label="结算时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.settleTime) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { repairman } from '@/api'
import dayjs from 'dayjs'

// 月份快捷选项
const monthShortcuts = [
  {
    text: '本月',
    value: () => dayjs().format('YYYY-MM')
  },
  {
    text: '上个月',
    value: () => dayjs().subtract(1, 'month').format('YYYY-MM')
  },
  {
    text: '三个月前',
    value: () => dayjs().subtract(3, 'month').format('YYYY-MM')
  }
]

// 筛选表单
const filterForm = reactive({
  month: dayjs().format('YYYY-MM')
})

// 概览数据
const overview = ref({
  totalIncome: 0,
  totalHours: 0,
  hourlyMoneyRate: 0
})

// 收入明细数据
const loading = ref(false)
const details = ref([])

// 格式化月份
const formatMonth = (month) => {
  if (!month) return '-'
  return dayjs(month).format('YYYY年MM月')
}

// 格式化日期时间
const formatDateTime = (datetime) => {
  if (!datetime) return '-'
  return dayjs(datetime).format('YYYY-MM-DD HH:mm:ss')
}

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      month: filterForm.month
    }
    const res = await repairman.getLaborFeeLogs(params)
    details.value = res.list || []
    
    // 计算概览数据
    if (details.value.length > 0) {
      overview.value = {
        totalIncome: details.value.reduce((sum, item) => sum + (item.totalIncome || 0), 0),
        totalHours: details.value.reduce((sum, item) => sum + (item.totalHours || 0), 0),
        hourlyMoneyRate: details.value[0]?.hourlyMoneyRate || 0
      }
    } else {
      overview.value = {
        totalIncome: 0,
        totalHours: 0,
        hourlyMoneyRate: 0
      }
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 筛选
const handleFilter = () => {
  fetchData()
}

// 重置筛选
const handleReset = () => {
  filterForm.month = dayjs().format('YYYY-MM')
  handleFilter()
}

// 导出
const handleExport = async () => {
  try {
    await repairman.exportLaborFeeLogs({
      month: filterForm.month
    })
    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  }
}

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

.table-card {
  margin-bottom: 24px;
}
</style> 