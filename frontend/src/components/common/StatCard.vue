
<template>
  <el-card shadow="hover" class="stat-card">
    <div class="icon"><slot name="icon" /></div>
    <div class="info">
      <div class="count">{{ count }}</div>
      <div class="label">{{ label }}</div>
    </div>
  </el-card>
</template>

<script setup>

const props = defineProps({ count: [Number, String], label: String });
</script>

<style scoped lang="scss">
@import "@/styles/variables.scss";
.stat-card {
  background: $card-bg;
  backdrop-filter: blur(20px);
  border-radius: $border-radius;
  box-shadow: $box-shadow;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  :hover {
    transform: scale(1.02);
    box-shadow: 0 6px 24px rgba(0,0,0,0.15);
  }
}
.icon { color: $color-primary; }
</style>