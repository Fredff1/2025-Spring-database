<template>
  <div class="history">
    <div class="page-header">
      <h2 class="page-title">历史记录</h2>
    </div>

    <!-- 搜索表单 -->
    <el-card shadow="hover" class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNumber" placeholder="请输入订单号" clearable />
        </el-form-item>
        <el-form-item label="车牌号">
          <el-input v-model="searchForm.plateNumber" placeholder="请输入车牌号" clearable />
        </el-form-item>
        <el-form-item label="维修类型">
          <el-select v-model="searchForm.repairType" placeholder="请选择维修类型" clearable>
            <el-option label="常规保养" value="maintenance" />
            <el-option label="故障维修" value="repair" />
            <el-option label="事故维修" value="accident" />
          </el-select>
        </el-form-item>
        <el-form-item label="评分">
          <el-select v-model="searchForm.rating" placeholder="请选择评分" clearable>
            <el-option label="5星" value="5" />
            <el-option label="4星" value="4" />
            <el-option label="3星" value="3" />
            <el-option label="2星" value="2" />
            <el-option label="1星" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 历史记录列表 -->
    <el-card shadow="hover" class="table-card">
      <el-table :data="history" style="width: 100%" v-loading="loading">
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
        <el-table-column prop="solution" label="维修方案" min-width="200" show-overflow-tooltip />
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
        <el-table-column prop="comment" label="评价" min-width="200" show-overflow-tooltip />
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">
            ¥{{ row.amount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="completeTime" label="完成时间" width="180" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
          </template>
        </el-table-column>
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

    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="维修详情"
      width="800px"
      destroy-on-close
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ currentRecord.orderNumber }}</el-descriptions-item>
        <el-descriptions-item label="车牌号">{{ currentRecord.plateNumber }}</el-descriptions-item>
        <el-descriptions-item label="维修类型">
          {{ getRepairTypeText(currentRecord.repairType) }}
        </el-descriptions-item>
        <el-descriptions-item label="评分">
          <el-rate
            v-model="currentRecord.rating"
            disabled
            show-score
            text-color="#ff9900"
          />
        </el-descriptions-item>
        <el-descriptions-item label="问题描述" :span="2">
          {{ currentRecord.problem }}
        </el-descriptions-item>
        <el-descriptions-item label="维修方案" :span="2">
          {{ currentRecord.solution }}
        </el-descriptions-item>
        <el-descriptions-item label="使用配件" :span="2">
          <el-table :data="currentRecord.parts" style="width: 100%">
            <el-table-column prop="name" label="配件名称" />
            <el-table-column prop="specification" label="规格" />
            <el-table-column prop="quantity" label="数量" width="100" />
            <el-table-column prop="price" label="单价" width="120">
              <template #default="{ row }">
                ¥{{ row.price.toFixed(2) }}
              </template>
            </el-table-column>
          </el-table>
        </el-descriptions-item>
        <el-descriptions-item label="维修费用">
          ¥{{ currentRecord.amount.toFixed(2) }}
        </el-descriptions-item>
        <el-descriptions-item label="完成时间">
          {{ currentRecord.completeTime }}
        </el-descriptions-item>
        <el-descriptions-item label="客户评价" :span="2">
          {{ currentRecord.comment }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { repairman } from '@/api'

// 搜索表单
const searchForm = reactive({
  orderNumber: '',
  plateNumber: '',
  repairType: '',
  rating: '',
  dateRange: []
})

// 历史记录列表数据
const loading = ref(false)
const history = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 查看详情
const viewDialogVisible = ref(false)
const currentRecord = ref({})

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

// 获取历史记录列表
const fetchHistory = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      ...searchForm,
      startDate: searchForm.dateRange?.[0],
      endDate: searchForm.dateRange?.[1]
    }
    const res = await repairman.getHistory(params)
    history.value = res.list
    total.value = res.total
  } catch (error) {
    console.error('获取历史记录失败:', error)
    ElMessage.error('获取历史记录失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchHistory()
}

// 重置搜索
const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  handleSearch()
}

// 分页
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchHistory()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchHistory()
}

// 查看详情
const handleView = async (row) => {
  try {
    const res = await repairman.getHistoryDetail(row.id)
    currentRecord.value = res
    viewDialogVisible.value = true
  } catch (error) {
    console.error('获取详情失败:', error)
    ElMessage.error('获取详情失败')
  }
}

// 初始化
fetchHistory()
</script>

<style scoped>
.history {
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

.search-card {
  margin-bottom: 24px;
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