// 系统常量
export const SYSTEM = {
  NAME: '车辆维修管理系统',
  VERSION: '1.0.0',
  AUTHOR: 'Your Name',
  COPYRIGHT: '© 2024 Your Company. All rights reserved.',
  DESCRIPTION: '一个现代化的车辆维修管理系统',
  KEYWORDS: '车辆维修,管理系统,维修订单,库存管理,财务管理',
  LOGO: '/logo.png',
  FAVICON: '/favicon.ico',
  THEME: {
    PRIMARY: '#1890ff',
    SUCCESS: '#52c41a',
    WARNING: '#faad14',
    ERROR: '#f5222d',
    INFO: '#1890ff',
    BACKGROUND: '#f0f2f5',
    TEXT: '#000000',
    TEXT_SECONDARY: '#666666',
    BORDER: '#d9d9d9',
    DIVIDER: '#e8e8e8',
    DISABLED: '#bfbfbf',
    MASK: 'rgba(0, 0, 0, 0.45)',
    SHADOW: '0 2px 8px rgba(0, 0, 0, 0.15)'
  }
}

// 用户角色
export const ROLES = {
  ADMIN: 'admin',
  MANAGER: 'manager',
  STAFF: 'staff',
  CUSTOMER: 'customer'
}

// 用户角色名称
export const ROLE_NAMES = {
  [ROLES.ADMIN]: '管理员',
  [ROLES.MANAGER]: '经理',
  [ROLES.STAFF]: '员工',
  [ROLES.CUSTOMER]: '客户'
}

// 用户角色权限
export const ROLE_PERMISSIONS = {
  [ROLES.ADMIN]: ['*'],
  [ROLES.MANAGER]: [
    'view:dashboard',
    'view:repair_orders',
    'view:vehicles',
    'view:inventory',
    'view:finance',
    'view:statistics',
    'create:repair_orders',
    'edit:repair_orders',
    'delete:repair_orders',
    'create:vehicles',
    'edit:vehicles',
    'delete:vehicles',
    'create:inventory',
    'edit:inventory',
    'delete:inventory',
    'create:finance',
    'edit:finance',
    'delete:finance'
  ],
  [ROLES.STAFF]: [
    'view:dashboard',
    'view:repair_orders',
    'view:vehicles',
    'view:inventory',
    'create:repair_orders',
    'edit:repair_orders',
    'create:vehicles',
    'edit:vehicles',
    'create:inventory',
    'edit:inventory'
  ],
  [ROLES.CUSTOMER]: [
    'view:dashboard',
    'view:repair_orders',
    'view:vehicles',
    'create:repair_orders',
    'edit:repair_orders'
  ]
}

// 维修订单状态
export const REPAIR_ORDER_STATUS = {
  PENDING: 'pending',
  IN_PROGRESS: 'in_progress',
  COMPLETED: 'completed',
  CANCELLED: 'cancelled'
}

// 维修订单状态名称
export const REPAIR_ORDER_STATUS_NAMES = {
  [REPAIR_ORDER_STATUS.PENDING]: '待处理',
  [REPAIR_ORDER_STATUS.IN_PROGRESS]: '进行中',
  [REPAIR_ORDER_STATUS.COMPLETED]: '已完成',
  [REPAIR_ORDER_STATUS.CANCELLED]: '已取消'
}

// 维修订单状态颜色
export const REPAIR_ORDER_STATUS_COLORS = {
  [REPAIR_ORDER_STATUS.PENDING]: '#faad14',
  [REPAIR_ORDER_STATUS.IN_PROGRESS]: '#1890ff',
  [REPAIR_ORDER_STATUS.COMPLETED]: '#52c41a',
  [REPAIR_ORDER_STATUS.CANCELLED]: '#f5222d'
}

// 维修类型
export const REPAIR_TYPES = {
  REGULAR: 'regular',
  MAJOR: 'major',
  EMERGENCY: 'emergency'
}

// 维修类型名称
export const REPAIR_TYPE_NAMES = {
  [REPAIR_TYPES.REGULAR]: '常规维修',
  [REPAIR_TYPES.MAJOR]: '大修',
  [REPAIR_TYPES.EMERGENCY]: '紧急维修'
}

// 维修类型颜色
export const REPAIR_TYPE_COLORS = {
  [REPAIR_TYPES.REGULAR]: '#1890ff',
  [REPAIR_TYPES.MAJOR]: '#722ed1',
  [REPAIR_TYPES.EMERGENCY]: '#f5222d'
}

// 车辆类型
export const VEHICLE_TYPES = {
  CAR: 'car',
  SUV: 'suv',
  TRUCK: 'truck',
  BUS: 'bus',
  OTHER: 'other'
}

// 车辆类型名称
export const VEHICLE_TYPE_NAMES = {
  [VEHICLE_TYPES.CAR]: '轿车',
  [VEHICLE_TYPES.SUV]: 'SUV',
  [VEHICLE_TYPES.TRUCK]: '卡车',
  [VEHICLE_TYPES.BUS]: '客车',
  [VEHICLE_TYPES.OTHER]: '其他'
}

