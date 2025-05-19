import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: '/api',
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response?.status === 401) {
      // 处理未授权错误
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// 认证相关接口
export const auth = {
  login: (data) => api.post('/auth/login', data),
  logout: () => api.post('/auth/logout'),
  getProfile: () => api.get('/auth/profile')
}

// 维修订单相关接口
export const repairOrders = {
  getList: (params) => api.get('/repair-orders', { params }),
  getById: (id) => api.get(`/repair-orders/${id}`),
  create: (data) => api.post('/repair-orders', data),
  update: (id, data) => api.put(`/repair-orders/${id}`, data),
  delete: (id) => api.delete(`/repair-orders/${id}`),
  updateStatus: (id, status) => api.patch(`/repair-orders/${id}/status`, { status })
}

// 车辆管理相关接口
export const vehicles = {
  getList: (params) => api.get('/vehicles', { params }),
  getById: (id) => api.get(`/vehicles/${id}`),
  create: (data) => api.post('/vehicles', data),
  update: (id, data) => api.put(`/vehicles/${id}`, data),
  delete: (id) => api.delete(`/vehicles/${id}`),
  getRepairHistory: (id) => api.get(`/vehicles/${id}/repair-history`)
}

// 库存管理相关接口
export const inventory = {
  getList: (params) => api.get('/inventory', { params }),
  getById: (id) => api.get(`/inventory/${id}`),
  create: (data) => api.post('/inventory', data),
  update: (id, data) => api.put(`/inventory/${id}`, data),
  delete: (id) => api.delete(`/inventory/${id}`),
  updateStock: (id, quantity) => api.patch(`/inventory/${id}/stock`, { quantity }),
  getStockHistory: (id) => api.get(`/inventory/${id}/stock-history`)
}

// 财务管理相关接口
export const finance = {
  getTransactions: (params) => api.get('/transactions', { params }),
  getTransactionById: (id) => api.get(`/transactions/${id}`),
  createTransaction: (data) => api.post('/transactions', data),
  updateTransaction: (id, data) => api.put(`/transactions/${id}`, data),
  deleteTransaction: (id) => api.delete(`/transactions/${id}`),
  getStatistics: (params) => api.get('/finance/statistics', { params }),
  exportData: (params) => api.get('/finance/export', { 
    params,
    responseType: 'blob'
  })
}

// 统计分析相关接口
export const statistics = {
  getOverview: (params) => api.get('/statistics/overview', { params }),
  getRepairTypeDistribution: (params) => api.get('/statistics/repair-type-distribution', { params }),
  getIncomeTrend: (params) => api.get('/statistics/income-trend', { params }),
  getRepairTimeDistribution: (params) => api.get('/statistics/repair-time-distribution', { params }),
  getCustomerSourceDistribution: (params) => api.get('/statistics/customer-source-distribution', { params }),
  getDetailedData: (params) => api.get('/statistics/detailed-data', { params }),
  exportReport: (params) => api.get('/statistics/export-report', {
    params,
    responseType: 'blob'
  })
}

// 系统管理相关接口
export const system = {
  getUsers: (params) => api.get('/system/users', { params }),
  createUser: (data) => api.post('/system/users', data),
  updateUser: (id, data) => api.put(`/system/users/${id}`, data),
  deleteUser: (id) => api.delete(`/system/users/${id}`),
  getRoles: () => api.get('/system/roles'),
  getPermissions: () => api.get('/system/permissions'),
  getSystemLogs: (params) => api.get('/system/logs', { params })
}

export default {
  auth,
  repairOrders,
  vehicles,
  inventory,
  finance,
  statistics,
  system
} 