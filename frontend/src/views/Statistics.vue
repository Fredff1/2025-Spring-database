<template>
  <div class="statistics">
    <div class="page-header">
      <h1>统计分析</h1>
      <div class="header-actions">
        <select class="form-input" v-model="timeRange" @change="loadData">
          <option value="week">本周</option>
          <option value="month">本月</option>
          <option value="quarter">本季度</option>
          <option value="year">本年</option>
        </select>
        <button class="btn btn-secondary" @click="exportReport">
          <i class="fas fa-download"></i>
          导出报表
        </button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="card stat-card">
        <div class="stat-title">维修订单总数</div>
        <div class="stat-value">{{ totalOrders }}</div>
        <div class="stat-trend" :class="{ 'trend-up': orderTrend > 0 }">
          {{ orderTrend > 0 ? '+' : '' }}{{ orderTrend }}%
        </div>
      </div>
      <div class="card stat-card">
        <div class="stat-title">完成率</div>
        <div class="stat-value">{{ completionRate }}%</div>
        <div class="stat-trend" :class="{ 'trend-up': completionTrend > 0 }">
          {{ completionTrend > 0 ? '+' : '' }}{{ completionTrend }}%
        </div>
      </div>
      <div class="card stat-card">
        <div class="stat-title">平均维修时长</div>
        <div class="stat-value">{{ averageRepairTime }}小时</div>
        <div class="stat-trend" :class="{ 'trend-up': repairTimeTrend < 0 }">
          {{ repairTimeTrend > 0 ? '+' : '' }}{{ repairTimeTrend }}%
        </div>
      </div>
      <div class="card stat-card">
        <div class="stat-title">客户满意度</div>
        <div class="stat-value">{{ satisfactionRate }}%</div>
        <div class="stat-trend" :class="{ 'trend-up': satisfactionTrend > 0 }">
          {{ satisfactionTrend > 0 ? '+' : '' }}{{ satisfactionTrend }}%
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-grid">
      <!-- 维修类型分布 -->
      <div class="card chart-card">
        <h3>维修类型分布</h3>
        <div class="chart-container">
          <canvas ref="repairTypeChart"></canvas>
        </div>
      </div>

      <!-- 收入趋势 -->
      <div class="card chart-card">
        <h3>收入趋势</h3>
        <div class="chart-container">
          <canvas ref="incomeTrendChart"></canvas>
        </div>
      </div>

      <!-- 维修时长分布 -->
      <div class="card chart-card">
        <h3>维修时长分布</h3>
        <div class="chart-container">
          <canvas ref="repairTimeChart"></canvas>
        </div>
      </div>

      <!-- 客户来源分布 -->
      <div class="card chart-card">
        <h3>客户来源分布</h3>
        <div class="chart-container">
          <canvas ref="customerSourceChart"></canvas>
        </div>
      </div>
    </div>

    <!-- 详细数据表格 -->
    <div class="card">
      <h3>详细数据</h3>
      <table class="table">
        <thead>
          <tr>
            <th>日期</th>
            <th>维修订单数</th>
            <th>完成订单数</th>
            <th>平均维修时长</th>
            <th>总收入</th>
            <th>客户满意度</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="data in detailedData" :key="data.date">
            <td>{{ formatDate(data.date) }}</td>
            <td>{{ data.totalOrders }}</td>
            <td>{{ data.completedOrders }}</td>
            <td>{{ data.avgRepairTime }}小时</td>
            <td>¥{{ formatNumber(data.revenue) }}</td>
            <td>{{ data.satisfaction }}%</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { statistics } from '@/api'
import Chart from 'chart.js/auto'
import { formatDate, formatNumber } from '@/utils/formatters'
import { STATISTICS_TIME_RANGES } from '@/utils/constants'