// 车辆类型颜色
export const VEHICLE_TYPE_COLORS = {
  [VEHICLE_TYPES.CAR]: '#1890ff',
  [VEHICLE_TYPES.SUV]: '#52c41a',
  [VEHICLE_TYPES.TRUCK]: '#722ed1',
  [VEHICLE_TYPES.BUS]: '#fa8c16',
  [VEHICLE_TYPES.OTHER]: '#bfbfbf'
}

// 库存状态
export const INVENTORY_STATUS = {
  IN_STOCK: 'in_stock',
  LOW_STOCK: 'low_stock',
  OUT_OF_STOCK: 'out_of_stock'
}

// 库存状态名称
export const INVENTORY_STATUS_NAMES = {
  [INVENTORY_STATUS.IN_STOCK]: '有库存',
  [INVENTORY_STATUS.LOW_STOCK]: '库存不足',
  [INVENTORY_STATUS.OUT_OF_STOCK]: '无库存'
}

// 库存状态颜色
export const INVENTORY_STATUS_COLORS = {
  [INVENTORY_STATUS.IN_STOCK]: '#52c41a',
  [INVENTORY_STATUS.LOW_STOCK]: '#faad14',
  [INVENTORY_STATUS.OUT_OF_STOCK]: '#f5222d'
}

// 库存类型
export const INVENTORY_TYPES = {
  PART: 'part',
  TOOL: 'tool',
  MATERIAL: 'material',
  OTHER: 'other'
}

// 库存类型名称
export const INVENTORY_TYPE_NAMES = {
  [INVENTORY_TYPES.PART]: '零件',
  [INVENTORY_TYPES.TOOL]: '工具',
  [INVENTORY_TYPES.MATERIAL]: '材料',
  [INVENTORY_TYPES.OTHER]: '其他'
}

// 库存类型颜色
export const INVENTORY_TYPE_COLORS = {
  [INVENTORY_TYPES.PART]: '#1890ff',
  [INVENTORY_TYPES.TOOL]: '#52c41a',
  [INVENTORY_TYPES.MATERIAL]: '#722ed1',
  [INVENTORY_TYPES.OTHER]: '#bfbfbf'
}

// 财务类型
export const FINANCE_TYPES = {
  INCOME: 'income',
  EXPENSE: 'expense'
}

// 财务类型名称
export const FINANCE_TYPE_NAMES = {
  [FINANCE_TYPES.INCOME]: '收入',
  [FINANCE_TYPES.EXPENSE]: '支出'
}

// 财务类型颜色
export const FINANCE_TYPE_COLORS = {
  [FINANCE_TYPES.INCOME]: '#52c41a',
  [FINANCE_TYPES.EXPENSE]: '#f5222d'
}

// 财务状态
export const FINANCE_STATUS = {
  PENDING: 'pending',
  PAID: 'paid',
  CANCELLED: 'cancelled'
}

// 财务状态名称
export const FINANCE_STATUS_NAMES = {
  [FINANCE_STATUS.PENDING]: '待支付',
  [FINANCE_STATUS.PAID]: '已支付',
  [FINANCE_STATUS.CANCELLED]: '已取消'
}

// 财务状态颜色
export const FINANCE_STATUS_COLORS = {
  [FINANCE_STATUS.PENDING]: '#faad14',
  [FINANCE_STATUS.PAID]: '#52c41a',
  [FINANCE_STATUS.CANCELLED]: '#f5222d'
}

// 支付方式
export const PAYMENT_METHODS = {
  CASH: 'cash',
  CARD: 'card',
  TRANSFER: 'transfer',
  OTHER: 'other'
}

// 支付方式名称
export const PAYMENT_METHOD_NAMES = {
  [PAYMENT_METHODS.CASH]: '现金',
  [PAYMENT_METHODS.CARD]: '刷卡',
  [PAYMENT_METHODS.TRANSFER]: '转账',
  [PAYMENT_METHODS.OTHER]: '其他'
}

// 支付方式颜色
export const PAYMENT_METHOD_COLORS = {
  [PAYMENT_METHODS.CASH]: '#1890ff',
  [PAYMENT_METHODS.CARD]: '#52c41a',
  [PAYMENT_METHODS.TRANSFER]: '#722ed1',
  [PAYMENT_METHODS.OTHER]: '#bfbfbf'
}

// 统计时间范围
export const STATISTICS_TIME_RANGES = {
  WEEK: 'week',
  MONTH: 'month',
  QUARTER: 'quarter',
  YEAR: 'year'
}

// 统计时间范围名称
export const STATISTICS_TIME_RANGE_NAMES = {
  [STATISTICS_TIME_RANGES.WEEK]: '本周',
  [STATISTICS_TIME_RANGES.MONTH]: '本月',
  [STATISTICS_TIME_RANGES.QUARTER]: '本季度',
  [STATISTICS_TIME_RANGES.YEAR]: '本年'
}

