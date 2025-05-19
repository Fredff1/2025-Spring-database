<template>
  <div class="login-container">
    <el-card class="login-card" shadow="hover">
      <h2 class="title">车辆维修管理系统</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-button type="primary" class="w-full" @click="onSubmit">登录</el-button>
        <div class="tips">还没有账号？<router-link to="/register">注册</router-link></div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { login } from '@/api/user';
import { useAuthStore } from '@/store/modules/user';
import { ElMessage } from 'element-plus';

const router = useRouter();
const authStore = useAuthStore();

const form = ref({ username: '', password: '' });
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
};

const formRef = ref();

const onSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return;
    const res = await login(form.value);
    authStore.$patch({ token: res.token, role: res.role, username: form.value.username });
    ElMessage.success('登录成功');
    router.push('/dashboard');
  });
};
</script>

<style scoped lang="scss">
@import "@/styles/variables.scss";
.login-card {
  background: $card-bg;
  backdrop-filter: blur(20px);
  border-radius: $border-radius;
  box-shadow: $box-shadow;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.login-card:hover {
  transform: scale(1.02);
  box-shadow: 0 6px 24px rgba(0,0,0,0.15);
}
.title {
  font-family: $font-family;
  font-weight: 600;
  color: $color-primary;
}
</style>
