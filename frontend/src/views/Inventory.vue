<template>
  <div class="inventory">
    <div class="page-header">
      <h1>库存管理</h1>
      <button class="btn btn-primary" @click="addItem">
        <i class="fas fa-plus"></i>
        添加配件
      </button>
    </div>

    <!-- 筛选器 -->
    <div class="card filters">
      <div class="filter-group">
        <label class="form-label">配件类型</label>
        <select class="form-input" v-model="filters.type">
          <option value="">全部</option>
          <option value="engine">发动机配件</option>
          <option value="brake">制动系统</option>
          <option value="electrical">电气系统</option>
          <option value="body">车身配件</option>
        </select>
      </div>
      <div class="filter-group">
        <label class="form-label">库存状态</label>
        <select class="form-input" v-model="filters.status">
          <option value="">全部</option>
          <option value="in_stock">有库存</option>
          <option value="low_stock">库存不足</option>
          <option value="out_of_stock">无库存</option>
        </select>
      </div>
      <div class="filter-group">
        <label class="form-label">搜索</label>
        <input type="text" class="form-input" v-model="filters.search" placeholder="搜索配件名称/编号">
      </div>
    </div>

    <!-- 库存列表 -->
    <div class="card">
      <table class="table">
        <thead>
          <tr>
            <th>配件编号</th>
            <th>配件名称</th>
            <th>类型</th>
            <th>库存数量</th>
            <th>单价</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in filteredItems" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.name }}</td>
            <td>{{ getTypeText(item.type) }}</td>
            <td>{{ item.quantity }}</td>
            <td>¥{{ item.price }}</td>
            <td>
              <span :class="['status-tag', `status-tag-${getStockStatus(item.quantity)}`]">
                {{ getStockStatusText(item.quantity) }}
              </span>
            </td>
            <td>
              <button class="btn btn-secondary" @click="viewItem(item)">
                <i class="fas fa-eye"></i>
                查看
              </button>
              <button class="btn btn-primary" @click="editItem(item)">
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
  name: 'Inventory',
  data() {
    return {
      items: [
        {
          id: 'P001',
          name: '机油滤芯',
          type: 'engine',
          quantity: 50,
          price: 45.00
        },
        {
          id: 'P002',
          name: '刹车片',
          type: 'brake',
          quantity: 5,
          price: 120.00
        },
        {
          id: 'P003',
          name: '火花塞',
          type: 'electrical',
          quantity: 0,
          price: 35.00
        }
      ],
      filters: {
        type: '',
        status: '',
        search: ''
      },
      currentPage: 1,
      itemsPerPage: 10
    }
  },
  computed: {
    filteredItems() {
      let result = this.items

      if (this.filters.type) {
        result = result.filter(item => item.type === this.filters.type)
      }

      if (this.filters.status) {
        result = result.filter(item => this.getStockStatus(item.quantity) === this.filters.status)
      }

      if (this.filters.search) {
        const search = this.filters.search.toLowerCase()
        result = result.filter(item => 
          item.id.toLowerCase().includes(search) ||
          item.name.toLowerCase().includes(search)
        )
      }

      const start = (this.currentPage - 1) * this.itemsPerPage
      const end = start + this.itemsPerPage
      return result.slice(start, end)
    },
    totalPages() {
      return Math.ceil(this.items.length / this.itemsPerPage)
    }
  },
  methods: {
    getTypeText(type) {
      const typeMap = {
        engine: '发动机配件',
        brake: '制动系统',
        electrical: '电气系统',
        body: '车身配件'
      }
      return typeMap[type] || type
    },
    getStockStatus(quantity) {
      if (quantity === 0) return 'danger'
      if (quantity < 10) return 'warning'
      return 'success'
    },
    getStockStatusText(quantity) {
      if (quantity === 0) return '无库存'
      if (quantity < 10) return '库存不足'
      return '有库存'
    },
    addItem() {
      // 实现添加配件逻辑
    },
    viewItem(item) {
      // 实现查看配件详情逻辑
    },
    editItem(item) {
      // 实现编辑配件逻辑
    }
  }
}
</script>

<style scoped>
.inventory {
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