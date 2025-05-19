<template>
  <el-form :model="model" :rules="rules" ref="formRef" label-width="80px">
    <el-form-item label="用户名" prop="username">
      <el-input v-model="model.username" placeholder="请输入用户名" />
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="model.email" placeholder="请输入邮箱" />
    </el-form-item>
    <el-form-item label="电话" prop="phone">
      <el-input v-model="model.phone" placeholder="请输入电话号码" />
    </el-form-item>
    <el-form-item label="角色" prop="role">
      <el-select v-model="model.role" placeholder="请选择角色">
        <el-option label="用户" value="USER" />
        <el-option label="维修员" value="MECHANIC" />
        <el-option label="管理员" value="ADMIN" />
      </el-select>
    </el-form-item>
    <slot /> <!-- 允许父级插入提交按钮 -->
  </el-form>
</template>

<script setup>
import { ref } from 'vue';
const props = defineProps({
  modelValue: { type: Object, required: true }
});
const emit = defineEmits(['update:modelValue', 'validate']);
const model = ref({ ...props.modelValue });
const formRef = ref();
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  email:    [{ required: true, type: 'email', message: '请输入合法邮箱', trigger: 'blur' }]
};

const validate = () => new Promise((resolve, reject) => {
  formRef.value.validate(valid => {
    if (valid) { emit('update:modelValue', model.value); resolve(model.value); }
    else reject();
  });
});

// 暴露校验方法给父组件
defineExpose({ validate });
</script>
