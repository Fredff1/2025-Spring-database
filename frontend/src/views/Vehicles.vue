<template>
  <div class="vehicles">
    <div class="page-header">
      <h1>车辆管理</h1>
      <button class="btn btn-primary" @click="addVehicle">
        <i class="fas fa-plus"></i>
        添加车辆
      </button>
    </div>

    <!-- 筛选器 -->
    <div class="card filters">
      <div class="filter-group">
        <label class="form-label">品牌</label>
        <select class="form-input" v-model="filters.brand">
          <option value="">全部</option>
          <option value="Toyota">丰田</option>
          <option value="Honda">本田</option>
          <option value="BMW">宝马</option>
          <option value="Benz">奔驰</option>
        </select>
      </div>
      <div class="filter-group">
        <label class="form-label">搜索</label>
        <input type="text" class="form-input" v-model="filters.search" placeholder="搜索车牌号/车主">
      </div>
    </div>

    <!-- 车辆列表 -->
    <div class="card">
      <table class="table">
        <thead>
          <tr>
            <th>车牌号</th>
            <th>品牌</th>
            <th>型号</th>
            <th>车主</th>
            <th>联系电话</th>
            <th>最后维修时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="vehicle in filteredVehicles" :key="vehicle.plateNumber">
            <td>{{ vehicle.plateNumber }}</td>
            <td>{{ vehicle.brand }}</td>
            <td>{{ vehicle.model }}</td>
            <td>{{ vehicle.owner }}</td>
            <td>{{ vehicle.phone }}</td>
            <td>{{ formatDate(vehicle.lastRepairDate) }}</td>
            <td>
              <button class="btn btn-secondary" @click="viewVehicle(vehicle)">
                <i class="fas fa-eye"></i>
                查看
              </button>
              <button class="btn btn-primary" @click="editVehicle(vehicle)">
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
  name: 'Vehicles',
  data() {
    return {
      vehicles: [
        {
          plateNumber: '京A12345',
          brand: 'Toyota',
          model: '卡罗拉',
          owner: '张三',
          phone: '13800138000',
          lastRepairDate: '2024-02-15T10:00:00'
        },
        {
          plateNumber: '京B67890',
          brand: 'Honda',
          model: '雅阁',
          owner: '李四',
          phone: '13900139000',
          lastRepairDate: '2024-02-20T14:30:00'
        }
      ],
      filters: {
        brand: '',
        search: ''
      },
      currentPage: 1,
      itemsPerPage: 10
    }
  },
  computed: {
    filteredVehicles() {
      let result = this.vehicles

      if (this.filters.brand) {
        result = result.filter(vehicle => vehicle.brand === this.filters.brand)
      }

      if (this.filters.search) {
        const search = this.filters.search.toLowerCase()
        result = result.filter(vehicle => 
          vehicle.plateNumber.toLowerCase().includes(search) ||
          vehicle.owner.toLowerCase().includes(search)
        )
      }

      const start = (this.currentPage - 1) * this.itemsPerPage
      const end = start + this.itemsPerPage
      return result.slice(start, end)
    },
    totalPages() {
      return Math.ceil(this.vehicles.length / this.itemsPerPage)
    }
  },
  methods: {
    formatDate(date) {
      return new Date(date).toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      })
    },
    addVehicle() {
      // 实现添加车辆逻辑
    },
    viewVehicle(vehicle) {
      // 实现查看车辆详情逻辑
    },
    editVehicle(vehicle) {
      // 实现编辑车辆逻辑
    }
  }
}
</script>

<style scoped>
.vehicles {
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