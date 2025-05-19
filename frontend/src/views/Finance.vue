<template>
  <div class="finance">
    <div class="page-header">
      <h1>财务管理</h1>
      <div class="header-actions">
        <button class="btn btn-secondary" @click="exportData">
          <i class="fas fa-download"></i>
          导出数据
        </button>
        <button class="btn btn-primary" @click="addTransaction">
          <i class="fas fa-plus"></i>
          添加收支
        </button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="card stat-card">
        <div class="stat-title">本月收入</div>
        <div class="stat-value income">¥{{ formatNumber(monthlyIncome) }}</div>
        <div class="stat-trend" :class="{ 'trend-up': monthlyIncomeTrend > 0 }">
          {{ monthlyIncomeTrend > 0 ? '+' : '' }}{{ monthlyIncomeTrend }}%
        </div>
      </div>
      <div class="card stat-card">
        <div class="stat-title">本月支出</div>
        <div class="stat-value expense">¥{{ formatNumber(monthlyExpense) }}</div>
        <div class="stat-trend" :class="{ 'trend-up': monthlyExpenseTrend > 0 }">
          {{ monthlyExpenseTrend > 0 ? '+' : '' }}{{ monthlyExpenseTrend }}%
        </div>
      </div>
      <div class="card stat-card">
        <div class="stat-title">本月利润</div>
        <div class="stat-value" :class="{ 'income': monthlyProfit > 0, 'expense': monthlyProfit < 0 }">
          ¥{{ formatNumber(monthlyProfit) }}
        </div>
        <div class="stat-trend" :class="{ 'trend-up': monthlyProfitTrend > 0 }">
          {{ monthlyProfitTrend > 0 ? '+' : '' }}{{ monthlyProfitTrend }}%
        </div>
      </div>
      <div class="card stat-card">
        <div class="stat-title">待收款</div>
        <div class="stat-value">¥{{ formatNumber(pendingPayments) }}</div>
        <div class="stat-subtitle">{{ pendingPaymentsCount }}笔待处理</div>
      </div>
    </div>

    <!-- 筛选器 -->
    <div class="card filters">
      <div class="filter-group">
        <label class="form-label">交易类型</label>
        <select class="form-input" v-model="filters.type">
          <option value="">全部</option>
          <option value="income">收入</option>
          <option value="expense">支出</option>
        </select>
      </div>
      <div class="filter-group">
        <label class="form-label">日期范围</label>
        <div class="date-range">
          <input type="date" class="form-input" v-model="filters.startDate">
          <span>至</span>
          <input type="date" class="form-input" v-model="filters.endDate">
        </div>
      </div>
      <div class="filter-group">
        <label class="form-label">搜索</label>
        <input type="text" class="form-input" v-model="filters.search" placeholder="搜索交易描述/订单号">
      </div>
    </div>

    <!-- 交易列表 -->
    <div class="card">
      <table class="table">
        <thead>
          <tr>
            <th>日期</th>
            <th>交易类型</th>
            <th>金额</th>
            <th>描述</th>
            <th>关联订单</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="transaction in filteredTransactions" :key="transaction.id">
            <td>{{ formatDate(transaction.date) }}</td>
            <td>
              <span :class="['status-tag', transaction.type === 'income' ? 'status-tag-success' : 'status-tag-danger']">
                {{ transaction.type === 'income' ? '收入' : '支出' }}
              </span>
            </td>
            <td :class="{ 'income': transaction.type === 'income', 'expense': transaction.type === 'expense' }">
              ¥{{ formatNumber(transaction.amount) }}
            </td>
            <td>{{ transaction.description }}</td>
            <td>{{ transaction.orderId || '-' }}</td>
            <td>
              <span :class="['status-tag', `status-tag-${transaction.status}`]">
                {{ getStatusText(transaction.status) }}
              </span>
            </td>
            <td>
              <button class="btn btn-secondary" @click="viewTransaction(transaction)">
                <i class="fas fa-eye"></i>
                查看
              </button>
              <button class="btn btn-primary" @click="editTransaction(transaction)">
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
  name: 'Finance',
  data() {
    return {
      monthlyIncome: 125000,
      monthlyIncomeTrend: 12.5,
      monthlyExpense: 45000,
      monthlyExpenseTrend: -5.2,
      monthlyProfit: 80000,
      monthlyProfitTrend: 8.3,
      pendingPayments: 25000,
      pendingPaymentsCount: 15,
      transactions: [
        {
          id: 'T001',
          date: '2024-03-15',
          type: 'income',
          amount: 5000,
          description: '维修服务费',
          orderId: 'R001',
          status: 'completed'
        },
        {
          id: 'T002',
          date: '2024-03-14',
          type: 'expense',
          amount: 2000,
          description: '配件采购',
          orderId: null,
          status: 'pending'
        }
      ],
      filters: {
        type: '',
        startDate: '',
        endDate: '',
        search: ''
      },
      currentPage: 1,
      itemsPerPage: 10
    }
  },
  computed: {
    filteredTransactions() {
      let result = this.transactions

      if (this.filters.type) {
        result = result.filter(t => t.type === this.filters.type)
      }

      if (this.filters.startDate) {
        result = result.filter(t => t.date >= this.filters.startDate)
      }

      if (this.filters.endDate) {
        result = result.filter(t => t.date <= this.filters.endDate)
      }

      if (this.filters.search) {
        const search = this.filters.search.toLowerCase()
        result = result.filter(t => 
          t.description.toLowerCase().includes(search) ||
          (t.orderId && t.orderId.toLowerCase().includes(search))
        )
      }

      const start = (this.currentPage - 1) * this.itemsPerPage
      const end = start + this.itemsPerPage
      return result.slice(start, end)
    },
    totalPages() {
      return Math.ceil(this.transactions.length / this.itemsPerPage)
    }
  },
  methods: {
    formatNumber(num) {
      return num.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    formatDate(date) {
      return new Date(date).toLocaleDateString('zh-CN')
    },
    getStatusText(status) {
      const statusMap = {
        completed: '已完成',
        pending: '待处理',
        cancelled: '已取消'
      }
      return statusMap[status] || status
    },
    exportData() {
      // 实现导出数据逻辑
    },
    addTransaction() {
      // 实现添加交易逻辑
    },
    viewTransaction(transaction) {
      // 实现查看交易详情逻辑
    },
    editTransaction(transaction) {
      // 实现编辑交易逻辑
    }
  }
}
</script>

<style scoped>
.finance {
  padding: var(--spacing-md);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-lg);
}

.header-actions {
  display: flex;
  gap: var(--spacing-md);
}

.page-header h1 {
  font-size: 1.5rem;
  color: var(--text-primary);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-lg);
}

.stat-card {
  padding: var(--spacing-md);
}

.stat-title {
  color: var(--text-secondary);
  font-size: 0.9rem;
  margin-bottom: var(--spacing-xs);
}

.stat-value {
  font-size: 1.8rem;
  font-weight: bold;
  margin-bottom: var(--spacing-xs);
}

.stat-value.income {
  color: var(--success);
}

.stat-value.expense {
  color: var(--danger);
}

.stat-trend {
  font-size: 0.9rem;
  color: var(--danger);
}

.stat-trend.trend-up {
  color: var(--success);
}

.stat-subtitle {
  font-size: 0.9rem;
  color: var(--text-secondary);
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

.date-range {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
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
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .filters {
    grid-template-columns: 1fr;
  }
  
  .page-header {
    flex-direction: column;
    gap: var(--spacing-md);
    align-items: flex-start;
  }

  .header-actions {
    width: 100%;
    flex-direction: column;
  }
}
</style> 