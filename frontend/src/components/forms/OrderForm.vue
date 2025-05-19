<template>
  <el-form :model="model" :rules="rules" ref="formRef" label-width="90px">
    <el-form-item label="VIN" prop="vin">
      <el-input v-model="model.vin" placeholder="车辆VIN" />
    </el-form-item>
    <el-form-item label="故障描述" prop="faultDesc">
      <el-input
        v-model="model.faultDesc"
        type="textarea"
        :autosize="{ minRows: 3, maxRows: 6 }"
        placeholder="请描述车辆故障" />
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
  vin: [{ required: true, min: 17, max: 17, message: 'VIN 必须17位', trigger: 'blur' }],
  faultDesc: [{ required: true, message: '请填写故障描述', trigger: 'blur' }]
};
const validate = () => new Promise((resolve, reject) => {
  formRef.value.validate(valid => { if (valid) { emit('update:modelValue', model.value); resolve(model.value);} else reject(); });
});
defineExpose({ validate });
</script>