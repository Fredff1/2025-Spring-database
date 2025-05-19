<template>
  <div class="vehicles">
    <div class="page-header">
      <h2 class="page-title">车辆管理</h2>
    </div>

    <!-- 搜索栏 -->
    <el-card shadow="hover" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="车牌号">
          <el-input v-model="searchForm.plateNumber" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="品牌">
          <el-input v-model="searchForm.brand" placeholder="请输入品牌" />
        </el-form-item>
        <el-form-item label="型号">
          <el-input v-model="searchForm.model" placeholder="请输入型号" />
        </el-form-item>
        <el-form-item label="车主">
          <el-input v-model="searchForm.ownerName" placeholder="请输入车主姓名" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 车辆列表 -->
    <el-card shadow="hover">
      <el-table :data="vehicles" style="width: 100%">
        <el-table-column prop="plateNumber" label="车牌号" width="120" />
        <el-table-column prop="brand" label="品牌" width="120" />
        <el-table-column prop="model" label="型号" width="120" />
        <el-table-column prop="year" label="年份" width="100" />
        <el-table-column prop="color" label="颜色" width="100" />
        <el-table-column prop="mileage" label="里程数" width="120">
          <template #default="{ row }">
            {{ row.mileage }} km
          </template>
        </el-table-column>
        <el-table-column prop="vin" label="VIN码" width="180" />
        <el-table-column prop="ownerName" label="车主" width="120" />
        <el-table-column prop="ownerPhone" label="联系电话" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
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

    <!-- 查看/编辑车辆对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑车辆' : '查看车辆'"
      width="600px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        :disabled="!isEdit"
      >
        <el-form-item label="车牌号" prop="plateNumber">
          <el-input v-model="form.plateNumber" />
        </el-form-item>
        <el-form-item label="品牌" prop="brand">
          <el-input v-model="form.brand" />
        </el-form-item>
        <el-form-item label="型号" prop="model">
          <el-input v-model="form.model" />
        </el-form-item>
        <el-form-item label="年份" prop="year">
          <el-input-number v-model="form.year" :min="1900" :max="new Date().getFullYear()" />
        </el-form-item>
        <el-form-item label="颜色" prop="color">
          <el-input v-model="form.color" />
        </el-form-item>
        <el-form-item label="里程数" prop="mileage">
          <el-input-number v-model="form.mileage" :min="0" :step="1000" />
          <span class="unit">km</span>
        </el-form-item>
        <el-form-item label="VIN码" prop="vin">
          <el-input v-model="form.vin" />
        </el-form-item>
        <el-form-item label="车主" prop="ownerName">
          <el-input v-model="form.ownerName" />
        </el-form-item>
        <el-form-item label="联系电话" prop="ownerPhone">
          <el-input v-model="form.ownerPhone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" v-if="isEdit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 删除确认对话框 -->
    <el-dialog
      v-model="deleteDialogVisible"
      title="确认删除"
      width="400px"
    >
      <p>确定要删除该车辆吗？此操作不可恢复。</p>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="confirmDelete">确定</el-button>
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
  plateNumber: '',
  brand: '',
  model: '',
  ownerName: ''
})

// 车辆列表
const vehicles = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 对话框控制
const dialogVisible = ref(false)
const deleteDialogVisible = ref(false)
const isEdit = ref(false)
const currentVehicle = ref(null)

// 表单
const formRef = ref(null)
const form = ref({
  plateNumber: '',
  brand: '',
  model: '',
  year: new Date().getFullYear(),
  color: '',
  mileage: 0,
  vin: '',
  ownerName: '',
  ownerPhone: ''
})

// 表单验证规则
const validatePhone = (rule, value, callback) => {
  if (value && !/^1[3-9]\d{9}$/.test(value)) {
    callback(new Error('请输入正确的手机号'))
  } else {
    callback()
  }
}

const validateVin = (rule, value, callback) => {
  if (value && !/^[A-HJ-NPR-Z0-9]{17}$/.test(value)) {
    callback(new Error('请输入正确的VIN码'))
  } else {
    callback()
  }
}

const rules = {
  plateNumber: [
    { required: true, message: '请输入车牌号', trigger: 'blur' }
  ],
  brand: [
    { required: true, message: '请输入品牌', trigger: 'blur' }
  ],
  model: [
    { required: true, message: '请输入型号', trigger: 'blur' }
  ],
  year: [
    { required: true, message: '请输入年份', trigger: 'blur' }
  ],
  color: [
    { required: true, message: '请输入颜色', trigger: 'blur' }
  ],
  mileage: [
    { required: true, message: '请输入里程数', trigger: 'blur' }
  ],
  vin: [
    { required: true, message: '请输入VIN码', trigger: 'blur' },
    { validator: validateVin, trigger: 'blur' }
  ],
  ownerName: [
    { required: true, message: '请输入车主姓名', trigger: 'blur' }
  ],
  ownerPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { validator: validatePhone, trigger: 'blur' }
  ]
}

// 获取车辆列表
const fetchVehicles = async () => {
  try {
    const res = await admin.getVehicles({
      page: currentPage.value,
      limit: pageSize.value,
      ...searchForm.value
    })
    vehicles.value = res.list
    total.value = res.total
  } catch (error) {
    console.error('获取车辆列表失败:', error)
    ElMessage.error('获取车辆列表失败')
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchVehicles()
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    plateNumber: '',
    brand: '',
    model: '',
    ownerName: ''
  }
  handleSearch()
}

// 查看车辆
const handleView = (row) => {
  isEdit.value = false
  form.value = { ...row }
  dialogVisible.value = true
}

// 处理编辑
const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

// 处理删除
const handleDelete = (row) => {
  currentVehicle.value = row
  deleteDialogVisible.value = true
}

// 确认删除
const confirmDelete = async () => {
  try {
    await admin.deleteVehicle(currentVehicle.value.id)
    ElMessage.success('删除成功')
    deleteDialogVisible.value = false
    fetchVehicles()
  } catch (error) {
    console.error('删除失败:', error)
    ElMessage.error('删除失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await admin.updateVehicle(form.value.id, form.value)
        ElMessage.success('更新成功')
        dialogVisible.value = false
        fetchVehicles()
      } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error('操作失败')
      }
    }
  })
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchVehicles()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchVehicles()
}

onMounted(() => {
  fetchVehicles()
})
</script>

<style scoped>
.vehicles {
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

.unit {
  margin-left: 8px;
  color: var(--text-secondary);
}
</style> 