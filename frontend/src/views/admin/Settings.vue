<template>
  <div class="settings">
    <div class="page-header">
      <h2 class="page-title">系统设置</h2>
    </div>

    <el-tabs v-model="activeTab">
      <!-- 基本设置 -->
      <el-tab-pane label="基本设置" name="basic">
        <el-card shadow="hover">
          <el-form
            ref="basicFormRef"
            :model="basicForm"
            :rules="basicRules"
            label-width="120px"
          >
            <el-form-item label="系统名称" prop="systemName">
              <el-input v-model="basicForm.systemName" />
            </el-form-item>
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="basicForm.contactPhone" />
            </el-form-item>
            <el-form-item label="联系邮箱" prop="contactEmail">
              <el-input v-model="basicForm.contactEmail" />
            </el-form-item>
            <el-form-item label="营业时间" prop="businessHours">
              <el-time-picker
                v-model="basicForm.businessHours"
                is-range
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                format="HH:mm"
              />
            </el-form-item>
            <el-form-item label="系统公告" prop="announcement">
              <el-input
                v-model="basicForm.announcement"
                type="textarea"
                :rows="4"
                placeholder="请输入系统公告内容"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleBasicSubmit">保存</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <!-- 维修设置 -->
      <el-tab-pane label="维修设置" name="repair">
        <el-card shadow="hover">
          <el-form
            ref="repairFormRef"
            :model="repairForm"
            :rules="repairRules"
            label-width="120px"
          >
            <el-form-item label="维修类型" prop="repairTypes">
              <el-tag
                v-for="type in repairForm.repairTypes"
                :key="type"
                closable
                class="mx-1"
                @close="handleRemoveType(type)"
              >
                {{ type }}
              </el-tag>
              <el-input
                v-if="inputVisible"
                ref="typeInputRef"
                v-model="inputValue"
                class="w-20"
                size="small"
                @keyup.enter="handleInputConfirm"
                @blur="handleInputConfirm"
              />
              <el-button v-else class="button-new-tag" size="small" @click="showInput">
                + 添加类型
              </el-button>
            </el-form-item>
            <el-form-item label="最低维修费" prop="minRepairFee">
              <el-input-number v-model="repairForm.minRepairFee" :min="0" :precision="2" />
            </el-form-item>
            <el-form-item label="工时费" prop="hourlyRate">
              <el-input-number v-model="repairForm.hourlyRate" :min="0" :precision="2" />
            </el-form-item>
            <el-form-item label="自动分配" prop="autoAssign">
              <el-switch v-model="repairForm.autoAssign" />
            </el-form-item>
            <el-form-item label="分配规则" prop="assignRule" v-if="repairForm.autoAssign">
              <el-radio-group v-model="repairForm.assignRule">
                <el-radio label="round_robin">轮询分配</el-radio>
                <el-radio label="workload">工作量优先</el-radio>
                <el-radio label="rating">评分优先</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleRepairSubmit">保存</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <!-- 库存设置 -->
      <el-tab-pane label="库存设置" name="inventory">
        <el-card shadow="hover">
          <el-form
            ref="inventoryFormRef"
            :model="inventoryForm"
            :rules="inventoryRules"
            label-width="120px"
          >
            <el-form-item label="库存预警" prop="stockWarning">
              <el-switch v-model="inventoryForm.stockWarning" />
            </el-form-item>
            <el-form-item label="预警阈值" prop="warningThreshold" v-if="inventoryForm.stockWarning">
              <el-input-number v-model="inventoryForm.warningThreshold" :min="1" :max="100" />
            </el-form-item>
            <el-form-item label="自动补货" prop="autoReplenish">
              <el-switch v-model="inventoryForm.autoReplenish" />
            </el-form-item>
            <el-form-item label="补货数量" prop="replenishQuantity" v-if="inventoryForm.autoReplenish">
              <el-input-number v-model="inventoryForm.replenishQuantity" :min="1" />
            </el-form-item>
            <el-form-item label="库存盘点" prop="inventoryCheck">
              <el-select v-model="inventoryForm.inventoryCheck">
                <el-option label="每月" value="monthly" />
                <el-option label="每季度" value="quarterly" />
                <el-option label="每半年" value="semi_annual" />
                <el-option label="每年" value="annual" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleInventorySubmit">保存</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>

      <!-- 通知设置 -->
      <el-tab-pane label="通知设置" name="notification">
        <el-card shadow="hover">
          <el-form
            ref="notificationFormRef"
            :model="notificationForm"
            :rules="notificationRules"
            label-width="120px"
          >
            <el-form-item label="邮件通知" prop="emailNotification">
              <el-switch v-model="notificationForm.emailNotification" />
            </el-form-item>
            <el-form-item label="SMTP服务器" prop="smtpServer" v-if="notificationForm.emailNotification">
              <el-input v-model="notificationForm.smtpServer" />
            </el-form-item>
            <el-form-item label="SMTP端口" prop="smtpPort" v-if="notificationForm.emailNotification">
              <el-input-number v-model="notificationForm.smtpPort" :min="1" :max="65535" />
            </el-form-item>
            <el-form-item label="发件人邮箱" prop="senderEmail" v-if="notificationForm.emailNotification">
              <el-input v-model="notificationForm.senderEmail" />
            </el-form-item>
            <el-form-item label="邮箱密码" prop="emailPassword" v-if="notificationForm.emailNotification">
              <el-input v-model="notificationForm.emailPassword" type="password" />
            </el-form-item>
            <el-form-item label="短信通知" prop="smsNotification">
              <el-switch v-model="notificationForm.smsNotification" />
            </el-form-item>
            <el-form-item label="短信API密钥" prop="smsApiKey" v-if="notificationForm.smsNotification">
              <el-input v-model="notificationForm.smsApiKey" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleNotificationSubmit">保存</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { admin } from '@/api'

