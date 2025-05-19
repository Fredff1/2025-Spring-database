<template>
  <div class="orders">
    <div class="page-header">
      <h2 class="page-title">订单管理</h2>
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
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待处理" value="pending" />
            <el-option label="维修中" value="repairing" />
            <el-option label="待确认" value="confirming" />
            <el-option label="已完成" value="completed" />
            <el-option label="已取消" value="cancelled" />
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

    <!-- 订单列表 -->
    <el-card shadow="hover" class="table-card">
      <el-table :data="orders" style="width: 100%" v-loading="loading">
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
              @click="handleStartRepair(row)"
            >
              开始维修
            </el-button>
            <el-button
              v-if="row.status === 'repairing'"
              type="warning"
              link
              @click="handleComplete(row)"
            >
              完成维修
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
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 查看订单详情对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="订单详情"
      width="800px"
      destroy-on-close
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ currentOrder.orderNumber }}</el-descriptions-item>
        <el-descriptions-item label="车牌号">{{ currentOrder.plateNumber }}</el-descriptions-item>
        <el-descriptions-item label="维修类型">
          {{ getRepairTypeText(currentOrder.repairType) }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(currentOrder.status)">
            {{ getStatusText(currentOrder.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="问题描述" :span="2">
          {{ currentOrder.problem }}
        </el-descriptions-item>
        <el-descriptions-item label="维修方案" :span="2">
          {{ currentOrder.solution }}
        </el-descriptions-item>
        <el-descriptions-item label="使用配件" :span="2">
          <el-table :data="currentOrder.parts" style="width: 100%">
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
          ¥{{ currentOrder.amount.toFixed(2) }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ currentOrder.createTime }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 完成维修对话框 -->
    <el-dialog
      v-model="completeDialogVisible"
      title="完成维修"
      width="500px"
      destroy-on-close
    >
      <el-form
        ref="completeFormRef"
        :model="completeForm"
        :rules="completeRules"
        label-width="100px"
      >
        <el-form-item label="维修方案" prop="solution">
          <el-input
            v-model="completeForm.solution"
            type="textarea"
            :rows="4"
            placeholder="请输入维修方案"
          />
        </el-form-item>
        <el-form-item label="使用配件" prop="parts">
          <el-table :data="completeForm.parts" style="width: 100%">
            <el-table-column label="配件名称" prop="name" />
            <el-table-column label="数量" width="120">
              <template #default="{ row }">
                <el-input-number v-model="row.quantity" :min="1" :max="999" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80">
              <template #default="{ $index }">
                <el-button type="danger" link @click="handleRemovePart($index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="add-part">
            <el-button type="primary" link @click="handleAddPart">
              添加配件
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="completeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCompleteSubmit">确定</el-button>
      </template>
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
  status: '',
  dateRange: []
})

// 订单列表数据
const loading = ref(false)
const orders = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 查看订单详情
const viewDialogVisible = ref(false)
const currentOrder = ref({})

// 完成维修
const completeDialogVisible = ref(false)
const completeFormRef = ref(null)
const completeForm = reactive({
  solution: '',
  parts: []
})
const completeRules = {
  solution: [
    { required: true, message: '请输入维修方案', trigger: 'blur' }
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
    repairing: 'warning',
    confirming: 'primary',
    completed: 'success',
    cancelled: 'danger'
  }
  return map[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const map = {
    pending: '待处理',
    repairing: '维修中',
    confirming: '待确认',
    completed: '已完成',
    cancelled: '已取消'
  }
  return map[status] || status
}

// 获取订单列表
const fetchOrders = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      ...searchForm,
      startDate: searchForm.dateRange?.[0],
      endDate: searchForm.dateRange?.[1]
    }
    const res = await repairman.getOrders(params)
    orders.value = res.list
    total.value = res.total
  } catch (error) {
    console.error('获取订单列表失败:', error)
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchOrders()
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
  fetchOrders()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchOrders()
}

// 查看订单详情
const handleView = async (row) => {
  try {
    const res = await repairman.getOrderDetail(row.id)
    currentOrder.value = res
    viewDialogVisible.value = true
  } catch (error) {
    console.error('获取订单详情失败:', error)
    ElMessage.error('获取订单详情失败')
  }
}

// 开始维修
const handleStartRepair = async (row) => {
  try {
    await repairman.startRepair(row.id)
    ElMessage.success('已开始维修')
    fetchOrders()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 完成维修
const handleComplete = (row) => {
  currentOrder.value = row
  completeForm.solution = ''
  completeForm.parts = []
  completeDialogVisible.value = true
}

// 添加配件
const handleAddPart = () => {
  completeForm.parts.push({
    name: '',
    quantity: 1,
    price: 0
  })
}

// 删除配件
const handleRemovePart = (index) => {
  completeForm.parts.splice(index, 1)
}

// 提交完成维修
const handleCompleteSubmit = async () => {
  if (!completeFormRef.value) return
  await completeFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await repairman.completeRepair(currentOrder.value.id, completeForm)
        ElMessage.success('维修完成')
        completeDialogVisible.value = false
        fetchOrders()
      } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error('操作失败')
      }
    }
  })
}

// 初始化
fetchOrders()
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

.add-part {
  margin-top: 10px;
  text-align: center;
}
</style> 