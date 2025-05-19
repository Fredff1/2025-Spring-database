<template>
  <div class="app-container">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ 'sidebar-collapsed': isCollapsed }">
      <div class="logo-container">
        <!-- <img src="@/assets/logo.png" alt="Logo" class="logo" /> -->
        <h1 v-if="!isCollapsed">车辆维修管理系统</h1>
      </div>
      <nav class="nav-menu">
        <router-link to="/dashboard" class="nav-item">
          <i class="fas fa-home"></i>
          <span v-if="!isCollapsed">首页</span>
        </router-link>
        <router-link to="/repair-orders" class="nav-item">
          <i class="fas fa-tools"></i>
          <span v-if="!isCollapsed">维修工单</span>
        </router-link>
        <router-link to="/vehicles" class="nav-item">
          <i class="fas fa-car"></i>
          <span v-if="!isCollapsed">车辆管理</span>
        </router-link>
        <router-link to="/inventory" class="nav-item">
          <i class="fas fa-boxes"></i>
          <span v-if="!isCollapsed">库存管理</span>
        </router-link>
        <router-link to="/finance" class="nav-item">
          <i class="fas fa-chart-line"></i>
          <span v-if="!isCollapsed">财务管理</span>
        </router-link>
        <router-link to="/statistics" class="nav-item">
          <i class="fas fa-chart-bar"></i>
          <span v-if="!isCollapsed">统计分析</span>
        </router-link>
      </nav>
    </aside>

    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 顶部导航栏 -->
      <header class="header">
        <div class="header-left">
          <button class="collapse-btn" @click="toggleSidebar">
            <i :class="isCollapsed ? 'fas fa-chevron-right' : 'fas fa-chevron-left'"></i>
          </button>
          <div class="breadcrumb">
            <span>{{ currentRoute }}</span>
          </div>
        </div>
        <div class="header-right">
          <div class="user-info">
            <span class="username">{{ username }}</span>
            <span class="role">{{ userRole }}</span>
          </div>
          <button class="logout-btn" @click="handleLogout">
            <i class="fas fa-sign-out-alt"></i>
          </button>
        </div>
      </header>

      <!-- 页面内容 -->
      <main class="content">
        <router-view></router-view>
      </main>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MainLayout',
  data() {
    return {
      isCollapsed: false,
      username: '用户名',
      userRole: '角色'
    }
  },
  computed: {
    currentRoute() {
      return this.$route.name || '首页'
    }
  },
  methods: {
    toggleSidebar() {
      this.isCollapsed = !this.isCollapsed
    },
    handleLogout() {
      // 实现登出逻辑
    }
  }
}
</script>

<style scoped>
.app-container {
  display: flex;
  min-height: 100vh;
  background-color: #f5f6fa;
}

.sidebar {
  width: 250px;
  background: linear-gradient(180deg, #2c3e50 0%, #1a252f 100%);
  color: white;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
}

.sidebar-collapsed {
  width: 80px;
}

.logo-container {
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo {
  width: 40px;
  height: 40px;
}

.nav-menu {
  padding: 20px 0;
  flex: 1;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  color: #ecf0f1;
  text-decoration: none;
  transition: all 0.3s ease;
  gap: 10px;
}

.nav-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.nav-item.router-link-active {
  background-color: #3498db;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.header {
  height: 60px;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.collapse-btn {
  background: none;
  border: none;
  color: #2c3e50;
  cursor: pointer;
  font-size: 1.2rem;
}

.breadcrumb {
  color: #2c3e50;
  font-weight: 500;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.username {
  font-weight: 500;
  color: #2c3e50;
}

.role {
  font-size: 0.8rem;
  color: #7f8c8d;
}

.logout-btn {
  background: none;
  border: none;
  color: #e74c3c;
  cursor: pointer;
  font-size: 1.2rem;
}

.content {
  padding: 20px;
  flex: 1;
  overflow-y: auto;
}
</style>