// 当前激活的标签页
const activeTab = ref('basic')

// 基本设置表单
const basicFormRef = ref(null)
const basicForm = ref({
  systemName: '',
  contactPhone: '',
  contactEmail: '',
  businessHours: [],
  announcement: ''
})

// 维修设置表单
const repairFormRef = ref(null)
const repairForm = ref({
  repairTypes: [],
  minRepairFee: 0,
  hourlyRate: 0,
  autoAssign: false,
  assignRule: 'round_robin'
})

// 库存设置表单
const inventoryFormRef = ref(null)
const inventoryForm = ref({
  stockWarning: false,
  warningThreshold: 20,
  autoReplenish: false,
  replenishQuantity: 10,
  inventoryCheck: 'monthly'
})

// 通知设置表单
const notificationFormRef = ref(null)
const notificationForm = ref({
  emailNotification: false,
  smtpServer: '',
  smtpPort: 465,
  senderEmail: '',
  emailPassword: '',
  smsNotification: false,
  smsApiKey: ''
})

// 维修类型输入
const inputVisible = ref(false)
const inputValue = ref('')
const typeInputRef = ref(null)

// 表单验证规则
const basicRules = {
  systemName: [
    { required: true, message: '请输入系统名称', trigger: 'blur' }
  ],
  contactPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  contactEmail: [
    { required: true, message: '请输入联系邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  businessHours: [
    { required: true, message: '请选择营业时间', trigger: 'change' }
  ]
}

const repairRules = {
  repairTypes: [
    { required: true, message: '请至少添加一个维修类型', trigger: 'change' }
  ],
  minRepairFee: [
    { required: true, message: '请输入最低维修费', trigger: 'blur' }
  ],
  hourlyRate: [
    { required: true, message: '请输入工时费', trigger: 'blur' }
  ]
}

const inventoryRules = {
  warningThreshold: [
    { required: true, message: '请输入预警阈值', trigger: 'blur' }
  ],
  replenishQuantity: [
    { required: true, message: '请输入补货数量', trigger: 'blur' }
  ],
  inventoryCheck: [
    { required: true, message: '请选择库存盘点周期', trigger: 'change' }
  ]
}

const notificationRules = {
  smtpServer: [
    { required: true, message: '请输入SMTP服务器地址', trigger: 'blur' }
  ],
  smtpPort: [
    { required: true, message: '请输入SMTP端口', trigger: 'blur' }
  ],
  senderEmail: [
    { required: true, message: '请输入发件人邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  emailPassword: [
    { required: true, message: '请输入邮箱密码', trigger: 'blur' }
  ],
  smsApiKey: [
    { required: true, message: '请输入短信API密钥', trigger: 'blur' }
  ]
}

// 获取设置
const fetchSettings = async () => {
  try {
    const [basic, repair, inventory, notification] = await Promise.all([
      admin.getBasicSettings(),
      admin.getRepairSettings(),
      admin.getInventorySettings(),
      admin.getNotificationSettings()
    ])
    basicForm.value = basic
    repairForm.value = repair
    inventoryForm.value = inventory
    notificationForm.value = notification
  } catch (error) {
    console.error('获取设置失败:', error)
    ElMessage.error('获取设置失败')
  }
}

// 保存基本设置
const handleBasicSubmit = async () => {
  if (!basicFormRef.value) return
  
  await basicFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await admin.updateBasicSettings(basicForm.value)
        ElMessage.success('保存成功')
      } catch (error) {
        console.error('保存失败:', error)
        ElMessage.error('保存失败')
      }
    }
  })
}

// 保存维修设置
const handleRepairSubmit = async () => {
  if (!repairFormRef.value) return
  
  await repairFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await admin.updateRepairSettings(repairForm.value)
        ElMessage.success('保存成功')
      } catch (error) {
        console.error('保存失败:', error)
        ElMessage.error('保存失败')
      }
    }
  })
}

// 保存库存设置
const handleInventorySubmit = async () => {
  if (!inventoryFormRef.value) return
  
  await inventoryFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await admin.updateInventorySettings(inventoryForm.value)
        ElMessage.success('保存成功')
      } catch (error) {
        console.error('保存失败:', error)
        ElMessage.error('保存失败')
      }
    }
  })
}

// 保存通知设置
const handleNotificationSubmit = async () => {
  if (!notificationFormRef.value) return
  
  await notificationFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await admin.updateNotificationSettings(notificationForm.value)
        ElMessage.success('保存成功')
      } catch (error) {
        console.error('保存失败:', error)
        ElMessage.error('保存失败')
      }
    }
  })
}

// 显示维修类型输入框
const showInput = () => {
  inputVisible.value = true
  nextTick(() => {
    typeInputRef.value.focus()
  })
}

// 处理维修类型输入确认
const handleInputConfirm = () => {
  if (inputValue.value) {
    repairForm.value.repairTypes.push(inputValue.value)
  }
  inputVisible.value = false
  inputValue.value = ''
}

// 移除维修类型
const handleRemoveType = (type) => {
  repairForm.value.repairTypes = repairForm.value.repairTypes.filter(t => t !== type)
}

onMounted(() => {
  fetchSettings()
})
</script>

<style scoped>
.settings {
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

.mx-1 {
  margin: 0 4px;
}

.w-20 {
  width: 100px;
}

.button-new-tag {
  margin-left: 8px;
}
</style> 