<template>
  <div class="repairman-layout">
    <header class="header">
      <div class="logo">汽车维修管理系统 - 维修人员</div>
      <div class="user-info">
        <el-dropdown>
          <span class="user-dropdown">
            {{ userInfo.username }}
            <el-icon><arrow-down /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="goToProfile">个人资料</el-dropdown-item>
              <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </header>
    
    <div class="main">
      <aside class="sidebar">
        <el-menu
          :default-active="activeMenu"
          class="menu"
          router
        >
          <el-menu-item index="/repairman/dashboard">
            <el-icon><home-filled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/repairman/orders">
            <el-icon><tools /></el-icon>
            <span>维修订单</span>
          </el-menu-item>
          <el-menu-item index="/repairman/assignments">
            <el-icon><document /></el-icon>
            <span>待处理任务</span>
          </el-menu-item>
          <el-menu-item index="/repairman/profile">
            <el-icon><money /></el-icon>
            <span>概览</span>
          </el-menu-item>
        </el-menu>
      </aside>
      
      <main class="content">
        <router-view></router-view>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import {
  HomeFilled,
  Tools,
  Document,
  Money,
  ArrowDown
} from '@element-plus/icons-vue'

import { logout } from '@/utils/auth'

const router = useRouter()
const route = useRoute()
const userStore = useAuthStore()

const userInfo = computed(() => userStore.userInfo)
const activeMenu = computed(() => route.path)

const goToProfile = () => {
  router.push('/repairman/profile')
}

const handleLogout = async () => {
  await logout()
  router.push('/login')
}
</script>

<style scoped>
.repairman-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  height: 60px;
  background: white;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.logo {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--primary);
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
}

.main {
  flex: 1;
  display: flex;
  background: #f5f7fa;
}

.sidebar {
  width: 240px;
  background: white;
  border-right: 1px solid #eee;
}

.menu {
  border-right: none;
}

.content {
  flex: 1;
  padding: 2rem;
  overflow-y: auto;
}
</style> 