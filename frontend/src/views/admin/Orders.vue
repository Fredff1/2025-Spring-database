<template>
  <div class="orders">
    <div class="page-header">
      <h2 class="page-title">订单管理</h2>
      <el-button type="primary" @click="handleCreateOrderDialog">创建订单</el-button>
    </div>

    <!-- 订单列表 -->
    <el-card shadow="hover" class="table-card">
      <el-table :data="orders" style="width: 100%" v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" width="80" />
        <el-table-column prop="customerName" label="客户名" width="100" />
        <el-table-column prop="vehiclePlate" label="车牌号" width="120" />
        <el-table-column prop="repairType" label="维修类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getRepairTypeTag(row.repairType)">
              {{ getRepairTypeText(row.repairType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="problem" label="问题描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getOrderStatusTag(row.status)">
              {{ getOrderStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="90">
          <template #default="{ row }">
            ¥{{ row.amount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看详情</el-button>
            <el-button type="success" link @click="handleFeedback(row)">查看反馈</el-button>
            <el-button type="warning" link @click="handleUpdateOrderDialog(row)">更新状态</el-button>
            <el-button type="info" link @click="handleAssignOrderDialog(row)">分配维修</el-button>
            <el-button type="danger" link @click="handleDeleteOrder(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination v-model="pagination" :page-sizes="[10, 20, 50, 100]"
          :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </el-card>

    <!-- 查看订单详情对话框 -->
    <el-dialog v-model="viewDialogVisible" title="订单详情" width="800px" destroy-on-close>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单客户"><strong>{{ currentOrder.customerName }}</strong></el-descriptions-item>
        <el-descriptions-item label="车牌号">{{ currentOrder.vehiclePlate }}</el-descriptions-item>
        <el-descriptions-item label="维修类型">
          <el-tag :type="getRepairTypeTag(currentOrder.status)">
            {{ getRepairTypeText(currentOrder.repairType) }}
          </el-tag>

        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getOrderStatusTag(currentOrder.status)">
            {{ getOrderStatusText(currentOrder.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="问题描述" :span="2">
          {{ currentOrder.problem }}
        </el-descriptions-item>
        <el-descriptions-item label="维修人员" :span="2">
          <el-table :data="currentOrder.repairmanBaseInfos" style="width: 100%">
            <el-table-column prop="repairmanName" label="维修人" />
            <el-table-column prop="repairmanNumber" label="维修人工号"/>
          </el-table>
        </el-descriptions-item>
        <el-descriptions-item label="维修记录" :span="2">
          <div class="record-actions">
            <el-button type="primary" @click="handleCreateRecordDialog(currentOrder)">添加维修记录</el-button>
          </div>
          <el-timeline>
            <el-timeline-item v-for="record in repairRecords" :key="record.recordId" :timestamp="record.createTime"
              :type="record.status === '已完成' ? 'success' : 'primary'">
              <h4>维修记录</h4>
              <p><strong>当前故障描述：</strong>{{ record.faultDescription }}</p>
              <p><strong>状态:</strong>
                <el-tag :type="getOrderStatusTag(record.status)">
                  {{ getOrderStatusText(record.status) }}
                </el-tag>
              </p>
              <p><strong>维修结果:</strong> {{ record.repairResult }}</p>
              <p><strong>维修时长：{{ record.actualWorkingHour }}h</strong></p>
              <p><strong>维修人员：</strong><strong>{{ record.repairmanName }}</strong></p>
              <el-button type="primary" link @click="handleEditRecord(record)">修改</el-button>
              <div class="record-actions">
                <el-button type="danger" link @click="handleDeleteRecord(record.recordId)">删除</el-button>
              </div>
            </el-timeline-item>
          </el-timeline>
        </el-descriptions-item>
        <el-descriptions-item label="使用材料" :span="2">
          <div class="material-actions">
            <el-button type="primary" @click="handleCreateMaterialDialog(currentOrder)">添加材料</el-button>
          </div>
          <el-table :data="materialUsages" style="width: 100%">
            <el-table-column prop="materialName" label="材料名称" />
            <el-table-column prop="quantity" label="数量" width="100" />
            <el-table-column prop="unitPrice" label="单价" width="120">
              <template #default="{ row }">
                ¥{{ row.unitPrice.toFixed(2) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-button type="primary" link @click="handleEditMaterial(row)">修改</el-button>
                <el-button type="danger" link @click="handleDeleteMaterial(row.usageId)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-descriptions-item>
        <el-descriptions-item label="维修费用">
          ¥{{ currentOrder.amount.toFixed(2) }}
        </el-descriptions-item>
        <el-descriptions-item label="支付状态">
          <el-tag :type="currentOrder?.isPaid ? 'success' : 'warning'">
            {{ currentOrder?.isPaid ? '已支付' : '未支付' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ currentOrder.createTime }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 创建订单对话框 -->
    <el-dialog v-model="createOrderDialogVisible" title="创建订单" width="600px" destroy-on-close>
      <el-form ref="createOrderFormRef" :model="createOrderForm" :rules="createOrderRules" label-width="100px">
        <el-form-item label="客户名" prop="userName">
          <el-input v-model="createOrderForm.userName" placeholder="请输入客户名" />
        </el-form-item>
        <el-form-item label="车辆id" prop="vehicleId">
          <el-input v-model="createOrderForm.vehicleId" placeholder="请输入车辆ID" />
        </el-form-item>
        <el-form-item label="维修类型" prop="faultType">
          <el-select v-model="createOrderForm.faultType" placeholder="请选择维修类型">
            <el-option label="常规保养" value="MAINTENANCE" />
            <el-option label="通用维修" value="REPAIR" />
            <el-option label="钣金喷漆" value="PAINT" />
            <el-option label="轮胎更换" value="TIRE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="问题描述" prop="description">
          <el-input v-model="createOrderForm.description" type="textarea" :rows="4" placeholder="请输入问题描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createOrderDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateOrder">创建</el-button>
      </template>
    </el-dialog>

    <!-- 更新订单状态对话框 -->
    <el-dialog v-model="updateOrderDialogVisible" title="更新订单状态" width="500px" destroy-on-close>
      <el-form ref="updateOrderFormRef" :model="updateOrderForm" :rules="updateOrderRules" label-width="100px">
        <el-form-item label="订单状态" prop="orderStatus">
          <el-select v-model="updateOrderForm.orderStatus" placeholder="请选择订单状态">
            <el-option label="待处理" value="PENDING" />
            <el-option label="维修中" value="PROCESSING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="维修类型" prop="faultType">
          <el-select v-model="updateOrderForm.faultType" placeholder="请选择维修类型">
            <el-option label="常规保养" value="MAINTENANCE" />
            <el-option label="通用维修" value="REPAIR" />
            <el-option label="钣金喷漆" value="PAINT" />
            <el-option label="轮胎更换" value="TIRE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="问题描述" prop="description">
          <el-input v-model="updateOrderForm.description" type="textarea" :rows="4" placeholder="请输入问题描述" />
        </el-form-item>
        <el-form-item label="总费用" prop="totalFee">
          <el-input-number v-model="updateOrderForm.totalFee" :precision="2" :step="0.1" :min="0" />
        </el-form-item>
        <el-form-item label="是否已支付" prop="isPaid">
          <el-switch v-model="updateOrderForm.isPaid" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="updateOrderDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdateOrder">更新</el-button>
      </template>
    </el-dialog>

    <!-- 分配维修人员对话框 -->
    <el-dialog v-model="assignOrderDialogVisible" title="分配维修人员" width="500px" destroy-on-close>
      <el-form ref="assignOrderFormRef" :model="assignOrderForm" :rules="assignOrderRules" label-width="100px">
        <el-form-item label="维修人员工号" prop="repairmanNumber">
          <el-input v-model="assignOrderForm.repairmanNumber" type="textarea" :rows="1" placeholder="请输入维修人员工号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="assignOrderDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAssignOrder">分配</el-button>
      </template>
    </el-dialog>

    <!-- 创建维修记录对话框 -->
    <el-dialog v-model="createRecordDialogVisible" title="创建维修记录" width="600px" destroy-on-close>
      <el-form ref="createRecordFormRef" :model="createRecordForm" :rules="createRecordRules" label-width="100px">
        <el-form-item label="维修人员" prop="repairmanId">
          <el-select v-model="createRecordForm.repairmanId" placeholder="请选择维修人员">
            <el-option v-for="repairman in repairmen" :key="repairman.repairmanId" :label="repairman.repairmanName" :value="repairman.repairmanId" />
          </el-select>
        </el-form-item>
        <el-form-item label="故障描述" prop="faultDescription">
          <el-input v-model="createRecordForm.faultDescription" type="textarea" :rows="4" placeholder="请输入故障描述" />
        </el-form-item>
        <el-form-item label="维修结果" prop="repairResult">
          <el-input v-model="createRecordForm.repairResult" type="textarea" :rows="4" placeholder="请输入维修结果" />
        </el-form-item>
        <el-form-item label="订单状态" prop="status">
          <el-select v-model="createRecordForm.status" placeholder="请选择订单状态">
            <el-option label="维修中" value="PROCESSING" />
            <el-option label="已完成" value="COMPLETED" />
          </el-select>
        </el-form-item>
        <el-form-item label="工作时长" prop="actualWorkHour">
          <el-input-number v-model="createRecordForm.actualWorkHour" :precision="1" :step="0.5" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createRecordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateRecord">创建</el-button>
      </template>
    </el-dialog>

    <!-- 创建材料使用记录对话框 -->
    <el-dialog v-model="createMaterialDialogVisible" title="创建材料使用记录" width="500px" destroy-on-close>
      <el-form ref="createMaterialFormRef" :model="createMaterialForm" :rules="createMaterialRules" label-width="100px">
        <el-form-item label="材料名称" prop="materialName">
          <el-input v-model="createMaterialForm.materialName" placeholder="请输入材料名称" />
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number v-model="createMaterialForm.quantity" :min="1" />
        </el-form-item>
        <el-form-item label="单价" prop="unitPrice">
          <el-input-number v-model="createMaterialForm.unitPrice" :precision="2" :step="0.1" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createMaterialDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateMaterial">创建</el-button>
      </template>
    </el-dialog>

    <!-- 创建反馈回复对话框 -->
    <el-dialog v-model="createFeedbackResponseDialogVisible" title="回复反馈" width="500px" destroy-on-close>
      <el-form ref="createFeedbackResponseFormRef" :model="createFeedbackResponseForm" :rules="createFeedbackResponseRules" label-width="100px">
        <el-form-item label="回复内容" prop="response">
          <el-input v-model="createFeedbackResponseForm.response" type="textarea" :rows="4" placeholder="请输入回复内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createFeedbackResponseDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateFeedbackResponse">提交</el-button>
      </template>
    </el-dialog>

    <!-- 反馈列表对话框 -->
    <el-dialog
      v-model="feedbackDialogVisible"
      title="维修反馈"
      width="600px"
    >
      <div v-loading="feedbackLoading">
        <div v-if="feedbackList.length === 0" class="empty-feedback">
          暂无反馈
        </div>
        <div v-else class="feedback-list">
          <div v-for="feedback in feedbackList" :key="feedback.feedbackId" class="feedback-item">
            <div class="feedback-header">
              <span class="feedback-time">{{ (feedback.feedbackTime) }}</span>
              <el-rate
                v-if="feedback.rating"
                v-model="feedback.rating"
                disabled
                show-score
                text-color="#ff9900"
              />
            </div>
            <div class="feedback-content"><strong>客户：{{ feedback.username }}</strong></div>
            <div class="feedback-content">{{ feedback.description }}</div>
            <div class="feedback-content" v-if="feedback.response"><strong>系统回复</strong></div>
            <div class="feedback-content" v-if="feedback.response">{{ feedback.response }}</div>
            <div class="feedback-actions">
              <el-button type="primary" link @click="handleCreateFeedbackResponseDialog(feedback)">回复</el-button>
              <el-button type="danger" link @click="handleDeleteFeedback(feedback.feedbackId)">删除</el-button>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- 编辑维修记录对话框 -->
    <el-dialog v-model="editRecordDialogVisible" title="编辑维修记录" width="600px" destroy-on-close>
      <el-form ref="editRecordFormRef" :model="editRecordForm" :rules="editRecordRules" label-width="100px">
        <el-form-item label="维修人员" prop="repairmanId">
          <el-select v-model="editRecordForm.repairmanId" placeholder="请选择维修人员">
            <el-option v-for="repairman in repairmen" :key="repairman.repairmanId" :label="repairman.repairmanName" :value="repairman.repairmanId" />
          </el-select>
        </el-form-item>
        <el-form-item label="故障描述" prop="faultDescription">
          <el-input v-model="editRecordForm.faultDescription" type="textarea" :rows="4" placeholder="请输入故障描述" />
        </el-form-item>
        <el-form-item label="维修结果" prop="repairResult">
          <el-input v-model="editRecordForm.repairResult" type="textarea" :rows="4" placeholder="请输入维修结果" />
        </el-form-item>
        <el-form-item label="订单状态" prop="orderStatus">
          <el-select v-model="editRecordForm.orderStatus" placeholder="请选择订单状态">
            <el-option label="待处理" value="PENDING" />
            <el-option label="处理中" value="PROCESSING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="实际工时" prop="actualWorkingHours">
          <el-input-number v-model="editRecordForm.actualWorkingHours" :min="0" :precision="1" :step="0.5" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editRecordDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleEditRecordSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑材料使用对话框 -->
    <el-dialog v-model="editMaterialDialogVisible" title="编辑材料使用" width="500px" destroy-on-close>
      <el-form ref="editMaterialFormRef" :model="editMaterialForm" :rules="editMaterialRules" label-width="100px">
        <el-form-item label="维修人员工号" prop="submitterId">
          <el-input v-model="editMaterialForm.submitterId" placeholder="请输入维修人员工号" />
        </el-form-item>
        <el-form-item label="材料名称" prop="materialName">
          <el-input v-model="editMaterialForm.materialName" placeholder="请输入材料名称" />
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number v-model="editMaterialForm.quantity" :min="1" :precision="0" />
        </el-form-item>
        <el-form-item label="单价" prop="unitPrice">
          <el-input-number v-model="editMaterialForm.unitPrice" :min="0" :precision="2" :step="0.1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editMaterialDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleEditMaterialSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { admin } from '@/api'
import dayjs from 'dayjs'

// 订单列表数据
const loading = ref(false)
const orders = ref([])
const total = ref(0)

// 查看订单详情
const viewDialogVisible = ref(false)
const currentOrder = ref({})
const repairRecords = ref([])
const materialUsages = ref([])
const currentOrderId = ref(0)

// 创建订单相关
const createOrderDialogVisible = ref(false)
const createOrderFormRef = ref(null)
const createOrderForm = reactive({
  userName: '',
  faultType: '',
  description: '',
  vehicleId:''
})
const createOrderRules = {
  userName: [{ required: true, message: '请输入客户名', trigger: 'blur' }],
  faultType: [{ required: true, message: '请选择维修类型', trigger: 'change' }],
  description: [{ required: true, message: '请输入问题描述', trigger: 'blur' }]
}

// 更新订单相关
const updateOrderDialogVisible = ref(false)
const updateOrderFormRef = ref(null)
const updateOrderForm = reactive({
  orderStatus: '',
  faultType: '',
  description: '',
  totalFee: 0,
  isPaid: false
})
const updateOrderRules = {
  orderStatus: [{ required: true, message: '请选择订单状态', trigger: 'change' }],
  faultType: [{ required: true, message: '请选择维修类型', trigger: 'change' }],
  description: [{ required: true, message: '请输入问题描述', trigger: 'blur' }],
  totalFee: [{ required: true, message: '请输入总费用', trigger: 'blur' }]
}

// 分配订单相关
const assignOrderDialogVisible = ref(false)
const assignOrderFormRef = ref(null)
const assignOrderForm = reactive({
  repairmanNumber: ''
})
const assignOrderRules = {
  repairmanId: [{ required: true, message: '请选择维修人员', trigger: 'change' }]
}

// 创建维修记录相关
const createRecordDialogVisible = ref(false)
const createRecordFormRef = ref(null)
const createRecordForm = reactive({
  faultDescription: '',
  repairResult: '',
  status: 'PROCESSING',
  actualWorkHour: 0,
  repairmanId: null
})
const createRecordRules = {
  faultDescription: [{ required: true, message: '请输入故障描述', trigger: 'blur' }],
  repairResult: [{ required: true, message: '请输入维修结果', trigger: 'blur' }],
  status: [{ required: true, message: '请选择订单状态', trigger: 'change' }],
  actualWorkHour: [{ required: true, message: '请输入工作时长', trigger: 'blur' }]
}

// 创建材料使用记录相关
const createMaterialDialogVisible = ref(false)
const createMaterialFormRef = ref(null)
const createMaterialForm = reactive({
  submitterId: '',
  materialName: '',
  quantity: 1,
  unitPrice: 0
})
const createMaterialRules = {
  materialName: [{ required: true, message: '请输入材料名称', trigger: 'blur' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }],
  unitPrice: [{ required: true, message: '请输入单价', trigger: 'blur' }]
}

// 创建反馈回复相关
const createFeedbackResponseDialogVisible = ref(false)
const createFeedbackResponseFormRef = ref(null)
const createFeedbackResponseForm = reactive({
  feedbackId: null,
  response: ''
})
const createFeedbackResponseRules = {
  response: [{ required: true, message: '请输入回复内容', trigger: 'blur' }]
}

//反馈相关
const feedbackDialogVisible = ref(false)
const feedbackList = ref([])

// 维修人员列表
const repairmen = ref([])

const pagination = reactive({
  currentPage: 1,
  pageSize: 10
})


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

const getOrderStatusTag = (status) => {
  const map = {
    PENDING: 'info',
    PROCESSING: 'success',
    COMPLETED: 'success',
    CANCELLED: 'danger'
  }
  return map[status] || 'info'
}

const getOrderStatusText = (status) => {
  const map = {
    PENDING: '待处理',
    PROCESSING: '维修中',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

const fetchOrders = async () => {
  loading.value = true
  try {
    const res = await admin.getOrders({
      page: pagination.currentPage,
      limit: pagination.pageSize
    })
    orders.value = res.list
    total.value = res.total

  } catch (error) {
    console.error('获取订单列表失败:', error)
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

const handleView = async (row) => {
  try {
    const [orderRes, recordsRes, materialsRes] = await Promise.all([
      admin.getOrderDetail(row.id),
      admin.getRepairRecords(row.id),
      admin.getMaterialUsages(row.id)
    ])
    currentOrder.value = orderRes
    repairRecords.value = recordsRes
    materialUsages.value = materialsRes
    repairmen.value = orderRes.repairmanBaseInfos
      updateOrderForm.orderStatus = orderRes.status,
      updateOrderForm.faultType = orderRes.faultType,
      updateOrderForm.description = orderRes.problem,
      updateOrderForm.totalFee = orderRes.totalFee,
      updateOrderForm.isPaid = orderRes.isPaid
    viewDialogVisible.value = true
  } catch (error) {
    console.error('获取订单详情失败:', error)
    ElMessage.error('获取订单详情失败')
  }
}

// 查看反馈
const handleFeedback = async (row) => {
  currentOrder.value = row
  currentOrderId.value = row.id
  feedbackDialogVisible.value = true
  try {
    const res = await admin.getFeedbackList(row.id)
    feedbackList.value = res || []
  } catch (error) {
    console.error('获取反馈列表失败:', error)
    ElMessage.error('获取反馈列表失败')
  } finally {

  }
}

// 创建订单
const handleCreateOrder = async () => {
  if (!createOrderFormRef.value) return
  await createOrderFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await admin.createOrder(createOrderForm,createOrderForm.userName)
        ElMessage.success('创建订单成功')
        createOrderDialogVisible.value = false
        fetchOrders()
      } catch (error) {
        console.error('创建订单失败:', error)
        ElMessage.error('创建订单失败')
      }
    }
  })
}

// 更新订单
const handleUpdateOrder = async () => {
  if (!updateOrderFormRef.value) return
  await updateOrderFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await admin.updateOrderStatus(currentOrder.value.id, updateOrderForm)
        ElMessage.success('更新订单成功')
        updateOrderDialogVisible.value = false
        fetchOrders()
      } catch (error) {
        console.error('更新订单失败:', error)
        ElMessage.error('更新订单失败')
      }
    }
  })
}

const handleDeleteOrder = async (id) => {
  try {
        await ElMessageBox.confirm('确定要删除此维修订单吗？此操作不可恢复！', '提示', {
          type: 'danger',
        })
                await ElMessageBox.confirm('请再次确定删除此维修订单', '提示', {
          type: 'danger',
        })
        await admin.deleteOrder(id)
        ElMessage.success('删除订单成功')
        updateOrderDialogVisible.value = false
        fetchOrders()
      } catch (error) {
        console.error('删除订单失败:', error)
        ElMessage.error('删除订单失败')
  }
}

// 分配订单
const handleAssignOrder = async () => {
  if (!assignOrderFormRef.value) return
  await assignOrderFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await admin.assignOrder(currentOrder.value.id, assignOrderForm.repairmanNumber)
        ElMessage.success('分配订单成功')
        assignOrderDialogVisible.value = false
        fetchOrders()
      } catch (error) {
        console.error('分配订单失败:', error)
        ElMessage.error('分配订单失败')
      }
    }
  })
}

// 创建维修记录
const handleCreateRecord = async () => {
  if (!createRecordFormRef.value) return
  await createRecordFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await admin.createRepairRecord(createRecordForm.repairmanId, {
          ...createRecordForm,
          orderId: currentOrder.value.id,
          materials: []
        })
        ElMessage.success('创建维修记录成功')
        createRecordDialogVisible.value = false
        handleView(currentOrder.value)
        fetchOrders()
      } catch (error) {
        console.error('创建维修记录失败:', error)
        ElMessage.error('创建维修记录失败')
      }
    }
  })
}

// 创建材料使用记录
const handleCreateMaterial = async () => {
  if (!createMaterialFormRef.value) return
  await createMaterialFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await admin.createMaterialUsage({
          ...createMaterialForm,
          orderId: currentOrder.value.id
        })
        ElMessage.success('创建材料使用记录成功')
        createMaterialDialogVisible.value = false
        handleView(currentOrder.value)
        fetchOrders()
      } catch (error) {
        console.error('创建材料使用记录失败:', error)
        ElMessage.error('创建材料使用记录失败')
      }
    }
  })
}

