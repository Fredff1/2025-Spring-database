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
        <el-table-column prop="model" label="型号" width="120" >
           <template #default="{ row }">
            {{getModelText(row.model)}}
           </template>
        </el-table-column>
        <el-table-column prop="registerDate" label="注册日期" width="120" />
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
          <el-select v-model="form.model" placeholder="请选择车型">
            <el-option label="多用途车" value="SUV" />
            <el-option label="轿车" value="Sedan" />
            <el-option label="皮卡" value="Pickup" />
            <el-option label="货车" value="Van" />
            <el-option label="小型车" value="Hatchback" />
            <el-option label="跑车" value="Coupe" />
            <el-option label="敞篷车" value="Convertible" />
          </el-select>
        </el-form-item>
        
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

const getModelText = (model) => {
    const map = {
        SUV: '多用途车',
        Sedan: '轿车',
        Pickup: '皮卡',
        Van: '货车',
        Hatchback: '小型车',
        Coupe:'跑车',
        Convertible:'敞篷车'

   }
  return map[model] || type
}

const showAddDialog = () => {
  isEdit.value = false
  form.value = {
    licensePlate: '',
    brand: '',
    model: '',
    registerDate: '',
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = (row) => {
  currentVehicle.value = row
  deleteDialogVisible.value = true
}

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