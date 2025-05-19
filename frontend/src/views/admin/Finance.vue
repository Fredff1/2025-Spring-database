 <template>
  <div class="inventory">
    <div class="page-header">
      <h2 class="page-title">库存管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><plus /></el-icon>
        添加配件
      </el-button>
    </div>

    <!-- 搜索栏 -->
    <el-card shadow="hover" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="配件名称">
          <el-input v-model="searchForm.name" placeholder="请输入配件名称" />
        </el-form-item>
        <el-form-item label="配件类型">
          <el-select v-model="searchForm.type" placeholder="请选择配件类型">
            <el-option label="全部" value="" />
            <el-option label="发动机配件" value="engine" />
            <el-option label="制动系统" value="brake" />
            <el-option label="悬挂系统" value="suspension" />
            <el-option label="电气系统" value="electrical" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="库存状态">
          <el-select v-model="searchForm.status" placeholder="请选择库存状态">
            <el-option label="全部" value="" />
            <el-option label="充足" value="sufficient" />
            <el-option label="不足" value="insufficient" />
            <el-option label="缺货" value="out_of_stock" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 配件列表 -->
    <el-card shadow="hover">
      <el-table :data="items" style="width: 100%">
        <el-table-column prop="name" label="配件名称" min-width="150" />
        <el-table-column prop="type" label="配件类型" width="120">
          <template #default="{ row }">
            {{ getTypeText(row.type) }}
          </template>
        </el-table-column>
        <el-table-column prop="specification" label="规格型号" width="150" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="price" label="单价" width="120">
          <template #default="{ row }">
            ¥{{ row.price.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存数量" width="120" />
        <el-table-column prop="minStock" label="最低库存" width="120" />
        <el-table-column prop="status" label="库存状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="supplier" label="供应商" width="150" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="success" link @click="handleStock(row)">入库</el-button>
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

    <!-- 添加/编辑配件对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑配件' : '添加配件'"
      width="600px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="配件名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="配件类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择配件类型">
            <el-option label="发动机配件" value="engine" />
            <el-option label="制动系统" value="brake" />
            <el-option label="悬挂系统" value="suspension" />
            <el-option label="电气系统" value="electrical" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="规格型号" prop="specification">
          <el-input v-model="form.specification" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" />
        </el-form-item>
        <el-form-item label="单价" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" :step="0.01" />
        </el-form-item>
        <el-form-item label="库存数量" prop="stock">
          <el-input-number v-model="form.stock" :min="0" :step="1" />
        </el-form-item>
        <el-form-item label="最低库存" prop="minStock">
          <el-input-number v-model="form.minStock" :min="0" :step="1" />
        </el-form-item>
        <el-form-item label="供应商" prop="supplier">
          <el-input v-model="form.supplier" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 入库对话框 -->
    <el-dialog
      v-model="stockDialogVisible"
      title="配件入库"
      width="500px"
    >
      <el-form
        ref="stockFormRef"
        :model="stockForm"
        :rules="stockRules"
        label-width="100px"
      >
        <el-form-item label="配件名称">
          <span>{{ currentItem?.name }}</span>
        </el-form-item>
        <el-form-item label="当前库存">
          <span>{{ currentItem?.stock }}</span>
        </el-form-item>
        <el-form-item label="入库数量" prop="quantity">
          <el-input-number v-model="stockForm.quantity" :min="1" :step="1" />
        </el-form-item>
        <el-form-item label="入库备注" prop="remark">
          <el-input v-model="stockForm.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="stockDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmStock">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 删除确认对话框 -->
    <el-dialog
      v-model="deleteDialogVisible"
      title="确认删除"
      width="400px"
    >
      <p>确定要删除该配件吗？此操作不可恢复。</p>
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
import { Plus } from '@element-plus/icons-vue'
import { admin } from '@/api'

// 搜索表单
const searchForm = ref({
  name: '',
  type: '',
  status: ''
})

// 配件列表
const items = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 对话框控制
const dialogVisible = ref(false)
const stockDialogVisible = ref(false)
const deleteDialogVisible = ref(false)
const isEdit = ref(false)
const currentItem = ref(null)

// 表单
const formRef = ref(null)
const form = ref({
  name: '',
  type: '',
  specification: '',
  unit: '',
  price: 0,
  stock: 0,
  minStock: 0,
  supplier: '',
  remark: ''
})

// 入库表单
const stockFormRef = ref(null)
const stockForm = ref({
  quantity: 1,
  remark: ''
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入配件名称', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择配件类型', trigger: 'change' }
  ],
  specification: [
    { required: true, message: '请输入规格型号', trigger: 'blur' }
  ],
  unit: [
    { required: true, message: '请输入单位', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入单价', trigger: 'blur' }
  ],
  stock: [
    { required: true, message: '请输入库存数量', trigger: 'blur' }
  ],
  minStock: [
    { required: true, message: '请输入最低库存', trigger: 'blur' }
  ],
  supplier: [
    { required: true, message: '请输入供应商', trigger: 'blur' }
  ]
}

// 入库表单验证规则
const stockRules = {
  quantity: [
    { required: true, message: '请输入入库数量', trigger: 'blur' }
  ]
}

// 获取配件类型文本
const getTypeText = (type) => {
  const map = {
    engine: '发动机配件',
    brake: '制动系统',
    suspension: '悬挂系统',
    electrical: '电气系统',
    other: '其他'
  }
  return map[type] || type
}

// 获取状态标签
const getStatusTag = (status) => {
  const map = {
    sufficient: 'success',
    insufficient: 'warning',
    out_of_stock: 'danger'
  }
  return map[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const map = {
    sufficient: '充足',
    insufficient: '不足',
    out_of_stock: '缺货'
  }
  return map[status] || status
}

// 获取配件列表
const fetchItems = async () => {
  try {
    const res = await admin.getInventory({
      page: currentPage.value,
      limit: pageSize.value,
      ...searchForm.value
    })
    items.value = res.list
    total.value = res.total
  } catch (error) {
    console.error('获取配件列表失败:', error)
    ElMessage.error('获取配件列表失败')
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchItems()
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    name: '',
    type: '',
    status: ''
  }
  handleSearch()
}

// 添加配件
const handleAdd = () => {
  isEdit.value = false
  form.value = {
    name: '',
    type: '',
    specification: '',
    unit: '',
    price: 0,
    stock: 0,
    minStock: 0,
    supplier: '',
    remark: ''
  }
  dialogVisible.value = true
}

// 编辑配件
const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

// 入库
const handleStock = (row) => {
  currentItem.value = row
  stockForm.value = {
    quantity: 1,
    remark: ''
  }
  stockDialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  currentItem.value = row
  deleteDialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await admin.updateInventoryItem(form.value.id, form.value)
          ElMessage.success('更新成功')
        } else {
          await admin.addInventoryItem(form.value)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        fetchItems()
      } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error('操作失败')
      }
    }
  })
}

// 确认入库
const confirmStock = async () => {
  if (!stockFormRef.value) return
  
  await stockFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await admin.addStock(currentItem.value.id, stockForm.value)
        ElMessage.success('入库成功')
        stockDialogVisible.value = false
        fetchItems()
      } catch (error) {
        console.error('入库失败:', error)
        ElMessage.error('入库失败')
      }
    }
  })
}

// 确认删除
const confirmDelete = async () => {
  try {
    await admin.deleteInventoryItem(currentItem.value.id)
    ElMessage.success('删除成功')
    deleteDialogVisible.value = false
    fetchItems()
  } catch (error) {
    console.error('删除失败:', error)
    ElMessage.error('删除失败')
  }
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchItems()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchItems()
}

onMounted(() => {
  fetchItems()
})
</script>

<style scoped>
.inventory {
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
</style> 