<template>
  <div class="statistics">
    <!-- 1. 负面反馈统计（带 period 切换） -->
    <el-row :gutter="20" style="margin-bottom: 48px;">
      <el-col :span="24">
        <el-card class="stat-card-large">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>负面反馈统计</span>
              <!-- period 切换 -->
              <el-radio-group 
              v-model="negativeFeedbackPeriod" 
              size="small"
              @change="fetchNegativeFeedback">
                <el-radio-button label="recent">最近</el-radio-button>
                <el-radio-button label="all">全部</el-radio-button>
              </el-radio-group>
            </div>
          </template>

          <div class="table-wrapper">
            <el-table
              :data="negativeFeedbackList"
              border stripe
              style="width: 90%;"
              :row-key="row => row.feedbackId"
            >
              <el-table-column prop="customerName" label="用户" width="100" />
              <el-table-column prop="feedbackType"    label="反馈类型" width="120" >
                <template #default="{ row }">
                    <el-tag :type="getFeedbackTag(row.feedbackType)">
                    {{ getFeedbackText(row.feedbackType) }}
                    </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="评分" width="120">
                <template #default="{ row }">
                  <span v-if="row.rating == null">未给出</span>
                  <el-rate v-else v-model="row.rating" disabled show-score />
                </template>
              </el-table-column>
              <el-table-column prop="technicians" label="技师" min-width="150" />
              <el-table-column prop="content"     label="内容" min-width="200" />
              <el-table-column prop="date"        label="时间" width="180">
                <template #default="{ row }">
                  {{ formatDateTime(row.date) }}
                </template>
              </el-table-column>
            </el-table>
          </div>

          <div style="text-align: right; margin-top: 12px;">
            <el-pagination
              v-model:current-page="negativeFeedbackPage"
              :page-size="negativeFeedbackLimit"
              :total="negativeFeedbackTotal"
              layout="prev, pager, next"
              @current-change="fetchNegativeFeedback"
            />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 2. 成本分析 & 车辆维修：一行两个，并带 period 切换 -->
    <el-row :gutter="20" style="margin-bottom: 24px;">
      <el-col :span="12">
        <el-card class="stat-card">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>成本分析</span>
              <el-radio-group v-model="costPeriod" size="small" @change="fetchCostProportion">
                <el-radio-button label="recent">最近</el-radio-button>
                <el-radio-button label="all">全部</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="costPieChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card class="stat-card">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>车辆维修统计</span>
              <el-radio-group 
              v-model="vehiclePeriod" 
              size="small"
              @change="fetchVehicleOrderStats">
                <el-radio-button label="recent">最近</el-radio-button>
                <el-radio-button label="all">全部</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="vehicleBarChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 3. 故障类型统计 & 订单流程统计：一行两个，并带 period 切换 -->
    <el-row :gutter="20" style="margin-bottom: 24px;">
      <el-col :span="12">
        <el-card class="stat-card">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>故障类型统计</span>
              <el-radio-group 
              v-model="faultTypePeriod" 
              size="small"
              @change="fetchFaultTypeStats">
                <el-radio-button label="recent">最近</el-radio-button>
                <el-radio-button label="all">全部</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="faultTypePieChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card class="stat-card">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>订单流程统计</span>
              <el-radio-group 
              v-model="orderProcessPeriod" 
              size="small"
               @change="fetchOrderProcessStats">
                <el-radio-button label="recent">最近</el-radio-button>
                <el-radio-button label="all">全部</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="orderProcessBarChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-bottom: 24px;">
      <el-col :span="12">
        <el-card class="stat-card">
          <template #header>维修类型不匹配统计</template>
          <div class="table-wrapper">
            <el-table :data="mismatchList" border stripe style="width: 90%;">
              <el-table-column prop="orderId"            label="订单ID"     width="120" />
              <el-table-column prop="orderFaultType"     label="期望类型"   width="120" >
                 <template #default="{ row }">
                    <el-tag :type="getRepairTypeTag(row.orderFaultType)">
                    {{ getRepairTypeText(row.orderFaultType) }}
                    </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="repairmanSpecialty" label="实际类型"   width="120" >
                 <template #default="{ row }">
                    <el-tag :type="getRepairTypeTag(row.repairmanSpecialty)">
                    {{ getRepairTypeText(row.repairmanSpecialty) }}
                    </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="repairmanName"      label="维修人员"   width="150" />
            </el-table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card class="stat-card">
          <template #header>按故障类型 - 未完成订单统计</template>
          <div class="table-wrapper">
            <el-table :data="unfinishedByFaultType" border stripe style="width: 90%;">
              <el-table-column prop="name"  label="故障类型" >
                <template #default="{ row }">
                    <el-tag :type="getRepairTypeTag(row.name)">
                    {{ getRepairTypeText(row.name) }}
                    </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="count" label="数量"     width="100" />
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-bottom: 24px;">
      <el-col :span="12">
        <el-card class="stat-card">
          <template #header>按维修人员 - 未完成订单统计</template>
          <div class="table-wrapper">
            <el-table :data="unfinishedByRepairman" border stripe style="width: 90%;">
              <el-table-column prop="repairmanName" label="维修人员" width="150" />
              <el-table-column prop="specialty"     label="专业类型" width="120" >
                <template #default="{ row }">
                    <el-tag :type="getRepairTypeTag(row.specialty)">
                    {{ getRepairTypeText(row.specialty) }}
                    </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="count"         label="数量"     width="100" />
            </el-table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card class="stat-card">
          <template #header>按车辆 - 未完成订单统计</template>
          <div class="table-wrapper">
            <el-table :data="unfinishedByVehicle" border stripe style="width: 90%;">
              <el-table-column prop="licensePlate" label="车牌号"   width="150" />
              <el-table-column prop="brand"        label="品牌"     width="100" />
              <el-table-column prop="model"        label="型号"     width="120" >
                <template #default="{ row }">
                    {{ getModelText(row.model) }}
                </template>
              </el-table-column>
              <el-table-column prop="count"        label="数量"     width="100" />
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import * as admin from '@/api/admin'

