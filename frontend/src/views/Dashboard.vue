<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="card stat-card">
        <div class="stat-icon">
          <i class="fas fa-tools"></i>
        </div>
        <div class="stat-content">
          <h3>待处理工单</h3>
          <p class="stat-number">{{ stats.pendingOrders }}</p>
        </div>
      </div>
      <div class="card stat-card">
        <div class="stat-icon">
          <i class="fas fa-car"></i>
        </div>
        <div class="stat-content">
          <h3>在修车辆</h3>
          <p class="stat-number">{{ stats.inProgressVehicles }}</p>
        </div>
      </div>
      <div class="card stat-card">
        <div class="stat-icon">
          <i class="fas fa-users"></i>
        </div>
        <div class="stat-content">
          <h3>今日客户</h3>
          <p class="stat-number">{{ stats.todayCustomers }}</p>
        </div>
      </div>
      <div class="card stat-card">
        <div class="stat-icon">
          <i class="fas fa-chart-line"></i>
        </div>
        <div class="stat-content">
          <h3>本月收入</h3>
          <p class="stat-number">¥{{ stats.monthlyIncome }}</p>
        </div>
      </div>
    </div>

    <!-- 最近工单 -->
    <div class="card">
      <div class="card-header">
        <h2>最近工单</h2>
        <router-link to="/repair-orders" class="btn btn-secondary">
          查看全部
        </router-link>
      </div>
      <table class="table">
        <thead>
          <tr>
            <th>工单号</th>
            <th>车牌号</th>
            <th>维修类型</th>
            <th>状态</th>
            <th>创建时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in recentOrders" :key="order.id">
            <td>{{ order.id }}</td>
            <td>{{ order.plateNumber }}</td>
            <td>{{ order.repairType }}</td>
            <td>
              <span :class="['status-tag', `status-tag-${order.status}`]">
                {{ getStatusText(order.status) }}
              </span>
            </td>
            <td>{{ formatDate(order.createdAt) }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 维修类型统计 -->
    <div class="card">
      <div class="card-header">
        <h2>维修类型统计</h2>
      </div>
      <div class="chart-container">
        <div class="chart-placeholder">
          <i class="fas fa-chart-pie"></i>
          <p>维修类型分布图表</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        pendingOrders: 12,
        inProgressVehicles: 8,
        todayCustomers: 25,
        monthlyIncome: '128,500'
      },
      recentOrders: [
        {
          id: 'RO20240301001',
          plateNumber: '京A12345',
          repairType: '发动机维修',
          status: 'pending',
          createdAt: '2024-03-01T10:00:00'
        },
        {
          id: 'RO20240301002',
          plateNumber: '京B67890',
          repairType: '轮胎更换',
          status: 'in_progress',
          createdAt: '2024-03-01T11:30:00'
        },
        {
          id: 'RO20240301003',
          plateNumber: '京C13579',
          repairType: '空调维修',
          status: 'completed',
          createdAt: '2024-03-01T14:20:00'
        }
      ]
    }
  },
  methods: {
    getStatusText(status) {
      const statusMap = {
        pending: '待处理',
        in_progress: '维修中',
        completed: '已完成',
        cancelled: '已取消'
      }
      return statusMap[status] || status
    },
    formatDate(date) {
      return new Date(date).toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
  }
}
</script>

<style scoped>
.dashboard {
  padding: var(--spacing-md);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-lg);
}

.stat-card {
  display: flex;
  align-items: center;
  padding: var(--spacing-lg);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-md);
  background-color: var(--primary-color);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  margin-right: var(--spacing-md);
}

.stat-content h3 {
  font-size: 0.9rem;
  color: var(--text-secondary);
  margin-bottom: var(--spacing-xs);
}

.stat-number {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-primary);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-md);
}

.card-header h2 {
  font-size: 1.2rem;
  color: var(--text-primary);
}

.chart-container {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chart-placeholder {
  text-align: center;
  color: var(--text-secondary);
}

.chart-placeholder i {
  font-size: 3rem;
  margin-bottom: var(--spacing-md);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .card-header {
    flex-direction: column;
    gap: var(--spacing-md);
    align-items: flex-start;
  }
}
</style> 