<template>
  <div class="vehicles">
    <div class="page-header">
      <h2 class="page-title">车辆管理</h2>
      <el-button type="primary" @click="handleAdd">添加车辆</el-button>
    </div>

    <!-- 搜索栏 -->
    <!-- <el-card shadow="hover" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="车牌号">
          <el-input v-model="searchForm.plateNumber" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="品牌">
          <el-input v-model="searchForm.brand" placeholder="请输入品牌" />
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
        <el-form-item label="车主">
          <el-input v-model="searchForm.ownerName" placeholder="请输入车主姓名" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card> -->

    <!-- 车辆列表 -->
    <el-card shadow="hover">
      <el-table :data="vehicles" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="licensePlate" label="车牌号" width="120" />
        <el-table-column prop="brand" label="品牌" width="120" />
        <el-table-column prop="model" label="型号" width="120" >
          <template #default="{ row }">
            {{getModelText(row.model)}}
           </template>
        </el-table-column>
        <el-table-column prop="ownerName" label="拥有者" width="180" />
        <el-table-column prop="registerDate" label="注册日期" width="120">
          <template #default="{ row }">
            {{ formatDate(row.registerDate) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="150">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column> -->
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
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

    <!-- 车辆表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="编辑车辆信息"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="车牌号" prop="licensePlate">
          <el-input v-model="form.licensePlate" />
        </el-form-item>
        <el-form-item label="品牌" prop="brand">
          <el-input v-model="form.brand" />
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
        <el-form-item label="拥有者" prop="ownerName">
          <el-input v-model="form.ownerName" :disabled="dialogType === 'edit'" />
        </el-form-item>
        <el-form-item label="注册日期" prop="registerDate">
          <el-date-picker
            v-model="form.registerDate"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { admin } from '@/api'
import dayjs from 'dayjs'

const dialogType = ref('add')

// 搜索表单
const searchForm = ref({
  plateNumber: '',
  brand: '',
  model: '',
  ownerName: ''
})

// 表格数据
const loading = ref(false)
const vehicles = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 对话框相关
const dialogVisible = ref(false)
const submitting = ref(false)
const formRef = ref(null)

// 表单数据
const form = reactive({
  id: null,
  licensePlate: '',
  brand: '',
  model: '',
  vin: '',
  registerDate: ''
})

const handleAdd = () => {
  dialogType.value = 'add'
  Object.assign(form, {
    id: null,
    licensePlate: '',
    brand: '',
    model: '',
    vin: '',
    registerDate: '',
    ownerName: ''
  })
  dialogVisible.value = true
}

// 表单验证规则
const validateLicensePlate = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入车牌号'))
  } else if (!/^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-Z0-9]{4,5}[A-Z0-9挂学警港澳]$/.test(value)) {
    callback(new Error('请输入正确的车牌号'))
  } else {
    callback()
  }
}

const validateVin = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入车架号'))
  } else if (!/^[A-HJ-NPR-Z0-9]{17}$/.test(value)) {
    callback(new Error('请输入正确的车架号'))
  } else {
    callback()
  }
}

const rules = {
  licensePlate: [
    { required: true, message: '请输入车牌号', trigger: 'blur' },
    { validator: validateLicensePlate, trigger: 'blur' }
  ],
  brand: [
    { required: true, message: '请输入品牌', trigger: 'blur' }
  ],
  model: [
    { required: true, message: '请输入型号', trigger: 'blur' }
  ],
  vin: [
    { required: true, message: '请输入车架号', trigger: 'blur' },
    { validator: validateVin, trigger: 'blur' }
  ],
  registerDate: [
    { required: true, message: '请选择注册日期', trigger: 'change' }
  ]
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

// 格式化日期
const formatDate = (date) => {
  if (!date) return '-'
  return dayjs(date).format('YYYY-MM-DD')
}

// 获取车辆列表
const fetchVehicles = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      limit: pageSize.value
    }
    const res = await admin.getVehicles(params)
    vehicles.value = res.list || []
    total.value = res.total || 0
  } catch (error) {
    console.error('获取车辆列表失败:', error)
    ElMessage.error('获取车辆列表失败')
  } finally {
    loading.value = false
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

// 编辑车辆
const handleEdit = (row) => {
  dialogType.value = 'edit'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 删除车辆
const handleDelete = (row) => {
  ElMessageBox.confirm(
    '确定要删除该车辆吗？',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await admin.deleteVehicle(row.id)
      ElMessage.success('删除成功')
      fetchVehicles()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (form.id) {
          // 编辑
          await admin.updateVehicle(form.id, form)
          ElMessage.success('更新成功')
        } else {
          // 创建（这里传入用户名，假设是 ownerName）
          await admin.createVehicle(form.ownerName, form)
          ElMessage.success('创建成功')
        }
        dialogVisible.value = false
        fetchVehicles()
      } catch (error) {
        console.error('保存失败:', error)
        ElMessage.error('保存失败')
      } finally {
        submitting.value = false
      }
    }
  })
}


// 分页大小变化
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchVehicles()
}

// 页码变化
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

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style> 