export default {
  name: 'Statistics',
  data() {
    return {
      timeRange: STATISTICS_TIME_RANGES.MONTH,
      totalOrders: 0,
      orderTrend: 0,
      completionRate: 0,
      completionTrend: 0,
      averageRepairTime: 0,
      repairTimeTrend: 0,
      satisfactionRate: 0,
      satisfactionTrend: 0,
      detailedData: [],
      charts: {
        repairType: null,
        incomeTrend: null,
        repairTime: null,
        customerSource: null
      }
    }
  },
  methods: {
    formatDate,
    formatNumber,
    async loadData() {
      try {
        // 加载概览数据
        const overview = await statistics.getOverview({ timeRange: this.timeRange })
        this.updateOverviewData(overview)

        // 加载维修类型分布数据
        const repairTypeData = await statistics.getRepairTypeDistribution({ timeRange: this.timeRange })
        this.updateRepairTypeChart(repairTypeData)

        // 加载收入趋势数据
        const incomeTrendData = await statistics.getIncomeTrend({ timeRange: this.timeRange })
        this.updateIncomeTrendChart(incomeTrendData)

        // 加载维修时长分布数据
        const repairTimeData = await statistics.getRepairTimeDistribution({ timeRange: this.timeRange })
        this.updateRepairTimeChart(repairTimeData)

        // 加载客户来源分布数据
        const customerSourceData = await statistics.getCustomerSourceDistribution({ timeRange: this.timeRange })
        this.updateCustomerSourceChart(customerSourceData)

        // 加载详细数据
        const detailedData = await statistics.getDetailedData({ timeRange: this.timeRange })
        this.detailedData = detailedData
      } catch (error) {
        console.error('加载统计数据失败:', error)
        // 这里可以添加错误提示
      }
    },
    updateOverviewData(data) {
      this.totalOrders = data.totalOrders
      this.orderTrend = data.orderTrend
      this.completionRate = data.completionRate
      this.completionTrend = data.completionTrend
      this.averageRepairTime = data.averageRepairTime
      this.repairTimeTrend = data.repairTimeTrend
      this.satisfactionRate = data.satisfactionRate
      this.satisfactionTrend = data.satisfactionTrend
    },
    updateRepairTypeChart(data) {
      if (this.charts.repairType) {
        this.charts.repairType.destroy()
      }

      const ctx = this.$refs.repairTypeChart.getContext('2d')
      this.charts.repairType = new Chart(ctx, {
        type: 'pie',
        data: {
          labels: data.labels,
          datasets: [{
            data: data.values,
            backgroundColor: [
              '#4CAF50',
              '#2196F3',
              '#FFC107',
              '#9C27B0',
              '#FF5722'
            ]
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: 'right'
            }
          }
        }
      })
    },
    updateIncomeTrendChart(data) {
      if (this.charts.incomeTrend) {
        this.charts.incomeTrend.destroy()
      }

      const ctx = this.$refs.incomeTrendChart.getContext('2d')
      this.charts.incomeTrend = new Chart(ctx, {
        type: 'line',
        data: {
          labels: data.labels,
          datasets: [{
            label: '收入',
            data: data.values,
            borderColor: '#4CAF50',
            tension: 0.1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                callback: value => '¥' + value.toLocaleString()
              }
            }
          }
        }
      })
    },
    updateRepairTimeChart(data) {
      if (this.charts.repairTime) {
        this.charts.repairTime.destroy()
      }

      const ctx = this.$refs.repairTimeChart.getContext('2d')
      this.charts.repairTime = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: data.labels,
          datasets: [{
            label: '维修时长分布',
            data: data.values,
            backgroundColor: '#2196F3'
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            y: {
              beginAtZero: true,
              title: {
                display: true,
                text: '订单数量'
              }
            },
            x: {
              title: {
                display: true,
                text: '维修时长（小时）'
              }
            }
          }
        }
      })
    },
    updateCustomerSourceChart(data) {
      if (this.charts.customerSource) {
        this.charts.customerSource.destroy()
      }

      const ctx = this.$refs.customerSourceChart.getContext('2d')
      this.charts.customerSource = new Chart(ctx, {
        type: 'doughnut',
        data: {
          labels: data.labels,
          datasets: [{
            data: data.values,
            backgroundColor: [
              '#4CAF50',
              '#2196F3',
              '#FFC107',
              '#9C27B0',
              '#FF5722'
            ]
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: 'right'
            }
          }
        }
      })
    },
    async exportReport() {
      try {
        const response = await statistics.exportReport({ timeRange: this.timeRange })
        const url = window.URL.createObjectURL(new Blob([response]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', `统计报表_${this.timeRange}_${new Date().toISOString().split('T')[0]}.xlsx`)
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      } catch (error) {
        console.error('导出报表失败:', error)
        // 这里可以添加错误提示
      }
    }
  },
  mounted() {
    this.loadData()
  },
  beforeUnmount() {
    // 清理图表实例
    Object.values(this.charts).forEach(chart => {
      if (chart) {
        chart.destroy()
      }
    })
  }
}
</script>

<style scoped>
.statistics {
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

.stat-trend {
  font-size: 0.9rem;
  color: var(--danger);
}

.stat-trend.trend-up {
  color: var(--success);
}

.charts-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-lg);
}

.chart-card {
  padding: var(--spacing-md);
}

.chart-card h3 {
  margin-bottom: var(--spacing-md);
  color: var(--text-primary);
}

.chart-container {
  height: 300px;
  position: relative;
}

/* 响应式调整 */
@media (max-width: 1200px) {
  .charts-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-grid {
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