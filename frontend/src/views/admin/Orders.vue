<template>
  <div class="orders">
    <div class="page-header">
      <h2 class="page-title">维修订单管理</h2>
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
            <el-option label="待接单" value="pending" />
            <el-option label="维修中" value="in_progress" />
            <el-option label="待支付" value="waiting_payment" />
            <el-option label="已完成" value="completed" />
            <el-option label="已取消" value="cancelled" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 订单列表 -->
    <el-card shadow="hover">
      <el-table :data="orders" style="width: 100%">
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
        <el-table-column prop="repairman" label="维修人员" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">
            ¥{{ row.amount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button
              v-if="row.status === 'pending'"
              type="success"
              link
              @click="handleAssign(row)"
            >
              分配
            </el-button>
            <el-button
              v-if="row.status === 'waiting_payment'"
              type="success"
              link
              @click="handleComplete(row)"
            >
              完成
            </el-button>
            <el-button
              v-if="['pending', 'in_progress'].includes(row.status)"
              type="danger"
              link
              @click="handleCancel(row)"
            >
              取消
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

    <!-- 查看订单对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="订单详情"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ currentOrder?.orderNumber }}</el-descriptions-item>
        <el-descriptions-item label="车牌号">{{ currentOrder?.plateNumber }}</el-descriptions-item>
        <el-descriptions-item label="维修类型">
          {{ getRepairTypeText(currentOrder?.repairType) }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(currentOrder?.status)">
            {{ getStatusText(currentOrder?.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="维修人员">{{ currentOrder?.repairman }}</el-descriptions-item>
        <el-descriptions-item label="金额">¥{{ currentOrder?.amount?.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentOrder?.createTime }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ currentOrder?.completeTime }}</el-descriptions-item>
        <el-descriptions-item label="问题描述" :span="2">{{ currentOrder?.problem }}</el-descriptions-item>
        <el-descriptions-item label="维修方案" :span="2">{{ currentOrder?.solution }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentOrder?.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 分配维修人员对话框 -->
    <el-dialog
      v-model="assignDialogVisible"
      title="分配维修人员"
      width="500px"
    >
      <el-form
        ref="assignFormRef"
        :model="assignForm"
        :rules="assignRules"
        label-width="100px"
      >
        <el-form-item label="维修人员" prop="repairmanId">
          <el-select v-model="assignForm.repairmanId" placeholder="请选择维修人员">
            <el-option
              v-for="item in repairmen"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="assignDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmAssign">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { admin } from '@/api'

// 搜索表单
const searchForm = ref({
  orderNumber: '',
  plateNumber: '',
  repairType: '',
  status: ''
})

// 订单列表
const orders = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 对话框控制
const viewDialogVisible = ref(false)
const assignDialogVisible = ref(false)
const currentOrder = ref(null)

// 维修人员列表
const repairmen = ref([])

// 分配表单
const assignFormRef = ref(null)
const assignForm = ref({
  repairmanId: ''
})

const assignRules = {
  repairmanId: [
    { required: true, message: '请选择维修人员', trigger: 'change' }
  ]
}

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

// 获取状态标签
const getStatusTag = (status) => {
  const map = {
    pending: 'info',
    in_progress: 'warning',
    waiting_payment: 'success',
    completed: 'success',
    cancelled: 'danger'
  }
  return map[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const map = {
    pending: '待接单',
    in_progress: '维修中',
    waiting_payment: '待支付',
    completed: '已完成',
    cancelled: '已取消'
  }
  return map[status] || status
}

// 获取订单列表
const fetchOrders = async () => {
  try {
    const res = await admin.getOrders({
      page: currentPage.value,
      limit: pageSize.value,
      ...searchForm.value
    })
    orders.value = res.list
    total.value = res.total
  } catch (error) {
    console.error('获取订单列表失败:', error)
    ElMessage.error('获取订单列表失败')
  }
}

// 获取维修人员列表
const fetchRepairmen = async () => {
  try {
    const res = await admin.getRepairmen({ status: 'active' })
    repairmen.value = res.list
  } catch (error) {
    console.error('获取维修人员列表失败:', error)
    ElMessage.error('获取维修人员列表失败')
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchOrders()
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

// 查看订单
const handleView = (row) => {
  currentOrder.value = row
  viewDialogVisible.value = true
}

// 分配维修人员
const handleAssign = (row) => {
  currentOrder.value = row
  assignForm.value.repairmanId = ''
  assignDialogVisible.value = true
}

// 确认分配
const confirmAssign = async () => {
  if (!assignFormRef.value) return
  
  await assignFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await admin.assignRepairman(currentOrder.value.id, assignForm.value.repairmanId)
        ElMessage.success('分配成功')
        assignDialogVisible.value = false
        fetchOrders()
      } catch (error) {
        console.error('分配失败:', error)
        ElMessage.error('分配失败')
      }
    }
  })
}

// 完成订单
const handleComplete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要完成该订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await admin.completeOrder(row.id)
    ElMessage.success('操作成功')
    fetchOrders()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('操作失败:', error)
      ElMessage.error('操作失败')
    }
  }
}

// 取消订单
const handleCancel = async (row) => {
  try {
    await ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await admin.cancelOrder(row.id)
    ElMessage.success('取消成功')
    fetchOrders()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消失败:', error)
      ElMessage.error('取消失败')
    }
  }
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchOrders()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchOrders()
}

onMounted(() => {
  fetchOrders()
  fetchRepairmen()
})
</script>

<style scoped>
.orders {
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

.search-card {
  margin-bottom: 24px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style> 