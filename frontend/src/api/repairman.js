import http from './http'

export const login = data => http.post('/auth/login', data)
export const getProfile = () => http.get('/repairman/profile')


// 订单管理
export const getOrders = params => http.get('/repairman/orders', { params })
export const getOrderDetail = id => http.get(`/repairman/orders/${id}`)
export const startRepair = id => http.post(`/repairman/orders/${id}/start`)
export const completeRepair = (id, data) => http.post(`/repairman/orders/${id}/complete`, data)

// 历史记录
export const getHistory = params => http.get('/repairman/history', { params })

// 收入统计
export const getIncome = params => http.get('/repairman/income', { params })
export const exportIncome = params => http.get('/repairman/income/export', { 
  params,
  responseType: 'blob'
}) 