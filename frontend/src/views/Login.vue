<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>登录</h2>
        </div>
      </template>
      
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        @submit.prevent="handleLogin"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>

        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="请选择角色">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="维修人员" value="REPAIRMAN" />
            <el-option label="客户" value="CUSTOMER" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" native-type="submit" :loading="loading">
            登录
          </el-button>
          <el-button @click="$router.push('/register')">
            没有账号？去注册
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { setToken } from '@/utils/auth'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const form = ref({
  username: '',
  password: '',
  role: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ]
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true

    const response = await fetch('/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(form.value)
    })

    
    if (response.ok) {
      const data = await response.json()
      // 保存token和角色信息
      setToken(data.token)
      ElMessage.success('登录成功')
      
      // 根据角色跳转到不同页面
      switch (data.role) {
        case 'ADMIN':
          router.push('/admin/dashboard')
          break
        case 'REPAIRMAN':
          router.push('/repairman/dashboard')
          break
        case 'CUSTOMER':
          router.push('/user/dashboard')
          break
      }
    } else {
      ElMessage.error(data.message || '登录失败')
    }
  } catch (error) {
    console.log(error)
    ElMessage.error('登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.login-card {
  width: 480px;
}

.card-header {
  text-align: center;
}

.card-header h2 {
  margin: 0;
  color: #303133;
}
</style> 