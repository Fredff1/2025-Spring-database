<template>
  <div>
    <page-header title="服务反馈" />
    <el-form :model="form" label-width="90px" class="mt-20">
      <el-form-item label="工单号">
        <el-input v-model="form.orderId" placeholder="请输入工单号" />
      </el-form-item>
      <el-form-item label="评分">
        <el-rate v-model="form.score" />
      </el-form-item>
      <el-form-item label="反馈内容">
        <el-input v-model="form.content" type="textarea" rows="4" placeholder="说说您的体验" />
      </el-form-item>
      <el-button type="primary" @click="submit">提交反馈</el-button>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { sendFeedback } from '@/api/user';
import PageHeader from '@/components/common/PageHeader.vue';
import { ElMessage } from 'element-plus';

const form = ref({ orderId: '', score: 0, content: '' });
const submit = async () => {
  await sendFeedback(form.value);
  ElMessage.success('感谢您的反馈！(mock)');
  form.value = { orderId: '', score: 0, content: '' };
};
</script>
