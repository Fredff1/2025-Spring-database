<template>
  <div class="repair-history">
    <div class="page-header">
      <h2 class="page-title">维修记录</h2>
    </div>

    <!-- 搜索栏 -->
    <el-card shadow="hover" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderId" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="故障类型">
          <el-select v-model="searchForm.faultType" placeholder="请选择故障类型">
            <el-option label="全部" value="" />
            <el-option label="常规保养" value="MAINTENANCE" />
            <el-option label="故障维修" value="REPAIR" />
            <el-option label="钣金喷漆" value="PAINT" />
            <el-option label="轮胎更换" value="TIRE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态">
            <el-option label="全部" value="" />
            <el-option label="待接单" value="PENDING" />
            <el-option label="维修中" value="IN_PROGRESS" />
            <el-option label="待支付" value="WAITING_PAYMENT" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 维修订单列表 -->
    <el-card shadow="hover">
      <el-table :data="repairOrders" style="width: 100%" v-loading="loading">
        <el-table-column prop="orderId" label="订单号" width="180" />
        <el-table-column prop="faultType" label="故障类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getFaultTypeTag(row.faultType)">
              {{ getFaultTypeText(row.faultType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="问题描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalFee" label="总费用" width="120">
          <template #default="{ row }">
            ¥{{ row.totalFee?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="submitTime" label="提交时间" width="180" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button
              v-if="row.status === 'IN_PROGRESS'"
              type="warning"
              link
              @click="handleUrge(row)"
            >
              催单
            </el-button>
            <el-button
              v-if="row.status === 'COMPLETED'"
              type="success"
              link
              @click="handleFeedback(row)"
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
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next"
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
    >
      <el-tabs v-model="activeTab">
        <!-- 订单信息 -->
        <el-tab-pane label="订单信息" name="order">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="订单号">{{ currentOrder?.orderId }}</el-descriptions-item>
            <el-descriptions-item label="故障类型">
              {{ getFaultTypeText(currentOrder?.faultType) }}
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="getStatusTag(currentOrder?.status)">
                {{ getStatusText(currentOrder?.status) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="提交时间">{{ currentOrder?.submitTime }}</el-descriptions-item>
            <el-descriptions-item label="总费用">¥{{ currentOrder?.totalFee?.toFixed(2) }}</el-descriptions-item>
            <el-descriptions-item label="问题描述" :span="2">{{ currentOrder?.description }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <!-- 维修记录 -->
        <el-tab-pane label="维修记录" name="records">
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
                    <span>维修人员: {{ getRepairmanName(record.repairmanId) }}</span>
                    <el-tag :type="getStatusTag(record.status)" size="small">
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
        </el-tab-pane>

        <!-- 材料使用 -->
        <el-tab-pane label="材料使用" name="materials">
          <el-table :data="materialUsages" style="width: 100%">
            <el-table-column prop="materialName" label="材料名称" />
            <el-table-column prop="quantity" label="数量" width="100" />
            <el-table-column prop="unitPrice" label="单价" width="120">
              <template #default="{ row }">
                ¥{{ row.unitPrice?.toFixed(2) }}
              </template>
            </el-table-column>
            <el-table-column label="小计" width="120">
              <template #default="{ row }">
                ¥{{ (row.quantity * row.unitPrice)?.toFixed(2) }}
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 评价信息 -->
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
                  </div>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </template>
          <el-empty v-else description="暂无反馈记录" />
        </el-tab-pane>
      </el-tabs>
    </el-dialog>

    <!-- 评价对话框 -->
    <el-dialog
      v-model="feedbackDialogVisible"
      :title="feedbackForm.type === 'URGENT' ? '催单反馈' : '服务评价'"
      width="500px"
    >
      <el-form
        ref="feedbackFormRef"
        :model="feedbackForm"
        :rules="feedbackRules"
        label-width="100px"
      >
        <el-form-item label="反馈类型" prop="type">
          <el-radio-group v-model="feedbackForm.type">
            <el-radio label="RATING">评分反馈</el-radio>
            <el-radio label="URGENT">催单反馈</el-radio>
            <el-radio label="GENERAL">一般反馈</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="feedbackForm.type === 'RATING'" label="服务评分" prop="rating">
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
            :placeholder="getFeedbackPlaceholder(feedbackForm.type)"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="feedbackDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleFeedbackSubmit">提交</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { user } from '@/api'

// 搜索表单
const searchForm = ref({
  orderId: '',
  faultType: '',
  status: ''
})

// 维修订单列表
const loading = ref(false)
const repairOrders = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 对话框控制
const viewDialogVisible = ref(false)
const feedbackDialogVisible = ref(false)
const currentOrder = ref(null)
const currentFeedback = ref(null)
const materialUsages = ref([])
const repairRecords = ref([])
const activeTab = ref('order')

// 评价表单
const feedbackFormRef = ref(null)
const feedbackForm = reactive({
  type: 'RATING',
  rating: 5,
  description: ''
})

const feedbackRules = {
  type: [
    { required: true, message: '请选择反馈类型', trigger: 'change' }
  ],
  rating: [
    { required: true, message: '请选择服务评分', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入反馈内容', trigger: 'blur' },
    { min: 5, message: '反馈内容至少5个字符', trigger: 'blur' }
  ]
}

// 获取故障类型标签
const getFaultTypeTag = (type) => {
  const map = {
    MAINTENANCE: 'info',
    REPAIR: 'warning',
    PAINT: 'success',
    TIRE: 'primary',
    OTHER: 'info'
  }
  return map[type] || 'info'
}

// 获取故障类型文本
const getFaultTypeText = (type) => {
  const map = {
    MAINTENANCE: '常规保养',
    REPAIR: '故障维修',
    PAINT: '钣金喷漆',
    TIRE: '轮胎更换',
    OTHER: '其他'
  }
  return map[type] || type
}

// 获取状态标签
const getStatusTag = (status) => {
  const map = {
    PENDING: 'info',
    IN_PROGRESS: 'warning',
    WAITING_PAYMENT: 'success',
    COMPLETED: 'success',
    CANCELLED: 'danger'
  }
  return map[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const map = {
    PENDING: '待接单',
    PROCESSING: '维修中',
    WAITING_PAYMENT: '待支付',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

// 获取维修记录类型标签
const getRecordTypeTag = (status) => {
  const map = {
    PENDING: 'info',
    IN_PROGRESS: 'warning',
    COMPLETED: 'success'
  }
  return map[status] || 'info'
}

// 获取维修人员姓名
const getRepairmanName = (repairmanId) => {
  // 这里需要从后端获取维修人员信息
  return `维修人员${repairmanId}`
}

// 获取维修订单列表
const fetchRepairOrders = async () => {
  loading.value = true
  try {
    const res = await user.getRepairOrders({
      page: currentPage.value,
      pageSize: pageSize.value,
      ...searchForm.value
    })
    repairOrders.value = res.list
    total.value = res.total
  } catch (error) {
    console.error('获取维修订单失败:', error)
    ElMessage.error('获取维修订单失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchRepairOrders()
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    orderId: '',
    faultType: '',
    status: ''
  }
  handleSearch()
}

// 分页
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchRepairOrders()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchRepairOrders()
}

// 查看详情
const handleView = async (row) => {
  try {
    const [orderDetail, materialUsageList, feedbackList, records] = await Promise.all([
      user.getOrderDetail(row.orderId),
      user.getMaterialUsages(row.orderId),
      user.getFeedbackList(row.orderId),
      user.getRepairRecords(row.orderId)
    ])
    currentOrder.value = orderDetail
    materialUsages.value = materialUsageList
    feedbackList.value = feedbackList
    repairRecords.value = records
    viewDialogVisible.value = true
  } catch (error) {
    console.error('获取维修详情失败:', error)
    ElMessage.error('获取维修详情失败')
  }
}

// 催单
const handleUrge = (row) => {
  currentOrder.value = row
  feedbackForm.type = 'URGENT'
  feedbackForm.rating = null
  feedbackForm.description = ''
  feedbackDialogVisible.value = true
}

// 评价
const handleFeedback = (row) => {
  currentOrder.value = row
  feedbackForm.type = 'RATING'
  feedbackForm.rating = 5
  feedbackForm.description = ''
  feedbackDialogVisible.value = true
}

// 提交评价
const handleFeedbackSubmit = async () => {
  if (!feedbackFormRef.value) return
  await feedbackFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await user.submitFeedback({
          orderId: currentOrder.value.orderId,
          ...feedbackForm
        })
        ElMessage.success(feedbackForm.type === 'URGENT' ? '催单反馈已提交' : '评价成功')
        feedbackDialogVisible.value = false
        fetchRepairOrders()
      } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error('提交失败')
      }
    }
  })
}

// 获取反馈内容占位符
const getFeedbackPlaceholder = (type) => {
  const placeholders = {
    RATING: '请输入您的评价内容',
    URGENT: '请描述您需要催单的原因',
    GENERAL: '请输入您的反馈内容'
  }
  return placeholders[type] || '请输入反馈内容'
}

// 获取反馈类型标签
const getFeedbackTypeTag = (type) => {
  const map = {
    RATING: 'success',
    URGENT: 'warning',
    GENERAL: 'info'
  }
  return map[type] || 'info'
}

// 获取反馈类型文本
const getFeedbackTypeText = (type) => {
  const map = {
    RATING: '评分反馈',
    URGENT: '催单反馈',
    GENERAL: '一般反馈'
  }
  return map[type] || type
}

// 初始化
fetchRepairOrders()
</script>

<style scoped>
.repair-history {
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

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.record-card {
  margin-bottom: 8px;
}

.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.record-content {
  margin-top: 8px;
}

.record-content p {
  margin: 4px 0;
}

.feedback-card {
  margin-bottom: 8px;
}

.feedback-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.feedback-content {
  margin-top: 8px;
}

.feedback-content p {
  margin: 8px 0;
}
</style> 