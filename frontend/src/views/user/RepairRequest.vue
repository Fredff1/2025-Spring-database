<template>
  <div>
    <page-header title="报修申请" />
    <vehicle-form v-model="form" ref="vform" />
    <el-form-item label="故障描述" class="mt-20">
      <el-input v-model="form.faultDesc" type="textarea" rows="4" placeholder="请描述故障" />
    </el-form-item>
    <el-button type="primary" @click="submit">提交</el-button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { submitRepair } from '@/api/user';
import PageHeader from '@/components/common/PageHeader.vue';
import VehicleForm from '@/components/forms/VehicleForm.vue';
import { ElMessage } from 'element-plus';

const form = ref({ vin: '', model: '', faultDesc: '' });
const vform = ref();
const submit = async () => {
  try {
    await vform.value.validate();
    await submitRepair(form.value);
    ElMessage.success('报修已提交');
    form.value = { vin: '', model: '', faultDesc: '' };
  } catch {}
};
</script>