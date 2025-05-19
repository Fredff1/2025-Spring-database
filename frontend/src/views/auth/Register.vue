<template>
  <div class="login-container">
    <el-card class="login-card" shadow="hover">
      <h2 class="title">注册新用户</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="用户名" prop="username"><el-input v-model="form.username"/></el-form-item>
        <el-form-item label="邮箱" prop="email"><el-input v-model="form.email"/></el-form-item>
        <el-form-item label="密码" prop="password"><el-input v-model="form.password" type="password"/></el-form-item>
        <el-form-item label="确认密码" prop="confirm"><el-input v-model="form.confirm" type="password"/></el-form-item>
        <el-button type="primary" class="w-full" @click="onSubmit">注册</el-button>
        <div class="tips">已有账号？<router-link to="/login">登录</router-link></div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { register } from '@/api/user';
import { ElMessage } from 'element-plus';

const router = useRouter();
const form = ref({ username:'', email:'', password:'', confirm:'' });
const rules = {
  username:[{required:true,message:'必填',trigger:'blur'}],
  email:[{required:true,type:'email',message:'邮箱格式',trigger:'blur'}],
  password:[{required:true,min:6,message:'至少6位',trigger:'blur'}],
  confirm:[{validator:(_,v)=>v===form.value.password,trigger:'blur',message:'两次密码不一致'}]
};
const formRef=ref();
const onSubmit=()=>{
  formRef.value.validate(async valid=>{
    if(!valid)return;
    await register(form.value);
    ElMessage.success('注册成功，请登录');
    router.push('/login');
  });
};
</script>

<style scoped lang="scss">@import './Login.vue';</style>