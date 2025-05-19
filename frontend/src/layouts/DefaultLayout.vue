<template>
  <div class="default-layout">
    <div class="content">
      <router-view></router-view>
    </div>
  </div>
</template>

<script setup>
// Default layout for public pages
</script>

<style scoped>
.default-layout {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.content {
  width: 100%;
  max-width: 400px;
  padding: 2rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
</style> 