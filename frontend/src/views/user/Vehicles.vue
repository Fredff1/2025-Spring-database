<template>
  <div class="vehicles">
    <div class="page-header">
      <h2 class="page-title">我的车辆</h2>
      <el-button type="primary" @click="showAddDialog">
        <el-icon><plus /></el-icon>
        添加车辆
      </el-button>
    </div>

    <!-- 车辆列表 -->
    <el-card shadow="hover">
      <el-table :data="vehicles" style="width: 100%">
        <el-table-column prop="licensePlate" label="车牌号" width="120" />
        <el-table-column prop="brand" label="品牌" width="120" />
        <el-table-column prop="model" label="型号" width="120" />
        <el-table-column prop="registerDate" label="注册日期" width="120" />
        <!-- <el-table-column prop="year" label="年份" width="100" />
        <el-table-column prop="color" label="颜色" width="100" />
        <el-table-column prop="mileage" label="里程数" width="120">
          <template #default="{ row }">
            {{ row.mileage }} km
          </template>
        </el-table-column>
        <el-table-column prop="vin" label="VIN码" width="180" /> -->
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 添加/编辑车辆对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑车辆' : '添加车辆'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="车牌号" prop="licensePlate">
          <el-input v-model="form.licensePlate" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="品牌" prop="brand">
          <el-input v-model="form.brand" placeholder="请输入品牌" />
        </el-form-item>
        <el-form-item label="型号" prop="model">
          <el-input v-model="form.model" placeholder="请输入型号" />
        </el-form-item> 
        
        <!-- <el-form-item label="年份" prop="year">
          <el-input-number v-model="form.year" :min="1900" :max="new Date().getFullYear()" />
        </el-form-item>
        <el-form-item label="颜色" prop="color">
          <el-input v-model="form.color" placeholder="请输入颜色" />
        </el-form-item> -->
        <!-- <el-form-item label="里程数" prop="mileage">
          <el-input-number v-model="form.mileage" :min="0" :step="1000" />
        </el-form-item> -->
        <!-- <el-form-item label="VIN码" prop="vin">
          <el-input v-model="form.vin" placeholder="请输入VIN码" />
        </el-form-item> -->
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 删除确认对话框 -->
    <el-dialog
      v-model="deleteDialogVisible"
      title="确认删除"
      width="400px"
    >
      <p>确定要删除这辆车吗？此操作不可恢复。</p>
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
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { user } from '@/api'

// 车辆列表
const vehicles = ref([])

// 对话框控制
const dialogVisible = ref(false)
const deleteDialogVisible = ref(false)
const isEdit = ref(false)
const currentVehicle = ref(null)

// 表单
const formRef = ref(null)
const form = ref({
  licensePlate: '',
  brand: '',
  model: '',
  registerDate: '',
  year: new Date().getFullYear(),
  color: '',
  mileage: 0,
  vin: ''
})

// 表单验证规则
const rules = {
  licensePlate: [
    { required: true, message: '请输入车牌号', trigger: 'blur' }
  ],
  brand: [
    { required: true, message: '请输入品牌', trigger: 'blur' }
  ],
  model: [
    { required: true, message: '请输入型号', trigger: 'blur' }
  ],
  // year: [
  //   { required: true, message: '请选择年份', trigger: 'change' }
  // ],
  // color: [
  //   { required: true, message: '请输入颜色', trigger: 'blur' }
  // ],
  // mileage: [
  //   { required: true, message: '请输入里程数', trigger: 'change' }
  // ],
  // vin: [
  //   { required: true, message: '请输入VIN码', trigger: 'blur' }
  // ]
}

// 获取车辆列表
const fetchVehicles = async () => {
  try {
    const res = await user.getVehicles()
    vehicles.value = res.list
    console.log(res)
  } catch (error) {
    console.error('获取车辆列表失败:', error)
    ElMessage.error('获取车辆列表失败')
  }
}

// 显示添加对话框
const showAddDialog = () => {
  isEdit.value = false
  form.value = {
    licensePlate: '',
    brand: '',
    model: '',
    registerDate: '',
    // year: new Date().getFullYear(),
    // color: '',
    // mileage: 0,
    // vin: ''
  }
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
    await user.deleteVehicle(currentVehicle.value.id)
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
        if (isEdit.value) {
          await user.updateVehicle(form.value.id, form.value)
          ElMessage.success('更新成功')
        } else {
          await user.addVehicle(form.value)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        fetchVehicles()
      } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error('操作失败')
      }
    }
  })
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

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style> 