// 创建反馈回复
const handleCreateFeedbackResponse = async () => {
  if (!createFeedbackResponseFormRef.value) return
  await createFeedbackResponseFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        console.log(createFeedbackResponseForm)

        await admin.createFeedbackResponse({
          feedbackId: createFeedbackResponseForm.feedbackId,
          adminResponse: createFeedbackResponseForm.response
        })
        ElMessage.success('回复反馈成功')
        createFeedbackResponseDialogVisible.value = false
        handleFeedback(currentOrder.value)
      } catch (error) {
        console.error('回复反馈失败:', error)
        ElMessage.error('回复反馈失败')
      }
    }
  })
}

// 删除维修记录
const handleDeleteRecord = async (recordId) => {
  try {
    await ElMessageBox.confirm('确定要删除这条维修记录吗？', '提示', {
      type: 'warning'
    })
    await admin.deleteRepairRecord(recordId)
    ElMessage.success('删除维修记录成功')
    handleView(currentOrder.value)
    fetchOrders()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除维修记录失败:', error)
      ElMessage.error('删除维修记录失败')
    }
  }
}

// 删除材料使用记录
const handleDeleteMaterial = async (materialId) => {
  try {
    await ElMessageBox.confirm('确定要删除这条材料使用记录吗？', '提示', {
      type: 'warning'
    })
    await admin.deleteMaterialUsage(materialId)
    ElMessage.success('删除材料使用记录成功')
    handleView(currentOrder.value)
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除材料使用记录失败:', error)
      ElMessage.error('删除材料使用记录失败')
    }
  }
}

