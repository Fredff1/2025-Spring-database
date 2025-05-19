# Vue 3 + Vite

This template should help get you started developing with Vue 3 in Vite. The template uses Vue 3 `<script setup>` SFCs, check out the [script setup docs](https://v3.vuejs.org/api/sfc-script-setup.html#sfc-script-setup) to learn more.

Learn more about IDE Support for Vue in the [Vue Docs Scaling up Guide](https://vuejs.org/guide/scaling-up/tooling.html#ide-support).

# 统计分析 API 文档

## 1. 获取概览数据
GET /api/statistics/overview
参数：
- timeRange: string (week/month/quarter/year)
响应：
{
  totalOrders: number,
  orderTrend: number,
  completionRate: number,
  completionTrend: number,
  averageRepairTime: number,
  repairTimeTrend: number,
  satisfactionRate: number,
  satisfactionTrend: number
}

## 2. 获取维修类型分布
GET /api/statistics/repair-type-distribution
参数：
- timeRange: string
响应：
{
  labels: string[],
  values: number[]
}

## 3. 获取收入趋势
GET /api/statistics/income-trend
参数：
- timeRange: string
响应：
{
  labels: string[],
  values: number[]
}

## 4. 获取维修时长分布
GET /api/statistics/repair-time-distribution
参数：
- timeRange: string
响应：
{
  labels: string[],
  values: number[]
}

## 5. 获取客户来源分布
GET /api/statistics/customer-source-distribution
参数：
- timeRange: string
响应：
{
  labels: string[],
  values: number[]
}

## 6. 获取详细数据
GET /api/statistics/detailed-data
参数：
- timeRange: string
响应：
{
  data: Array<{
    date: string,
    totalOrders: number,
    completedOrders: number,
    avgRepairTime: number,
    revenue: number,
    satisfaction: number
  }>
}

## 7. 导出报表
GET /api/statistics/export-report
参数：
- timeRange: string
响应：
- 文件流 (application/vnd.openxmlformats-officedocument.spreadsheetml.sheet)
