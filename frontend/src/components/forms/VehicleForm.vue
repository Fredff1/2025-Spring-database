<template>
  <el-form :model="model" :rules="rules" ref="formRef" label-width="90px">
    <el-form-item label="VIN" prop="vin">
      <el-input v-model="model.vin" placeholder="17位车辆识别号" />
    </el-form-item>
    <el-form-item label="车型" prop="model">
      <el-input v-model="model.model" placeholder="车辆型号" />
    </el-form-item>
    <slot />
  </el-form>
</template>
<script setup>
import { ref } from 'vue';
const props = defineProps({ modelValue: Object });
const emit = defineEmits(['update:modelValue']);
const model = ref({ ...props.modelValue });
const formRef = ref();
const rules = {
  vin:   [{ required: true, min: 17, max: 17, message: 'VIN 必须17位', trigger: 'blur' }],
  model: [{ required: true, message: '请输入车型', trigger: 'blur' }]
};
const validate = () => new Promise((resolve, reject) => {
  formRef.value.validate(valid => { if (valid) { emit('update:modelValue', model.value); resolve(model.value);} else reject(); });
});
defineExpose({ validate });
</script>