// 删除反馈
const handleDeleteFeedback = async (feedbackId) => {
  try {
    await ElMessageBox.confirm('确定要删除这条反馈吗？', '提示', {
      type: 'warning'
    })
    await admin.deleteFeedback(feedbackId)
    ElMessage.success('删除反馈成功')
    handleFeedback(currentOrder.value)
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除反馈失败:', error)
      ElMessage.error('删除反馈失败')
    }
  }
}

// 打开创建订单对话框
const handleCreateOrderDialog = () => {
  createOrderForm.userName = ''
  createOrderForm.faultType = ''
  createOrderForm.description = ''
  createOrderForm.vehicleId = 0
  createOrderDialogVisible.value = true
}

// 打开更新订单对话框
const handleUpdateOrderDialog = (row) => {
  currentOrder.value = row
  updateOrderForm.orderStatus = row.status
  updateOrderForm.faultType = row.repairType
  updateOrderForm.description = row.problem
  updateOrderForm.totalFee = row.amount
  updateOrderForm.isPaid = row.isPaid
  updateOrderDialogVisible.value = true
}

// 打开分配订单对话框
const handleAssignOrderDialog = (row) => {
  currentOrder.value = row
  assignOrderForm.repairmanId = null
  assignOrderDialogVisible.value = true
}

