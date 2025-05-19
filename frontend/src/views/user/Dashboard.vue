<template>
  <div class="dashboard">
    <page-header title="仪表盘" />
    <el-row :gutter="20" class="mt-20">
      <el-col :span="6"><stat-card :count="userProfile.username" label="当前用户" /></el-col>
      <el-col :span="6"><stat-card :count="stats.totalOrders" label="历史工单" /></el-col>
      <el-col :span="6"><stat-card :count="stats.pendingOrders" label="进行中工单" /></el-col>
      <el-col :span="6"><stat-card :count="stats.cost" label="累计消费(元)" /></el-col>
    </el-row>
    <line-chart :x-data="trend.dates" :series="trend.values" class="mt-30" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { profile as getProfile, listUserOrders } from '@/api/user';
import PageHeader from '@/components/common/PageHeader.vue';
import StatCard   from '@/components/common/StatCard.vue';
import LineChart  from '@/components/common/LineChart.vue';

const userProfile = ref({});
const stats   = ref({ totalOrders: 0, pendingOrders: 0, cost: 0 });
const trend   = ref({ dates: [], values: [] });

onMounted(async () => {
  userProfile.value = await getProfile();
  const orders = await listUserOrders();
  stats.value.totalOrders   = orders.length;
  stats.value.pendingOrders = orders.filter(o => o.status === 'IN_PROGRESS').length;
  trend.value.dates  = orders.map(o => o.createdAt);
  trend.value.values = orders.map((_, idx) => idx + 1);
});
</script>

<style scoped>.mt-20{margin-top:20px}.mt-30{margin-top:30px}</style>