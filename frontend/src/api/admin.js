import http from './http'


export const getProfile = () => http.get('/admin/profile')


// 管理员订单管理
export const getOrders = params => http.get('/admin/repair-orders', { params })
export const getOrderDetail = id => http.get(`/admin/repair-orders/${id}`)
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
// export const getUserDetail = id => http.get(`/admin/account/customers/${id}`)
// export const createUser = data => http.post('/admin/account/customers', data)
// export const updateUser = (id, data) => http.put(`/admin/account/customers/${id}`, data)
// export const deleteUser = id => http.delete(`/admin/account/customers/${id}`)

// 管理员维修人员管理
export const getRepairmen = params => http.get('/admin/account/repairmen', { params })
// export const getRepairmanDetail = id => http.get(`/admin/account/repairmen/${id}`)
// export const createRepairman = data => http.post('/admin/account/repairmen', data)
// export const updateRepairman = (id, data) => http.put(`/admin/account/repairmen/${id}`, data)
// export const deleteRepairman = id => http.delete(`/admin/account/repairmen/${id}`)

// 管理员库存管理

// 基本设置
export const getBasicSettings = () => http.get('/admin/settings/basic')
export const updateBasicSettings = data => http.put('/admin/settings/basic', data)

// 维修设置
export const getRepairSettings = () => http.get('/admin/settings/repair')
export const updateRepairSettings = data => http.put('/admin/settings/repair', data)

// 库存设置
export const getInventorySettings = () => http.get('/admin/settings/inventory')
export const updateInventorySettings = data => http.put('/admin/settings/inventory', data)

// 通知设置
export const getNotificationSettings = () => http.get('/admin/settings/notification')
export const updateNotificationSettings = data => http.put('/admin/settings/notification', data) 