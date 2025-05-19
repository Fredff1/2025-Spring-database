<template>
  <div>
    <page-header title="我的工单" />
    <el-table :data="orders" style="width:100%" class="mt-20">
      <el-table-column prop="orderId" label="工单号" width="100" />
      <el-table-column prop="vin" label="车辆VIN" width="180" />
      <el-table-column prop="status" label="状态" width="120" />
      <el-table-column prop="createdAt" label="创建时间" />
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button v-if="row.status==='IN_PROGRESS'" type="text" @click="cancel(row)">取消</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import PageHeader from '@/components/common/PageHeader.vue';
import { listUserOrders } from '@/api/user';
import { ElMessage } from 'element-plus';

const orders = ref([]);
const load = async () => { orders.value = await listUserOrders(); };
const cancel = async (row) => {
  row.status = 'CANCELLED';
  ElMessage.success('工单已取消 (mock)');
};

onMounted(load);
</script>