// 打开创建维修记录对话框
const handleCreateRecordDialog = (row) => {
  currentOrder.value = row
  createRecordForm.faultDescription = ''
  createRecordForm.repairResult = ''
  createRecordForm.status = 'PROCESSING'
  createRecordForm.actualWorkHour = 0
  createRecordDialogVisible.value = true
}

// 打开创建材料使用记录对话框
const handleCreateMaterialDialog = (row) => {
  currentOrder.value = row
  createMaterialForm.materialName = ''
  createMaterialForm.quantity = 1
  createMaterialForm.unitPrice = 0
  createMaterialDialogVisible.value = true
}

// 打开创建反馈回复对话框
const handleCreateFeedbackResponseDialog = (feedback) => {
  createFeedbackResponseForm.feedbackId = feedback.feedbackId
  createFeedbackResponseForm.response = ''
  createFeedbackResponseDialogVisible.value = true
}

// 分页
const handleSizeChange = (val) => {
  pagination.pageSize = val
  fetchOrders()
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
  fetchOrders()
}

// 编辑维修记录
const editRecordDialogVisible = ref(false)
const editRecordFormRef = ref(null)
const editRecordForm = reactive({
  recordId: 0,
  repairmanId: null,
  faultDescription: '',
  repairResult: '',
  orderStatus: '',
  actualWorkingHours: 0
})
const editRecordRules = {
  repairmanId: [{ required: true, message: '请选择维修人员', trigger: 'change' }],
  faultDescription: [{ required: true, message: '请输入故障描述', trigger: 'blur' }],
  repairResult: [{ required: true, message: '请输入维修结果', trigger: 'blur' }],
  orderStatus: [{ required: true, message: '请选择订单状态', trigger: 'change' }],
  actualWorkingHours: [{ required: true, message: '请输入实际工时', trigger: 'blur' }]
}