// 统计时间范围颜色
export const STATISTICS_TIME_RANGE_COLORS = {
  [STATISTICS_TIME_RANGES.WEEK]: '#1890ff',
  [STATISTICS_TIME_RANGES.MONTH]: '#52c41a',
  [STATISTICS_TIME_RANGES.QUARTER]: '#722ed1',
  [STATISTICS_TIME_RANGES.YEAR]: '#fa8c16'
}

// 统计类型
export const STATISTICS_TYPES = {
  REPAIR_ORDERS: 'repair_orders',
  INCOME: 'income',
  EXPENSE: 'expense',
  PROFIT: 'profit',
  CUSTOMERS: 'customers',
  VEHICLES: 'vehicles',
  INVENTORY: 'inventory'
}

// 统计类型名称
export const STATISTICS_TYPE_NAMES = {
  [STATISTICS_TYPES.REPAIR_ORDERS]: '维修订单',
  [STATISTICS_TYPES.INCOME]: '收入',
  [STATISTICS_TYPES.EXPENSE]: '支出',
  [STATISTICS_TYPES.PROFIT]: '利润',
  [STATISTICS_TYPES.CUSTOMERS]: '客户',
  [STATISTICS_TYPES.VEHICLES]: '车辆',
  [STATISTICS_TYPES.INVENTORY]: '库存'
}

// 统计类型颜色
export const STATISTICS_TYPE_COLORS = {
  [STATISTICS_TYPES.REPAIR_ORDERS]: '#1890ff',
  [STATISTICS_TYPES.INCOME]: '#52c41a',
  [STATISTICS_TYPES.EXPENSE]: '#f5222d',
  [STATISTICS_TYPES.PROFIT]: '#722ed1',
  [STATISTICS_TYPES.CUSTOMERS]: '#fa8c16',
  [STATISTICS_TYPES.VEHICLES]: '#13c2c2',
  [STATISTICS_TYPES.INVENTORY]: '#eb2f96'
}

// 统计图表类型
export const STATISTICS_CHART_TYPES = {
  LINE: 'line',
  BAR: 'bar',
  PIE: 'pie',
  RADAR: 'radar',
  SCATTER: 'scatter',
  MAP: 'map'
}

// 统计图表类型名称
export const STATISTICS_CHART_TYPE_NAMES = {
  [STATISTICS_CHART_TYPES.LINE]: '折线图',
  [STATISTICS_CHART_TYPES.BAR]: '柱状图',
  [STATISTICS_CHART_TYPES.PIE]: '饼图',
  [STATISTICS_CHART_TYPES.RADAR]: '雷达图',
  [STATISTICS_CHART_TYPES.SCATTER]: '散点图',
  [STATISTICS_CHART_TYPES.MAP]: '地图'
}

// 统计图表类型颜色
export const STATISTICS_CHART_TYPE_COLORS = {
  [STATISTICS_CHART_TYPES.LINE]: '#1890ff',
  [STATISTICS_CHART_TYPES.BAR]: '#52c41a',
  [STATISTICS_CHART_TYPES.PIE]: '#722ed1',
  [STATISTICS_CHART_TYPES.RADAR]: '#fa8c16',
  [STATISTICS_CHART_TYPES.SCATTER]: '#13c2c2',
  [STATISTICS_CHART_TYPES.MAP]: '#eb2f96'
}

// 导出所有常量
export default {
  SYSTEM,
  ROLES,
  ROLE_NAMES,
  ROLE_PERMISSIONS,
  REPAIR_ORDER_STATUS,
  REPAIR_ORDER_STATUS_NAMES,
  REPAIR_ORDER_STATUS_COLORS,
  REPAIR_TYPES,
  REPAIR_TYPE_NAMES,
  REPAIR_TYPE_COLORS,
  VEHICLE_TYPES,
  VEHICLE_TYPE_NAMES,
  VEHICLE_TYPE_COLORS,
  INVENTORY_STATUS,
  INVENTORY_STATUS_NAMES,
  INVENTORY_STATUS_COLORS,
  INVENTORY_TYPES,
  INVENTORY_TYPE_NAMES,
  INVENTORY_TYPE_COLORS,
  FINANCE_TYPES,
  FINANCE_TYPE_NAMES,
  FINANCE_TYPE_COLORS,
  FINANCE_STATUS,
  FINANCE_STATUS_NAMES,
  FINANCE_STATUS_COLORS,
  PAYMENT_METHODS,
  PAYMENT_METHOD_NAMES,
  PAYMENT_METHOD_COLORS,
  STATISTICS_TIME_RANGES,
  STATISTICS_TIME_RANGE_NAMES,
  STATISTICS_TIME_RANGE_COLORS,
  STATISTICS_TYPES,
  STATISTICS_TYPE_NAMES,
  STATISTICS_TYPE_COLORS,
  STATISTICS_CHART_TYPES,
  STATISTICS_CHART_TYPE_NAMES,
  STATISTICS_CHART_TYPE_COLORS
} 