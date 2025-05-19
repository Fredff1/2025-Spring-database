<template>
  <el-page-header @back="$router.back()" class="page-header">
    <template #content>
      <span class="title">{{ title }}</span>
    </template>
  </el-page-header>
</template>

<script setup>
const props = defineProps({ title: { type: String, default: '页面标题' } });
</script>

<style scoped lang="scss">
.page-header {
  padding: 16px;
  background: #fff;
  border-bottom: 1px solid #ebeef5;
  .title { font-size: 18px; font-weight: 600; }
}
</style>