// 编辑材料使用
const editMaterialDialogVisible = ref(false)
const editMaterialFormRef = ref(null)
const editMaterialForm = reactive({
  usageId: 0,
  materialName: '',
  quantity: 1,
  unitPrice: 0
})
const editMaterialRules = {
  materialName: [{ required: true, message: '请输入材料名称', trigger: 'blur' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }],
  unitPrice: [{ required: true, message: '请输入单价', trigger: 'blur' }]
}

// 编辑维修记录
const handleEditRecord = (record) => {
  editRecordForm.recordId = record.recordId
  editRecordForm.repairmanId = record.repairmanId
  editRecordForm.faultDescription = record.faultDescription
  editRecordForm.repairResult = record.repairResult
  editRecordForm.orderStatus = record.status
  editRecordForm.actualWorkingHours = record.actualWorkingHour
  editRecordDialogVisible.value = true
}

// 提交编辑维修记录
const handleEditRecordSubmit = async () => {
  if (!editRecordFormRef.value) return
  await editRecordFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await admin.updateRepairRecord(editRecordForm.recordId, {
          ...editRecordForm,
          orderId: currentOrder.value.id
        })
        ElMessage.success('更新维修记录成功')
        editRecordDialogVisible.value = false
        handleView(currentOrder.value)
      } catch (error) {
        console.error('更新维修记录失败:', error)
        ElMessage.error('更新维修记录失败')
      }
    }
  })
}

