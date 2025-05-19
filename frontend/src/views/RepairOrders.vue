<template>
  <div class="repair-orders">
    <div class="page-header">
      <h1>维修工单</h1>
      <button class="btn btn-primary" @click="createNewOrder">
        <i class="fas fa-plus"></i>
        新建工单
      </button>
    </div>

    <!-- 筛选器 -->
    <div class="card filters">
      <div class="filter-group">
        <label class="form-label">状态</label>
        <select class="form-input" v-model="filters.status">
          <option value="">全部</option>
          <option value="pending">待处理</option>
          <option value="in_progress">维修中</option>
          <option value="completed">已完成</option>
          <option value="cancelled">已取消</option>
        </select>
      </div>
      <div class="filter-group">
        <label class="form-label">时间范围</label>
        <input type="date" class="form-input" v-model="filters.startDate">
        <span>至</span>
        <input type="date" class="form-input" v-model="filters.endDate">
      </div>
      <div class="filter-group">
        <label class="form-label">搜索</label>
        <input type="text" class="form-input" v-model="filters.search" placeholder="搜索工单号/车牌号">
      </div>
    </div>

    <!-- 工单列表 -->
    <div class="card">
      <table class="table">
        <thead>
          <tr>
            <th>工单号</th>
            <th>车牌号</th>
            <th>客户姓名</th>
            <th>维修类型</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in filteredOrders" :key="order.id">
            <td>{{ order.id }}</td>
            <td>{{ order.plateNumber }}</td>
            <td>{{ order.customerName }}</td>
            <td>{{ order.repairType }}</td>
            <td>
              <span :class="['status-tag', `status-tag-${order.status}`]">
                {{ getStatusText(order.status) }}
              </span>
            </td>
            <td>{{ formatDate(order.createdAt) }}</td>
            <td>
              <button class="btn btn-secondary" @click="viewOrder(order)">
                <i class="fas fa-eye"></i>
                查看
              </button>
              <button class="btn btn-primary" @click="editOrder(order)" v-if="order.status === 'pending'">
                <i class="fas fa-edit"></i>
                编辑
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- 分页 -->
      <div class="pagination">
        <button class="btn btn-secondary" :disabled="currentPage === 1" @click="currentPage--">
          上一页
        </button>
        <span class="page-info">第 {{ currentPage }} 页，共 {{ totalPages }} 页</span>
        <button class="btn btn-secondary" :disabled="currentPage === totalPages" @click="currentPage++">
          下一页
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RepairOrders',
  data() {
    return {
      orders: [],
      filters: {
        status: '',
        startDate: '',
        endDate: '',
        search: ''
      },
      currentPage: 1,
      itemsPerPage: 10
    }
  },
  computed: {
    filteredOrders() {
      let result = this.orders

      // 应用状态筛选
      if (this.filters.status) {
        result = result.filter(order => order.status === this.filters.status)
      }

      // 应用日期筛选
      if (this.filters.startDate) {
        result = result.filter(order => new Date(order.createdAt) >= new Date(this.filters.startDate))
      }
      if (this.filters.endDate) {
        result = result.filter(order => new Date(order.createdAt) <= new Date(this.filters.endDate))
      }

      // 应用搜索筛选
      if (this.filters.search) {
        const search = this.filters.search.toLowerCase()
        result = result.filter(order => 
          order.id.toLowerCase().includes(search) ||
          order.plateNumber.toLowerCase().includes(search)
        )
      }

      // 应用分页
      const start = (this.currentPage - 1) * this.itemsPerPage
      const end = start + this.itemsPerPage
      return result.slice(start, end)
    },
    totalPages() {
      return Math.ceil(this.orders.length / this.itemsPerPage)
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
    },
    createNewOrder() {
      // 实现新建工单逻辑
    },
    viewOrder(order) {
      // 实现查看工单详情逻辑
    },
    editOrder(order) {
      // 实现编辑工单逻辑
    }
  },
  created() {
    // 模拟获取工单数据
    this.orders = [
      {
        id: 'RO20240301001',
        plateNumber: '京A12345',
        customerName: '张三',
        repairType: '发动机维修',
        status: 'pending',
        createdAt: '2024-03-01T10:00:00'
      },
      {
        id: 'RO20240301002',
        plateNumber: '京B67890',
        customerName: '李四',
        repairType: '轮胎更换',
        status: 'in_progress',
        createdAt: '2024-03-01T11:30:00'
      }
    ]
  }
}
</script>

<style scoped>
.repair-orders {
  padding: var(--spacing-md);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-lg);
}

.page-header h1 {
  font-size: 1.5rem;
  color: var(--text-primary);
}

.filters {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-lg);
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}

.filter-group span {
  margin: 0 var(--spacing-xs);
  color: var(--text-secondary);
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--spacing-md);
  margin-top: var(--spacing-lg);
}

.page-info {
  color: var(--text-secondary);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .filters {
    grid-template-columns: 1fr;
  }
  
  .page-header {
    flex-direction: column;
    gap: var(--spacing-md);
    align-items: flex-start;
  }
}
</style> 