// 负面反馈统计
const negativeFeedbackList = ref([])
const negativeFeedbackTotal = ref(0)
const negativeFeedbackPage = ref(1)
const negativeFeedbackLimit = ref(10)
const negativeFeedbackPeriod = ref('recent')

// 成本分析
const costPieChart = ref(null)
const costPieChartInstance = ref(null)
const costPeriod = ref('recent')

// 车辆维修统计
const vehicleBarChart = ref(null)
const vehicleBarChartInstance = ref(null)
const vehiclePeriod = ref('recent')

// 故障类型统计
const faultTypePieChart = ref(null)
const chartInst = ref(null)
const chartData = ref({ level1: {}, level2: {} })
const faultTypePeriod = ref('recent')

// 订单流程统计
const orderProcessBarChart = ref(null)
const orderProcessBarChartInstance = ref(null)
const orderProcessPeriod = ref('recent')

// 维修类型不匹配统计
const mismatchList = ref([])


const unfinishedByFaultType = ref([])

const unfinishedByRepairman = ref([])

const unfinishedByVehicle = ref([])


function formatDateString(d) {
  if (!d) return ''
  const yyyy = d.getFullYear()
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd}`
}

// 更新时间：格式化成 "YYYY-MM-DD hh:mm:ss"
const formatDateTime = dt => {
  if (!dt) return ''
  const d = new Date(dt)
  const yyyy = d.getFullYear()
  const mm = String(d.getMonth() + 1).padStart(2, '0')
  const dd = String(d.getDate()).padStart(2, '0')
  const hh = String(d.getHours()).padStart(2, '0')
  const mi = String(d.getMinutes()).padStart(2, '0')
  const ss = String(d.getSeconds()).padStart(2, '0')
  return `${yyyy}-${mm}-${dd} ${hh}:${mi}:${ss}`
}

const getFeedbackText = (value) => {
  const map = {
    RATING: '评分反馈',
    URGENT: '催单反馈',
    GENERAL: '一般反馈',
  }
  return map[value] || value
}

const getFeedbackTag = (value) => {
  const map = {
    RATING: 'success',
    URGENT: 'danger',
    GENERAL: 'info',
  }
  return map[value] || value
}

const getModelText = (model) => {
    const map = {
        SUV: '多用途车',
        Sedan: '轿车',
        Pickup: '皮卡',
        Van: '货车',
        Hatchback: '小型车',
        Coupe:'跑车',
        Convertible:'敞篷车'

   }
  return map[model] || type
}

const getRepairTypeTag = (type) => {
  const map = {
    MAINTENANCE: 'success',
    REPAIR: 'danger',
    PAINT: 'warning',
    TIRE: 'warning',
    OTHER: 'info'
  }
  return map[type] || 'info'
}

// 获取维修类型文本
const getRepairTypeText = (type) => {
  const map = {
    MAINTENANCE: '常规保养',
    REPAIR: '通用维修',
    PAINT: '钣金喷漆',
    TIRE: '轮胎更换',
    ELECTRICAL: '电路系统修复',
    BODYWORK: '车身修复',
    ENGINE: '发动机维修',
    OTHER: '其他'
  }
  return map[type] || type
}

// 获取状态标签
const getOrderStatusTag = (status) => {
  const map = {
    PENDING: 'info',
    PROCESSING: 'success',
    COMPLETED: 'success',
    CANCELLED: 'danger'
  }
  return map[status] || 'info'
}

// 获取状态文本
const getOrderStatusText = (status) => {
  const map = {
    PENDING: '待处理',
    PROCESSING: '维修中',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

// 负面反馈统计
const fetchNegativeFeedback = async () => {
  try {
    const res = await admin.getNegativeFeedbackStats({
      page: negativeFeedbackPage.value,
      limit: negativeFeedbackLimit.value,
      period: negativeFeedbackPeriod.value 
    });
    // 假设 res = { list: [...], total: 6 }

    // 1) 把后端原始字段映射到 table props 对应的字段：
    negativeFeedbackList.value = (res.list || []).map(item => ({
      feedbackType: item.feedbackType,
      customerName: item.customerName,         // 用户名
      content:      item.feedbackContent,      // 反馈内容，映射到 prop="content"
      date:         item.feedbackTime,         // 反馈时间，映射到 prop="date"
      technicians:  item.repairmanName.join('、'), // 把技师列表转成 “mike、nancy” 这种字符串
      rating:       item.rating,               // 评分 (比如 later 用 el-rate 展示)
      faultType:    item.faultType,            // 故障类型
      orderId:      item.orderId               // 如果想展示订单ID 也可以加一列
    }));
    console.log(negativeFeedbackList.value)
    negativeFeedbackTotal.value = res.total || 0;
  } catch (e) {
    ElMessage.error('获取负面反馈统计失败');
  }
};

const fetchCostProportion = async () => {
  try {
    const res = await admin.getCostProportionStats({ period: costPeriod.value });
    // 组装 ECharts 期望的 data 数组
    const chartData = [
      { name: '材料成本', value: res.materialCost },
      { name: '人工成本', value: res.laborCost }
    ];

    // 等 DOM 更新完毕后再初始化/更新图表
    await nextTick();
    if (!costPieChartInstance.value) {
      costPieChartInstance.value = echarts.init(costPieChart.value);
    } else {
      // 如果已有实例，先清空旧配置
      costPieChartInstance.value.clear();
    }
    costPieChartInstance.value.setOption({
      title: { text: '成本分析', left: 'center' },
      tooltip: { trigger: 'item', formatter: '{a} <br/>{b}: {c} ({d}%)' },
      legend: { bottom: 0 },
      series: [
        {
          name: '成本占比(￥)',
          type: 'pie',
          radius: '50%',
          data: chartData,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    });
  } catch (e) {
    // 如果请求失败，销毁旧实例
    if (costPieChartInstance.value) {
      costPieChartInstance.value.dispose();
      costPieChartInstance.value = null;
    }
    ElMessage.error('获取成本分析失败');
  }
};

// 车辆维修统计
const fetchVehicleOrderStats = async () => {
  try {
    const res = await admin.getVehicleOrderStats({ period: vehiclePeriod.value })
    const xData = res.map(i => getModelText(i.model))
    const yData = res.map(i => i.repairCount)
    const avgData = res.map(i => i.averageCost);
    await nextTick();
    if (!vehicleBarChartInstance.value) {
      vehicleBarChartInstance.value = echarts.init(vehicleBarChart.value);
    }

    vehicleBarChartInstance.value.setOption({
      title: { text: '车型维修统计', left: 'center' },
      xAxis: { type: 'category', data: xData },
      yAxis: [
        {
          type: 'value',
          name: '维修次数',
          minInterval: 1,
          axisLine: { lineStyle: { color: '#5470C6' } },
          axisLabel: { formatter: '{value}' }
        },
        {
          type: 'value',
          name: '平均费用（¥）',
          position: 'right',
          axisLine: { lineStyle: { color: '#EE6666' } },
          axisLabel: { formatter: value => `¥${value}` }
        }
      ],
      series: [
        {
          name: '维修次数',
          type: 'bar',
          data: yData,
          itemStyle: { color: '#5470C6' }
        },
        {
          name: '平均费用',
          type: 'line',
          yAxisIndex: 1,  
          data: avgData,
          itemStyle: { color: '#EE6666' },
          label: {
            show: true,
            position: 'top',
            formatter: params => `¥${params.data.toFixed(2)}`
          },
          smooth: true 
        }
      ],
      legend: {
        data: ['维修次数', '平均费用'],
        top: 30
      }
    });
  } catch (e) {
    ElMessage.error('获取车辆维修统计失败')
  }
}

function renderLevel1() {
  const level1 = chartData.value.level1
  chartInst.value.clear()
  chartInst.value.setOption({
    title: { text: '故障类型分布', left: 'center' },
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [
      {
        name: '故障类型',
        type: 'pie',
        radius: ['40%', '70%'],
        label: { formatter: '{b}: {d}%' },
        data: Object.entries(level1).map(([type, count]) => ({
          name: getRepairTypeText(type),
          value: count,
          rawType: type
        }))
      }
    ]
  })
}

// 点击事件：根据 rawType 下钻到车型，或点击“← 返回”回到第一级
function bindClick() {
  chartInst.value.off('click')
  chartInst.value.on('click', (params) => {
    if (params.name === '← 返回') {
      renderLevel1()
      return
    }
    const t = params.data.rawType
    if (!t) return
    const models = chartData.value.level2[t] || []
    chartInst.value.setOption({
      title: { text: `${getRepairTypeText(t)} - 车型分布`, left: 'center' },
      legend: { bottom: 0 },
      series: [
        {
          name: '车型',
          type: 'pie',
          radius: ['40%', '70%'],
          label: { formatter: '{b}: {d}%' },
          data: models
        },
        {
          type: 'pie',
          radius: ['0', '30%'],
          labelLine: { show: false },
          data: [
            {
              value: 1,
              name: '← 返回',
              itemStyle: { color: 'rgba(0,0,0,0)' },
              label: { show: true, position: 'center', fontSize: 14, color: '#666' }
            }
          ]
        }
      ]
    })
  })
}

// 拉取并组织数据
const fetchFaultTypeStats = async () => {
  try {
    const raw = await admin.getVehicleFaultTypeStats({ period: faultTypePeriod.value })
    // raw 格式：[{ model, faultType, count }, ...]
    const level1 = {}
    const level2 = {}
    raw.forEach(r => {
      level1[r.faultType] = (level1[r.faultType] || 0) + r.count
      if (!level2[r.faultType]) level2[r.faultType] = []
      level2[r.faultType].push({
        name: getModelText(r.model),
        value: r.count
      })
    })
    chartData.value = { level1, level2 }

    await nextTick()
    if (!chartInst.value) {
      chartInst.value = echarts.init(faultTypePieChart.value)
    } else {
      chartInst.value.clear()
    }
    renderLevel1()
    bindClick()
    // 在短延迟后触发一次 resize，确保容器稳定
    setTimeout(() => {
      chartInst.value && chartInst.value.resize()
    }, 100)
  } catch (e) {
    ElMessage.error('获取故障类型统计失败')
  }
}

const fetchOrderProcessStats = async () => {
  try {
    const end = new Date()
    const begin = new Date()
    if (orderProcessPeriod.value === 'recent') {
      begin.setDate(end.getDate() - 30)
    } else {
      begin.setFullYear(end.getFullYear() - 10)
    }
    const beginStr = begin.toISOString().slice(0, 10)
    const endStr   = end.toISOString().slice(0, 10)

    const res = await admin.getOrderProcessStats({
      begin: beginStr,
      end:   endStr
    })

    const xData = res.map(i => getRepairTypeText(i.faultType))

    const pending   = res.map(i => i.pendingOrders)
    const processing= res.map(i => i.processingOrders)
    const finished  = res.map(i => i.completedOrders)

    const sumTotal = res.reduce((sum, i) => sum + i.totalOrders, 0)


    const percentPending    = res.map(i => {
      return sumTotal === 0
        ? '0%'
        : ((i.pendingOrders    / sumTotal) * 100).toFixed(1) + '%'
    })
    const percentProcessing = res.map(i => {
      return sumTotal === 0
        ? '0%'
        : ((i.processingOrders / sumTotal) * 100).toFixed(1) + '%'
    })
    const percentFinished   = res.map(i => {
      return sumTotal === 0
        ? '0%'
        : ((i.completedOrders  / sumTotal) * 100).toFixed(1) + '%'
    })

    await nextTick()
    if (!orderProcessBarChartInstance.value) {
      orderProcessBarChartInstance.value = echarts.init(orderProcessBarChart.value)
    } else {
      orderProcessBarChartInstance.value.clear()
    }

    orderProcessBarChartInstance.value.setOption({
      title: { text: '订单流程统计', left: 'center' },
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
      legend: { data: ['待处理', '处理中', '已完成'], bottom: 10 },
      grid: { left: '3%', right: '4%', bottom: '10%', containLabel: true },
      xAxis: {
        type: 'category',
        data: xData,
        axisLabel: { rotate: 30, interval: 0 }
      },
      yAxis: { type: 'value', minInterval: 1 },
      series: [
        {
          name: '待处理',
          type: 'bar',
          data: pending,
          barWidth: '20%',
          itemStyle: { color: '#2196f3' },
          label: {
            show: true,
            position: 'top',
            formatter: params => percentPending[params.dataIndex]
          }
        },
        {
          name: '处理中',
          type: 'bar',
          data: processing,
          barWidth: '20%',
          itemStyle: { color: '#ff9800' },
          label: {
            show: true,
            position: 'top',
            formatter: params => percentProcessing[params.dataIndex]
          }
        },
        {
          name: '已完成',
          type: 'bar',
          data: finished,
          barWidth: '20%',
          itemStyle: { color: '#4caf50' },
          label: {
            show: true,
            position: 'top',
            formatter: params => percentFinished[params.dataIndex]
          }
        }
      ]
    })

  } catch (e) {
    ElMessage.error('获取订单流程统计失败')
  }
}

// 维修类型不匹配统计
const fetchMismatchStats = async () => {
  try {
    const res = await admin.getOrderMismatchStats()
    mismatchList.value = res || []
  } catch (e) {
    ElMessage.error('获取维修类型不匹配统计失败')
  }
}

const fetchUnfinishedOrderStats = async () => {
  try {
    // 同时并行请求三条接口
    const [faultTypeRes, repairmanRes, vehicleRes] = await Promise.all([
      admin.getUnfinishedOrderFaultTypeStats(),
      admin.getUnfinishedOrderRepairmanStats(),
      admin.getUnfinishedOrderVehicleStats()
    ])


    unfinishedByFaultType.value = faultTypeRes.map(item => ({
      name: item.faultType,
      count: item.count
    }))

    unfinishedByRepairman.value = repairmanRes.map(item => ({
      repairmanName: item.repairmanName,
      specialty: item.specialty,
      count: item.count
    }))


    unfinishedByVehicle.value = vehicleRes.map(item => ({
      licensePlate: item.licensePlate,
      brand: item.brand,
      model: item.model,
      count: item.count
    }))
  } catch (e) {
    ElMessage.error('获取未完成订单统计失败')
  }
}


onMounted(() => {
  fetchNegativeFeedback()
  fetchCostProportion()
  fetchVehicleOrderStats()
  fetchFaultTypeStats()
  fetchOrderProcessStats()
  fetchMismatchStats()
  fetchUnfinishedOrderStats()
})
</script>

<style scoped>
.statistics {
  padding: 20px;
}

/* 页面标题 */
.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}
.page-title {
  font-size: 24px;
  font-weight: 600;
}

/* 各卡片宽度 100%，由 el-col 控制栅格 */
.stat-card-large {
  width: 100%;
}
.stat-card {
  width: 100%;
}

/* 表格水平居中 */
.table-wrapper {
  display: flex;
  justify-content: center;
  padding: 16px 0;
}

/* 图表容器固定高度 */
.chart-container {
  height: 300px;
}
</style>
