import http from './http'

// 维修人员认证
export const updateProfile = data => http.put('/repairman/profile', data)


// 维修人员订单管理
export const getOrders = params => http.get('/repairman/orders', { params })
export const getOrderDetail = id => http.get(`/repairman/orders/${id}`)
export const startRepair = id => http.post(`/repairman/orders/${id}/start`)
export const completeRepair = (id, data) => http.post(`/repairman/orders/${id}/complete`, data)

// 维修人员任务分配
export const getAssignments = params => http.get('/repairman/assignments', { params })
export const acceptAssignment = assignmentId => http.post(`/repairman/assignments/${assignmentId}/accept`)
export const rejectAssignment = assignmentId => http.post(`/repairman/assignments/${assignmentId}/reject`)

// 维修人员工作统计
export const getOverview = () => http.get('/repairman/overview')
export const getPendingOrders = () => http.get('/repairman/pending-orders')
export const getStatistics = params => http.get('/repairman/statistics', { params })
export const getHistory = params => http.get('/repairman/history', { params })
export const getIncome = params => http.get('/repairman/income', { params })
export const exportIncome = params => http.get('/repairman/income/export', { 
  params,
  responseType: 'blob'
}) 