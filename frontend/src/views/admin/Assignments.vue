<template>
  <div class="assignments">
    <div class="page-header">
      <h2 class="page-title">任务分配</h2>
    </div>

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
        <el-table-column prop="repairmanName" label="维修人员" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getAssignmentStatusTag(row.status)">
              {{ getAssignmentStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <!-- <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status === 'ACCEPTED'"
              type="primary"
              link
              @click="handleStartRepair(row)"
            >
              查看详情
            </el-button>
          </template>
        </el-table-column> -->
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
import { admin } from '@/api'
import { useRouter } from 'vue-router'

// 任务分配列表
const assignments = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const router = useRouter()

// 获取维修类型标签
const getRepairTypeTag = (type) => {
  const map = {
    MAINTENANCE: 'success',
    REPAIR: 'danger',
    PAINT: 'warning',
    TIRE: 'warning',
    OTHER: 'info'
  }
  return map[type] || 'info'
}

// 获取维修类型文本
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

// 获取数据
const fetchData = async () => {
  try {
    const res = await admin.getAssignments({
      page: currentPage.value,
      limit: pageSize.value
    })
    assignments.value = res.list
    total.value = res.total
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
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
  margin-bottom: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 