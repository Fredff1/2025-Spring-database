<template>
  <div class="assignments">
    <div class="page-header">
      <h2 class="page-title">任务分配</h2>
    </div>

    <!-- 搜索栏 -->
    <el-card shadow="hover" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNumber" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="车牌号">
          <el-input v-model="searchForm.plateNumber" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="维修类型">
          <el-select v-model="searchForm.repairType" placeholder="请选择维修类型">
            <el-option label="全部" value="" />
            <el-option label="常规保养" value="maintenance" />
            <el-option label="故障维修" value="repair" />
            <el-option label="事故维修" value="accident" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态">
            <el-option label="全部" value="" />
            <el-option label="待接受" value="pending" />
            <el-option label="已接受" value="accepted" />
            <el-option label="已拒绝" value="rejected" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 任务分配列表 -->
    <el-card shadow="hover">
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
        <el-table-column prop="accepted" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getAssignmentStatusTag(row.accepted)">
              {{ getAssignmentStatusText(row.accepted) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.accepted === null"
              type="success"
              link
              @click="handleAcceptAssignment(row)"
            >
              接受
            </el-button>
            <el-button
              v-if="row.accepted === null"
              type="danger"
              link
              @click="handleRejectAssignment(row)"
            >
              拒绝
            </el-button>
            <el-button
              v-if="row.accepted === true"
              type="primary"
              link
              @click="handleStartRepair(row)"
            >
              开始维修
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { repairman } from '@/api'

// 搜索表单
const searchForm = ref({
  orderNumber: '',
  plateNumber: '',
  repairType: '',
  status: ''
})

// 任务分配列表
const assignments = ref([])
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

// 获取任务分配状态标签
const getAssignmentStatusTag = (accepted) => {
  if (accepted === null) return 'warning'
  return accepted ? 'success' : 'danger'
}

// 获取任务分配状态文本
const getAssignmentStatusText = (accepted) => {
  if (accepted === null) return '待接受'
  return accepted ? '已接受' : '已拒绝'
}

// 获取数据
const fetchData = async () => {
  try {
    const res = await repairman.getAssignments({
      page: currentPage.value,
      limit: pageSize.value,
      ...searchForm.value
    })
    assignments.value = res.list
    total.value = res.total
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchData()
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    orderNumber: '',
    plateNumber: '',
    repairType: '',
    status: ''
  }
  handleSearch()
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

// 开始维修
const handleStartRepair = async (row) => {
  try {
    await repairman.startRepair(row.orderId)
    ElMessage.success('已开始维修')
    fetchData()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 分页大小改变
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchData()
}

// 页码改变
const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchData()
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.assignments {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-title {
  margin: 0;
  font-size: 24px;
  font-weight: 500;
}

.search-card {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style> 