// 编辑材料使用
const handleEditMaterial = (material) => {
  editMaterialForm.usageId = material.usageId
  editMaterialForm.materialName = material.materialName
  editMaterialForm.quantity = material.quantity
  editMaterialForm.unitPrice = material.unitPrice
  editMaterialDialogVisible.value = true
}

// 提交编辑材料使用
const handleEditMaterialSubmit = async () => {
  if (!editMaterialFormRef.value) return
  await editMaterialFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await admin.updateMaterialUsage(editMaterialForm.usageId, {
          ...editMaterialForm,
          orderId: currentOrder.value.id
        })
        ElMessage.success('更新材料使用成功')
        editMaterialDialogVisible.value = false
        handleView(currentOrder.value)
      } catch (error) {
        console.error('更新材料使用失败:', error)
        ElMessage.error('更新材料使用失败')
      }
    }
  })
}

onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>
.orders {
  padding: 20px;
}

.page-header {
  margin-bottom: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.table-card {
  margin-bottom: 24px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.add-material {
  margin-top: 10px;
  text-align: center;
}

.materials-list {
  margin-bottom: 10px;
}

.material-item {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

.material-item:hover {
  background-color: #f5f7fa;
}

.feedback-list {
  max-height: 400px;
  overflow-y: auto;
}

.feedback-item {
  padding: 16px;
  border-bottom: 1px solid var(--border-color);
}

.feedback-item:last-child {
  border-bottom: none;
}

.feedback-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.feedback-time {
  color: var(--text-secondary);
  font-size: 14px;
}

.feedback-content {
  color: var(--text-primary);
  line-height: 1.6;
  white-space: pre-wrap;
}

.empty-feedback {
  text-align: center;
  color: var(--text-secondary);
  padding: 32px 0;
}

.record-actions {
  margin-bottom: 16px;
  display: flex;
  justify-content: flex-end;
}

.material-actions {
  margin-bottom: 16px;
  display: flex;
  justify-content: flex-end;
}

.feedback-actions {
  margin-top: 8px;
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.record-actions {
  display: flex;
  gap: 8px;
}
</style>