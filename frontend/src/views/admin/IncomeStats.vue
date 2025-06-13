<template>
  <div class="income-stats">
    <div class="page-header">
      <h2 class="page-title">财务管理</h2>
    </div>
    <!-- 收入明细 -->
    <el-card shadow="hover" class="table-card">
      <template #header>
        <div class="card-header">
          <span>工时费记录</span>
          <el-button type="primary" link @click="handleExport">导出</el-button>
        </div>
      </template>
      <div>注：工时费记录与维修记录一一对应，修改请前往订单页面与维修记录同步修改</div>
      <el-table :data="details" style="width: 100%" v-loading="loading">
        <el-table-column prop="repairmanName" label="维修人员" width="180" />
        <el-table-column prop="orderId" label="发放订单号" width="180" />
        
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

      <!-- 分页 -->
      <div class="pagination-container">
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { admin } from '@/api'
import dayjs from 'dayjs'



// 筛选表单
const filterForm = reactive({
  dateRange: [
    dayjs().subtract(30, 'day').format('YYYY-MM-DD'),
    dayjs().format('YYYY-MM-DD')
  ]
})

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 概览数据
const overview = ref({
  totalIncome: 0,
  totalHours: 0,
  averageHourlyRate: 0
})

// 收入明细数据
const loading = ref(false)
const details = ref([])


const formatMonth = (month) => {
  if (!month) return '-'
  return dayjs(month).format('YYYY年MM月')
}

// 格式化日期时间
const formatDateTime = (datetime) => {
  if (!datetime) return '-'
  return dayjs(datetime).format('YYYY-MM-DDTHH:mm:ss')
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value
    }
    const res = await admin.getIncomeStats(params)
    details.value = res.list || []
    total.value = res.total || 0
    

  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleDateRangeChange = () => {
  currentPage.value = 1
  handleFilter()
}

const handleFilter = () => {
  fetchData()
}

const handleReset = () => {
  filterForm.dateRange = [
    dayjs().subtract(30, 'day').format('YYYY-MM-DD'),
    dayjs().format('YYYY-MM-DD')
  ]
  currentPage.value = 1
  handleFilter()
}

// 导出
const handleExport = async () => {
  ElMessage.warning('导出功能暂未支持')
  return 
  try {
    await repairman.exportIncomeStats({
      startDate: filterForm.dateRange[0],
      endDate: filterForm.dateRange[1]
    })
    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  }
}


const handleSizeChange = (val) => {
  pageSize.value = val
  fetchData()
}


const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchData()
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.income-stats {
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

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 