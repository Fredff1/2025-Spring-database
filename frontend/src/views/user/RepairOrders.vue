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
        <el-table-column prop="orderNo" label="订单号" width="120" />
        <el-table-column prop="vehiclePlate" label="车牌号" width="100" />
        <el-table-column label="维修类型" width="150">
        <template #default="{ row }">
          {{ getRepairTypeText(row.repairType) }}
        </template>
      </el-table-column>
        <el-table-column prop="problem" label="问题描述" min-width="120" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="140" />
        <el-table-column prop="amount" label="金额" width="100">
          <template #default="{ row }">
            ¥{{ row.amount?.toFixed(2) || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status === 'PENDING'"
              type="danger"
              link
              @click="handleCancel(row)"
            >
              取消
            </el-button>
            <el-button
              v-if="row.status === 'COMPLETED' && !row.isPaid"
              type="primary"
              link
              @click="handlePay(row)"
            >
              支付
            </el-button>
            <el-button
              type="warning"
              link
              @click="handleFeedback(row)"
            >
              反馈
            </el-button>
            <el-button
              type="primary"
              link
              @click="handleView(row)"
            >
              详情
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
      width="800px"
    >
      <div class="order-list">
        <div v-for="(order, index) in orderForms" :key="index" class="order-item">
          <div class="order-header">
            <h3>订单 #{{ index + 1 }}</h3>
            <el-button 
              v-if="orderForms.length > 1"
              type="danger" 
              link 
              @click="removeOrder(index)"
            >
              删除
            </el-button>
          </div>
          <el-form
            :ref="el => { if (el) formRefs[index] = el }"
            :model="order"
            :rules="rules"
            label-width="100px"
          >
            <el-form-item label="选择车辆" prop="vehicleId">
              <el-select v-model="order.vehicleId" placeholder="请选择车辆">
                <el-option
                  v-for="item in vehicles"
                  :key="item.licensePlate"
                  :label="item.licensePlate"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="维修类型" prop="faultType">
              <el-select v-model="order.faultType" placeholder="请选择维修类型">
                <el-option label="常规保养" value="MAINTENANCE" />
                <el-option label="通用维修" value="REPAIR" />
                <el-option label="钣金喷漆" value="PAINT" />
                <el-option label="轮胎更换" value="TIRE" />
                <el-option label="电路系统修复" value="ELECTRICAL" />
                <el-option label="车身修复" value="BODYWORK" />
                <el-option label="发动机维修" value="ENGINE" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
            <el-form-item label="问题描述" prop="description">
              <el-input
                v-model="order.description"
                type="textarea"
                :rows="4"
                placeholder="请详细描述车辆问题"
              />
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="add-order">
        <el-button type="primary" link @click="addOrder">
          <el-icon><plus /></el-icon>
          添加订单
        </el-button>
      </div>
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
    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="维修详情"
      width="800px"
    >
      <el-tabs v-model="activeTab">
        <!-- 订单信息 -->
        <el-tab-pane label="订单信息" name="order">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="订单号">{{ currentOrder?.orderNo }}</el-descriptions-item>
            <el-descriptions-item label="车牌号">{{ currentOrder?.vehiclePlate }}</el-descriptions-item>
            <el-descriptions-item label="维修类型">
              {{ getRepairTypeText(currentOrder?.repairType) }}
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="getStatusType(currentOrder?.status)">
                {{ getStatusText(currentOrder?.status) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ currentOrder?.createTime }}</el-descriptions-item>
            <el-descriptions-item label="更新时间">{{ currentOrder?.updateTime }}</el-descriptions-item>
            <el-descriptions-item label="问题描述" :span="2">{{ currentOrder?.problem }}</el-descriptions-item>
            <el-descriptions-item label="维修人员" :span="2">
            <el-table :data="currentOrder.repairmanBaseInfos" style="width: 100%">
              <el-table-column prop="repairmanName" label="维修人" />
              <el-table-column prop="repairmanNumber" label="维修人工号"/>
            </el-table>
          </el-descriptions-item>
            <el-descriptions-item label="金额">¥{{ currentOrder?.amount?.toFixed(2) }}</el-descriptions-item>
            <el-descriptions-item label="支付状态">
              <el-tag :type="currentOrder?.isPaid ? 'success' : 'warning'">
                {{ currentOrder?.isPaid ? '已支付' : '未支付' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <!-- 维修记录 -->
        <el-tab-pane label="维修记录" name="records">
          <template v-if="repairRecords.length > 0">
            <el-timeline>
              <el-timeline-item
                v-for="record in repairRecords"
                :key="record.recordId"
                :timestamp="record.createTime"
                :type="getRecordTypeTag(record.status)"
              >
                <el-card class="record-card">
                  <template #header>
                    <div class="record-header">
                      <span><strong>维修人员:{{record.repairmanName}}</strong> </span>
                      <el-tag :type="getStatusType(record.status)" size="small">
                        {{ getStatusText(record.status) }}
                      </el-tag>
                    </div>
                  </template>
                  <div class="record-content">
                    <p><strong>故障描述：</strong>{{ record.faultDescription }}</p>
                    <p><strong>维修结果：</strong>{{ record.repairResult }}</p>
                  </div>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </template>
          <el-empty v-else description="暂无维修记录" />
        </el-tab-pane>

        <!-- 反馈记录 -->
        <el-tab-pane label="反馈记录" name="feedback">
          <template v-if="feedbackList.length > 0">
            <el-timeline>
              <el-timeline-item
                v-for="feedback in feedbackList"
                :key="feedback.feedbackId"
                :timestamp="feedback.feedbackTime"
                :type="getFeedbackTypeTag(feedback.type)"
              >
                <el-card class="feedback-card">
                  <template #header>
                    <div class="feedback-header">
                      <el-tag :type="getFeedbackTypeTag(feedback.type)" size="small">
                        {{ getFeedbackTypeText(feedback.type) }}
                      </el-tag>
                    </div>
                  </template>
                  <div class="feedback-content">
                    <el-rate
                      v-if="feedback.type === 'RATING' && feedback.rating"
                      v-model="feedback.rating"
                      disabled
                      show-score
                      text-color="#ff9900"
                    />
                    <p>{{ feedback.description }}</p>
                    <p class="feedback-content"v-if="feedback.response"><strong>系统回复</strong></p>
                    <p class="feedback-content"v-if="feedback.response">{{ feedback.response }}</p>
                  </div>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </template>
          <el-empty v-else description="暂无反馈记录" />
        </el-tab-pane>
      </el-tabs>
    </el-dialog>

    <!-- 反馈对话框 -->
    <el-dialog
      v-model="feedbackDialogVisible"
      title="订单反馈"
      width="500px"
    >
      <el-form
        ref="feedbackFormRef"
        :model="feedbackForm"
        :rules="feedbackRules"
        label-width="100px"
      >
        <el-form-item label="反馈类型" prop="feedbackType">
          <el-select v-model="feedbackForm.feedbackType" placeholder="请选择反馈类型">
            <el-option label="评分反馈" value="RATING" />
            <el-option label="催单反馈" value="URGENT" />
            <el-option label="一般反馈" value="GENERAL" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="feedbackForm.feedbackType === 'RATING'" label="服务评分" prop="rating">
          <el-rate
            v-model="feedbackForm.rating"
            show-score
            text-color="#ff9900"
          />
        </el-form-item>
        <el-form-item label="反馈内容" prop="description">
          <el-input
            v-model="feedbackForm.description"
            type="textarea"
            :rows="4"
            :placeholder="getFeedbackPlaceholder(feedbackForm.feedbackType)"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="feedbackDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitFeedback">提交反馈</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted,  reactive } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage ,ElMessageBox} from 'element-plus'
import { user} from '@/api'
import { getToken } from '@/utils/auth'

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
const viewDialogVisible = ref(false)
const feedbackDialogVisible = ref(false)
const activeTab = ref('order')
const repairRecords = ref([])
const materialUsages = ref([])
const feedbackList = ref([])

// 创建订单相关
const formRefs = ref([])
const orderForms = ref([])

// 创建订单表单
const form = reactive({
  vehicleId: '',
  faultType: '',
  description: ''
})

// 反馈表单
const feedbackFormRef = ref(null)
const feedbackForm = reactive({
  orderId: null,
  feedbackType: '',
  rating: 5,
  description: ''
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

// 反馈表单验证规则
const feedbackRules = {
  feedbackType: [
    { required: true, message: '请选择反馈类型', trigger: 'change' }
  ],
  rating: [
    { required: true, message: '请选择服务评分', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入反馈内容', trigger: 'blur' },
    { min: 5, max: 500, message: '反馈内容长度在 5 到 500 个字符之间', trigger: 'blur' }
  ]
}

const getStatusType = (status) => {
  const map = {
    PENDING: 'warning',
    PROCESSING: 'success',
    COMPLETED: 'success',
    CANCELLED: 'danger',
  }
  return map[status] || status
}

const getStatusText = (status) => {
  const map = {
    PENDING: '待处理',
    PROCESSING: '维修中',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

const fetchOrders = async () => {
  try {
    const res = await user.getRepairOrders({
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

const fetchVehicles = async () => {
  try {
    const res = await user.getVehicles()
    if (Array.isArray(res)) {
      vehicles.value = res
    } else if (res.list) {
      vehicles.value = res.list
    } else {
      vehicles.value = []
    }
  } catch (error) {
    console.error('获取车辆列表失败:', error)
    ElMessage.error('获取车辆列表失败')
    vehicles.value = []
  }
}

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

const showCreateDialog = async () => {
  orderForms.value = [{
    vehicleId: '',
    faultType: '',
    description: ''
  }]
  formRefs.value = []
  
  if (vehicles.value.length === 0) {
    await fetchVehicles()
  }
  
  createDialogVisible.value = true
}

const addOrder = () => {
  orderForms.value.push({
    vehicleId: '',
    faultType: '',
    description: ''
  })
}


const removeOrder = (index) => {
  orderForms.value.splice(index, 1)
}

const handleCreate = async () => {
  try {
    const validations = await Promise.all(
      formRefs.value.map(form => form.validate())
    )
    
    if (validations.some(valid => !valid)) {
      return
    }

    const response = await user.createOrderList(orderForms.value)
    ElMessage.success('创建成功')
    createDialogVisible.value = false
    fetchOrders()
    
  } catch (error) {
    console.error('创建失败:', error)
    ElMessage.error('创建失败')
  }
}

const handleCancel = async (row) => {
  try {
    await ElMessageBox.confirm(
      `你确定要取消订单 ${row.orderNo} 吗？`,
      '确认取消',
      {
        confirmButtonText: '确定',
        cancelButtonText: '再想想',
        type: 'warning',
      }
    )
    await user.cancelOrder(row.id)
    ElMessage.success('取消成功')
    fetchOrders()
  } catch (err) {
    if (err !== 'cancel') {
      console.error('取消失败:', err)
      ElMessage.error('取消失败')
    }
  }
}

const handlePay = (row) => {
  currentOrder.value = row
  payDialogVisible.value = true
}

const confirmPay = async () => {
  try {
    await user.payOrder(currentOrder.value.id)
    ElMessage.success('支付成功')
    payDialogVisible.value = false
    fetchOrders()
  } catch (error) {
    console.error('支付失败:', error)
    ElMessage.error('支付失败')
  }
}

const handleSizeChange = (val) => {
  pageSize.value = val
  fetchOrders()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchOrders()
}

const getFeedbackTypeTag = (type) => {
  const map = {
    RATING: 'success',
    URGENT: 'warning',
    GENERAL: 'info'
  }
  return map[type] || 'info'
}

const getFeedbackTypeText = (type) => {
  const map = {
    RATING: '评分反馈',
    URGENT: '催单反馈',
    GENERAL: '一般反馈'
  }
  return map[type] || type
}

const getRecordTypeTag = (status) => {
  const map = {
    '待处理': 'info',
    '处理中': 'warning',
    '已完成': 'success'
  }
  return map[status] || 'info'
}



const handleView = async (row) => {
  try {
    const [records, materials, feedbacks] = await Promise.all([
      user.getRepairRecords(row.id),
      user.getMaterialUsages(row.id),
      user.getFeedbackList(row.id)
    ])
    currentOrder.value = row
    repairRecords.value = records
    materialUsages.value = materials
    feedbackList.value = feedbacks
    viewDialogVisible.value = true
  } catch (error) {
    console.error('获取维修详情失败:', error)
    ElMessage.error('获取维修详情失败')
  }
}


const getFeedbackPlaceholder = (type) => {
  const placeholders = {
    RATING: '请输入您的评价内容',
    URGENT: '请描述您需要催单的原因',
    GENERAL: '请输入您的反馈内容'
  }
  return placeholders[type] || '请输入反馈内容'
}


const handleFeedback = (row) => {
  currentOrder.value = row
  feedbackForm.orderId = row.id
  feedbackForm.feedbackType = ''
  feedbackForm.rating = 5
  feedbackForm.description = ''
  feedbackDialogVisible.value = true
}


const submitFeedback = async () => {
  if (!feedbackFormRef.value) return
  await feedbackFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const submitData = {
          orderId: feedbackForm.orderId,
          feedbackType: feedbackForm.feedbackType,
          rating: feedbackForm.feedbackType === 'RATING' ? feedbackForm.rating : null,
          description: feedbackForm.description
        }
        await user.submitFeedback(submitData)
        ElMessage.success('反馈提交成功')
        feedbackDialogVisible.value = false
        if (viewDialogVisible.value) {
          const feedbacks = await user.getFeedbackList(currentOrder.value.id)
          feedbackList.value = feedbacks
        }
      } catch (error) {
        console.error('提交反馈失败:', error)
        ElMessage.error('提交反馈失败')
      }
    }
  })
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

.record-card,
.feedback-card {
  margin-bottom: 8px;
}

.record-header,
.feedback-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.record-content,
.feedback-content {
  margin-top: 8px;
}

.record-content p,
.feedback-content p {
  margin: 8px 0;
}

.order-list {
  max-height: 600px;
  overflow-y: auto;
  padding-right: 10px;
}

.order-item {
  padding: 20px;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  margin-bottom: 20px;
  background-color: var(--bg-color);
}

.order-item:last-child {
  margin-bottom: 0;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.order-header h3 {
  margin: 0;
  font-size: 16px;
  color: var(--text-primary);
}

.add-order {
  margin-top: 20px;
  text-align: center;
}
</style> 