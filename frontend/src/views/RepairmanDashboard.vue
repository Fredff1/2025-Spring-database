<template>
  <div class="repairman-dashboard">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="待处理订单" name="assigned">
        <el-table :data="assignedOrders" v-loading="loading">
          <el-table-column prop="orderNo" label="订单号" width="180" />
          <el-table-column prop="vehiclePlate" label="车牌号" width="120" />
          <el-table-column prop="repairType" label="维修类型" width="120">
            <template #default="{ row }">
              {{ getRepairTypeLabel(row.repairType) }}
            </template>
          </el-table-column>
          <el-table-column prop="problem" label="问题描述" />
          <el-table-column label="操作" width="200">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="handleAccept(row)">接受</el-button>
              <el-button type="danger" size="small" @click="handleReject(row)">拒绝</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="进行中订单" name="active">
        <el-table :data="activeOrders" v-loading="loading">
          <el-table-column prop="orderNo" label="订单号" width="180" />
          <el-table-column prop="vehiclePlate" label="车牌号" width="120" />
          <el-table-column prop="repairType" label="维修类型" width="120">
            <template #default="{ row }">
              {{ getRepairTypeLabel(row.repairType) }}
            </template>
          </el-table-column>
          <el-table-column prop="problem" label="问题描述" />
          <el-table-column label="操作" width="200">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="handleAddRecord(row)">添加维修记录</el-button>
              <el-button type="info" size="small" @click="handleViewRecords(row)">查看记录</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 拒绝原因对话框 -->
    <el-dialog v-model="rejectDialogVisible" title="拒绝订单" width="500px">
      <el-form :model="rejectForm" label-width="100px">
        <el-form-item label="拒绝原因" required>
          <el-input v-model="rejectForm.reason" type="textarea" rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmReject">确认</el-button>
      </template>
    </el-dialog>

    <!-- 维修记录对话框 -->
    <el-dialog v-model="recordDialogVisible" title="添加维修记录" width="800px">
      <el-form :model="recordForm" label-width="100px">
        <el-form-item label="维修描述" required>
          <el-input v-model="recordForm.description" type="textarea" rows="3" />
        </el-form-item>
        <el-form-item label="工时" required>
          <el-input-number v-model="recordForm.laborHours" :min="1" />
        </el-form-item>
        <el-form-item label="使用零件">
          <div v-for="(part, index) in recordForm.partsUsed" :key="index" class="part-item">
            <el-input v-model="part.partName" placeholder="零件名称" style="width: 200px" />
            <el-input-number v-model="part.quantity" :min="1" placeholder="数量" style="width: 120px" />
            <el-input-number v-model="part.unitPrice" :min="0" :precision="2" placeholder="单价" style="width: 120px" />
            <el-button type="danger" @click="removePart(index)">删除</el-button>
          </div>
          <el-button type="primary" @click="addPart">添加零件</el-button>
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="recordForm.isFinal">标记为最终记录</el-checkbox>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="recordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRecord">提交</el-button>
      </template>
    </el-dialog>

    <!-- 维修记录历史对话框 -->
    <el-dialog v-model="historyDialogVisible" title="维修记录历史" width="800px">
      <el-timeline>
        <el-timeline-item
          v-for="record in repairRecords"
          :key="record.id"
          :timestamp="formatDateTime(record.createTime)"
          :type="record.isFinal ? 'success' : 'primary'"
        >
          <h4>{{ record.description }}</h4>
          <p>工时: {{ record.laborHours }}小时</p>
          <div v-if="record.partsUsed && record.partsUsed.length > 0">
            <p>使用零件:</p>
            <ul>
              <li v-for="part in record.partsUsed" :key="part.partName">
                {{ part.partName }} x {{ part.quantity }} (单价: ¥{{ part.unitPrice }})
              </li>
            </ul>
          </div>
        </el-timeline-item>
      </el-timeline>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { formatDateTime } from '@/utils/formatters'

const activeTab = ref('assigned')
const loading = ref(false)
const assignedOrders = ref([])
const activeOrders = ref([])
const repairRecords = ref([])

// 对话框控制
const rejectDialogVisible = ref(false)
const recordDialogVisible = ref(false)
const historyDialogVisible = ref(false)

// 表单数据
const rejectForm = ref({
  orderId: null,
  reason: ''
})

const recordForm = ref({
  orderId: null,
  description: '',
  laborHours: 1,
  partsUsed: [],
  isFinal: false
})

// 获取待处理订单
const fetchAssignedOrders = async () => {
  try {
    loading.value = true
    const response = await fetch('/api/repairman/assigned-orders')
    const data = await response.json()
    assignedOrders.value = data.data.list
  } catch (error) {
    ElMessage.error('获取待处理订单失败')
  } finally {
    loading.value = false
  }
}

// 获取进行中订单
const fetchActiveOrders = async () => {
  try {
    loading.value = true
    const response = await fetch('/api/repairman/active-orders')
    const data = await response.json()
    activeOrders.value = data.data.list
  } catch (error) {
    ElMessage.error('获取进行中订单失败')
  } finally {
    loading.value = false
  }
}

// 接受订单
const handleAccept = async (order) => {
  try {
    await fetch(`/api/repairman/orders/${order.id}/accept`, {
      method: 'POST'
    })
    ElMessage.success('订单已接受')
    fetchAssignedOrders()
    fetchActiveOrders()
  } catch (error) {
    ElMessage.error('接受订单失败')
  }
}

// 拒绝订单
const handleReject = (order) => {
  rejectForm.value.orderId = order.id
  rejectForm.value.reason = ''
  rejectDialogVisible.value = true
}

const confirmReject = async () => {
  try {
    await fetch(`/api/repairman/orders/${rejectForm.value.orderId}/reject`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ reason: rejectForm.value.reason })
    })
    ElMessage.success('订单已拒绝')
    rejectDialogVisible.value = false
    fetchAssignedOrders()
  } catch (error) {
    ElMessage.error('拒绝订单失败')
  }
}

// 添加维修记录
const handleAddRecord = (order) => {
  recordForm.value.orderId = order.id
  recordForm.value.description = ''
  recordForm.value.laborHours = 1
  recordForm.value.partsUsed = []
  recordForm.value.isFinal = false
  recordDialogVisible.value = true
}

const addPart = () => {
  recordForm.value.partsUsed.push({
    partName: '',
    quantity: 1,
    unitPrice: 0
  })
}

const removePart = (index) => {
  recordForm.value.partsUsed.splice(index, 1)
}

const submitRecord = async () => {
  try {
    await fetch(`/api/repairman/orders/${recordForm.value.orderId}/repair-records`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(recordForm.value)
    })
    ElMessage.success('维修记录已提交')
    recordDialogVisible.value = false
    fetchActiveOrders()
  } catch (error) {
    ElMessage.error('提交维修记录失败')
  }
}

// 查看维修记录
const handleViewRecords = async (order) => {
  try {
    const response = await fetch(`/api/repairman/orders/${order.id}/repair-records`)
    const data = await response.json()
    repairRecords.value = data.data
    historyDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取维修记录失败')
  }
}

// 维修类型标签
const getRepairTypeLabel = (type) => {
  const types = {
    MAINTENANCE: '常规保养',
    REPAIR: '故障维修',
    ACCIDENT: '事故维修'
  }
  return types[type] || type
}

onMounted(() => {
  fetchAssignedOrders()
  fetchActiveOrders()
})
</script>

<style scoped>
.repairman-dashboard {
  padding: 20px;
}

.part-item {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
  align-items: center;
}
</style> 