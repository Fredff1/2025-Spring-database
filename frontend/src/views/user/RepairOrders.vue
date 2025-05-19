<template>
  <div class="repair-orders">
    <div class="page-header">
      <h2 class="page-title">维修订单</h2>
      <el-button type="primary" @click="showCreateDialog">
        <el-icon><plus /></el-icon>
        创建订单
      </el-button>
    </div>

    <!-- 订单列表 -->
    <el-card shadow="hover">
      <el-table :data="orders" style="width: 100%">
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="vehiclePlate" label="车牌号" width="120" />
        <el-table-column prop="repairType" label="维修类型" width="120" />
        <el-table-column prop="problem" label="问题描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">
            ¥{{ row.amount?.toFixed(2) || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status === '待处理'"
              type="danger"
              link
              @click="handleCancel(row)"
            >
              取消
            </el-button>
            <el-button
              v-if="row.status === '已完成' && !row.isPaid"
              type="primary"
              link
              @click="handlePay(row)"
            >
              支付
            </el-button>
            <el-button
              v-if="row.status === '已完成' && row.isPaid && !row.isReviewed"
              type="success"
              link
              @click="handleReview(row)"
            >
              评价
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

    <!-- 创建订单对话框 -->
    <el-dialog
      v-model="createDialogVisible"
      title="创建维修订单"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="选择车辆" prop="vehicleId">
          <el-select v-model="form.vehicleId" placeholder="请选择车辆">
            <el-option
              v-for="item in vehicles"
              :key="item.id"
              :label="item.plateNumber"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="维修类型" prop="repairType">
          <el-select v-model="form.repairType" placeholder="请选择维修类型">
            <el-option label="常规保养" value="常规保养" />
            <el-option label="故障维修" value="故障维修" />
            <el-option label="钣金喷漆" value="钣金喷漆" />
            <el-option label="轮胎更换" value="轮胎更换" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="问题描述" prop="problem">
          <el-input
            v-model="form.problem"
            type="textarea"
            :rows="4"
            placeholder="请详细描述车辆问题"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="createDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleCreate">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 支付对话框 -->
    <el-dialog
      v-model="payDialogVisible"
      title="订单支付"
      width="400px"
    >
      <div class="pay-info">
        <p>订单号：{{ currentOrder?.orderNo }}</p>
        <p>维修类型：{{ currentOrder?.repairType }}</p>
        <p>支付金额：¥{{ currentOrder?.amount?.toFixed(2) }}</p>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="payDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmPay">确认支付</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 评价对话框 -->
    <el-dialog
      v-model="reviewDialogVisible"
      title="服务评价"
      width="400px"
    >
      <el-form
        ref="reviewFormRef"
        :model="reviewForm"
        :rules="reviewRules"
        label-width="80px"
      >
        <el-form-item label="评分" prop="rating">
          <el-rate v-model="reviewForm.rating" />
        </el-form-item>
        <el-form-item label="评价" prop="comment">
          <el-input
            v-model="reviewForm.comment"
            type="textarea"
            :rows="4"
            placeholder="请输入您的评价"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="reviewDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitReview">提交评价</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { order, vehicle } from '@/api'

// 订单列表
const orders = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 车辆列表
const vehicles = ref([])

// 对话框控制
const createDialogVisible = ref(false)
const payDialogVisible = ref(false)
const reviewDialogVisible = ref(false)
const currentOrder = ref(null)

// 创建订单表单
const formRef = ref(null)
const form = ref({
  vehicleId: '',
  repairType: '',
  problem: ''
})

// 评价表单
const reviewFormRef = ref(null)
const reviewForm = ref({
  rating: 5,
  comment: ''
})

// 表单验证规则
const rules = {
  vehicleId: [
    { required: true, message: '请选择车辆', trigger: 'change' }
  ],
  repairType: [
    { required: true, message: '请选择维修类型', trigger: 'change' }
  ],
  problem: [
    { required: true, message: '请输入问题描述', trigger: 'blur' }
  ]
}

// 评价表单验证规则
const reviewRules = {
  rating: [
    { required: true, message: '请选择评分', trigger: 'change' }
  ],
  comment: [
    { required: true, message: '请输入评价内容', trigger: 'blur' }
  ]
}

// 获取状态标签类型
const getStatusType = (status) => {
  const types = {
    '待处理': 'info',
    '处理中': 'warning',
    '已完成': 'success',
    '已取消': 'danger'
  }
  return types[status] || 'info'
}

// 获取订单列表
const fetchOrders = async () => {
  try {
    const res = await order.getList({
      page: currentPage.value,
      limit: pageSize.value
    })
    orders.value = res.list
    total.value = res.total
  } catch (error) {
    console.error('获取订单列表失败:', error)
    ElMessage.error('获取订单列表失败')
  }
}

// 获取车辆列表
const fetchVehicles = async () => {
  try {
    const res = await vehicle.getList()
    vehicles.value = res.list
  } catch (error) {
    console.error('获取车辆列表失败:', error)
    ElMessage.error('获取车辆列表失败')
  }
}

// 显示创建订单对话框
const showCreateDialog = () => {
  form.value = {
    vehicleId: '',
    repairType: '',
    problem: ''
  }
  createDialogVisible.value = true
}

// 处理创建订单
const handleCreate = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await order.create(form.value)
        ElMessage.success('创建成功')
        createDialogVisible.value = false
        fetchOrders()
      } catch (error) {
        console.error('创建失败:', error)
        ElMessage.error('创建失败')
      }
    }
  })
}

// 处理取消订单
const handleCancel = async (row) => {
  try {
    await order.cancel(row.id)
    ElMessage.success('取消成功')
    fetchOrders()
  } catch (error) {
    console.error('取消失败:', error)
    ElMessage.error('取消失败')
  }
}

// 处理支付
const handlePay = (row) => {
  currentOrder.value = row
  payDialogVisible.value = true
}

// 确认支付
const confirmPay = async () => {
  try {
    await order.pay(currentOrder.value.id)
    ElMessage.success('支付成功')
    payDialogVisible.value = false
    fetchOrders()
  } catch (error) {
    console.error('支付失败:', error)
    ElMessage.error('支付失败')
  }
}

// 处理评价
const handleReview = (row) => {
  currentOrder.value = row
  reviewForm.value = {
    rating: 5,
    comment: ''
  }
  reviewDialogVisible.value = true
}

// 提交评价
const submitReview = async () => {
  if (!reviewFormRef.value) return
  
  await reviewFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await order.review(currentOrder.value.id, reviewForm.value)
        ElMessage.success('评价成功')
        reviewDialogVisible.value = false
        fetchOrders()
      } catch (error) {
        console.error('评价失败:', error)
        ElMessage.error('评价失败')
      }
    }
  })
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
  fetchVehicles()
})
</script>

<style scoped>
.repair-orders {
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

.pay-info {
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
}

.pay-info p {
  margin: 8px 0;
  color: var(--text-secondary);
}
</style> 