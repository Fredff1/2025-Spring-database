<template>
  <div class="orders">
    <div class="page-header">
      <h2 class="page-title">订单管理</h2>
    </div>

    <!-- 订单列表 -->
    <el-card shadow="hover" class="table-card">
      <el-table :data="orders" style="width: 100%" v-loading="loading">
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="vehiclePlate" label="车牌号" width="120" />
        <el-table-column prop="repairType" label="维修类型" width="120">
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
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">
            ¥{{ row.amount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <!-- <el-button
              v-if="row.status === 'PENDING'"
              type="success"
              link
              @click="handleStartRepair(row)"
            >
              开始维修
            </el-button> -->
            <el-button v-if="row.status === 'PROCESSING'" type="warning" link @click="handleSubmitRecord(row)">
              提交记录
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[10, 20, 50, 100]"
          :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </el-card>

    <!-- 查看订单详情对话框 -->
    <el-dialog v-model="viewDialogVisible" title="订单详情" width="800px" destroy-on-close>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
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
        <el-descriptions-item label="维修记录" :span="2">
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
            </el-timeline-item>
          </el-timeline>
        </el-descriptions-item>
        <el-descriptions-item label="使用材料" :span="2">
          <el-table :data="materialUsages" style="width: 100%">
            <el-table-column prop="materialName" label="材料名称" />
            <el-table-column prop="quantity" label="数量" width="100" />
            <el-table-column prop="unitPrice" label="单价" width="120">
              <template #default="{ row }">
                ¥{{ row.unitPrice.toFixed(2) }}
              </template>
            </el-table-column>
          </el-table>
        </el-descriptions-item>
        <el-descriptions-item label="维修费用">
          ¥{{ currentOrder.amount.toFixed(2) }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ currentOrder.createTime }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 提交维修记录对话框 -->
    <el-dialog v-model="recordDialogVisible" title="提交维修记录" width="600px" destroy-on-close>
      <el-form ref="recordFormRef" :model="recordForm" :rules="recordRules" label-width="100px">
        <el-form-item label="订单状态" prop="status">
          <el-select v-model="recordForm.status" placeholder="请选择订单状态">
            <el-option label="维修中" value="PROCESSING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="故障描述" prop="faultDescription">
          <el-input v-model="recordForm.faultDescription" type="textarea" :rows="4" placeholder="请输入故障描述" />
        </el-form-item>
        <el-form-item label="维修结果" prop="repairResult">
          <el-input v-model="recordForm.repairResult" type="textarea" :rows="4" placeholder="请输入维修结果" />
        </el-form-item>
        <el-form-item label="工作时长(小时)" prop="actualWorkHour">
          <el-input v-model="recordForm.actualWorkHour" :rows="1" placeholder="请输入工作时长(小时)" />
        </el-form-item>
        <el-form-item label="使用材料" prop="materials">
          <div class="materials-list">
            <div v-for="(material, index) in recordForm.materials" :key="index" class="material-item">
              <el-row :gutter="10">
                <el-col :span="6">
                  <el-input v-model="material.materialName" placeholder="材料名称" />
                </el-col>
                <el-col :span="6">
                  <el-input v-model="material.quantity" placeholder="数量" />
                </el-col>
                <el-col :span="6">
                  <el-input v-model="material.unitPrice" placeholder="单价(￥)" />
                </el-col>
                <el-col :span="2">
                  <el-button type="danger" link @click="handleRemoveMaterial(index)">
                    删除
                  </el-button>
                </el-col>
              </el-row>
            </div>
          </div>
          <div class="add-material">
            <el-button type="primary" link @click="handleAddMaterial">
              添加材料
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="recordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRecordSubmit">提交记录</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { repairman } from '@/api'

// 订单列表数据
const loading = ref(false)
const orders = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 查看订单详情
const viewDialogVisible = ref(false)
const currentOrder = ref({})
const repairRecords = ref([])
const materialUsages = ref([])

// 提交维修记录
const recordDialogVisible = ref(false)
const recordFormRef = ref(null)
const recordForm = reactive({
  status: 'PROCESSING',
  faultDescription: '',
  repairResult: '',
  materials: []
})
const recordRules = {
  status: [
    { required: true, message: '请选择订单状态', trigger: 'change' }
  ],
  faultDescription: [
    { required: true, message: '请输入故障描述', trigger: 'blur' }
  ],
  repairResult: [
    { required: true, message: '请输入维修结果', trigger: 'blur' }
  ],
  actualWorkHour: [
    { required: true, message: '请输入维修时长', trigger: 'blur' }
  ],
}

// 获取维修类型标签
const getRepairTypeTag = (type) => {
  const map = {
    MAINTENANCE: 'info',
    REPAIR: 'warning',
    ACCIDENT: 'danger'
  }
  return map[type] || 'info'
}

// 获取维修类型文本
const getRepairTypeText = (type) => {
  const map = {
    MAINTENANCE: '常规保养',
    REPAIR: '故障维修',
    ACCIDENT: '事故维修'
  }
  return map[type] || type
}

// 获取状态标签
const getOrderStatusTag = (status) => {
  const map = {
    PENDING: 'info',
    PROCESSING: 'success',
    COMPLETED: 'success',
    CANCELLED: 'danger'
  }
  return map[status] || 'info'
}

// 获取状态文本
const getOrderStatusText = (status) => {
  const map = {
    PENDING: '待处理',
    PROCESSING: '维修中',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

// 获取订单列表
const fetchOrders = async () => {
  loading.value = true
  try {
    const res = await repairman.getOrders({
      page: currentPage.value,
      limit: pageSize.value
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

// 查看订单详情
const handleView = async (row) => {
  try {
    const [orderRes, recordsRes, materialsRes] = await Promise.all([
      repairman.getOrderDetail(row.id),
      repairman.getRepairRecords(row.id),
      repairman.getMaterialUsages(row.id)
    ])
    currentOrder.value = orderRes
    repairRecords.value = recordsRes
    materialUsages.value = materialsRes
    viewDialogVisible.value = true
  } catch (error) {
    console.error('获取订单详情失败:', error)
    ElMessage.error('获取订单详情失败')
  }
}

// 开始维修
// const handleStartRepair = async (row) => {
//   try {
//     await repairman.startRepair(row.id)
//     ElMessage.success('已开始维修')
//     fetchOrders()
//   } catch (error) {
//     console.error('操作失败:', error)
//     ElMessage.error('操作失败')
//   }
// }

// 提交维修记录
const handleSubmitRecord = (row) => {
  currentOrder.value = row
  recordForm.status = 'PROCESSING'
  recordForm.actualWorkHour = null
  recordForm.faultDescription = ''
  recordForm.repairResult = ''
  recordForm.materials = []
  recordDialogVisible.value = true
}

// 添加材料
const handleAddMaterial = () => {
  recordForm.materials.push({
    materialName: '',
    quantity: null,
    unitPrice: null,
    orderId: currentOrder.value.id,
  })
}

// 删除材料
const handleRemoveMaterial = (index) => {
  recordForm.materials.splice(index, 1)
}

// 提交维修记录
const handleRecordSubmit = async () => {
  if (!recordFormRef.value) return
  await recordFormRef.value.validate(async (valid) => {
    if (valid) {
      try {

        const recordSubmitData = {
          orderId: currentOrder.value.id,
          status: recordForm.status,
          faultDescription: recordForm.faultDescription,
          repairResult: recordForm.repairResult,
          materials: recordForm.materials,
          actualWorkHour: recordForm.actualWorkHour
        };
        await repairman.submitRepairRecord(recordSubmitData)
        ElMessage.success('维修记录提交成功')
        recordDialogVisible.value = false
        fetchOrders()
      } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error('操作失败')
      }
    }
  })
}

// 分页
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
})
</script>

<style scoped>
.orders {
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
</style>