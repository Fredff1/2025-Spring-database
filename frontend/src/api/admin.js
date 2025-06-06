import http from './http'


export const getProfile = () => http.get('/admin/profile')
export const getOverview = () => http.get('/admin/overview')


// 管理员订单管理
export const getOrders = params => http.get('/admin/repair-orders', { params })
export const getOrderDetail = id => http.get(`/admin/repair-orders/${id}`)
export const getUnfinishedOrders = () => http.get('/admin/repair-orders/unfinished')
export const assignRepairman = (id, repairmanId) => http.post(`/admin/repair-orders/${id}/assign`, { repairmanId })
export const getOrderStats = () => http.get('/admin/repair-orders/stats')

// 管理员车辆管理
export const getVehicles = params => http.get('/admin/vehicles', { params })
export const getVehicleDetail = id => http.get(`/admin/vehicles/${id}`)
export const updateVehicle = (id, data) => http.put(`/admin/vehicles/${id}`, data)
export const deleteVehicle = id => http.delete(`/admin/vehicles/${id}`)

export const getAssignments = params => http.get('/admin/assignments', { params })
export const getRepairRecords = orderId => http.get(`/admin/repair-orders/${orderId}/records`);
export const getMaterialUsages = orderId => http.get(`/admin/repair-orders/${orderId}/materials`);
export const getFeedbackList = orderId => http.get(`/admin/repair-orders/${orderId}/feedback`);
export const getIncomeStats = params => http.get('/admin/income', { params })
// 管理员用户管理
export const getUsers = params => http.get('/admin/account/customers', { params })


// 管理员维修人员管理
export const getRepairmen = params => http.get('/admin/account/repairmen', { params })
export const getSystemStatus = () => http.get('/admin/system/status')

// 统计相关
export const getNegativeFeedbackStats = params => http.get('/admin/stats/feedback/negative', { params })
export const getCostProportionStats = params => http.get('/admin/stats/cost/proportion', { params })
export const getVehicleOrderStats = params => http.get('/admin/stats/vehicle/order-stat', { params })
export const getVehicleFaultTypeStats = params => http.get('/admin/stats/vehicle/fault-type', { params })
export const getOrderProcessStats = params => http.get('/admin/stats/repair-orders/process', { params })
export const getOrderMismatchStats = () => http.get('/admin/stats/repair-orders/mismatch')
export const getUnfinishedOrderFaultTypeStats = () => http.get('/admin/stats/repair-orders/unfinished/fault-type')
export const getUnfinishedOrderRepairmanStats = () => http.get('/admin/stats/repair-orders/unfinished/repairman')
export const getUnfinishedOrderVehicleStats = () => http.get('/admin/stats/repair-orders/unfinished/vehicle') 