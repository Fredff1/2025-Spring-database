import http from './http'

// 管理员认证
export const getProfile = () => http.get('/admin/profile')


// 管理员订单管理
export const getOrders = params => http.get('/admin/orders', { params })
export const getOrderDetail = id => http.get(`/admin/orders/${id}`)
export const assignRepairman = (id, repairmanId) => http.post(`/admin/orders/${id}/assign`, { repairmanId })
export const completeOrder = id => http.post(`/admin/orders/${id}/complete`)
export const getOrderStats = () => http.get('/admin/orders/stats')

// 管理员车辆管理
export const getVehicles = params => http.get('/admin/vehicles', { params })
export const getVehicleDetail = id => http.get(`/admin/vehicles/${id}`)
export const updateVehicle = (id, data) => http.put(`/admin/vehicles/${id}`, data)
export const deleteVehicle = id => http.delete(`/admin/vehicles/${id}`)

// 管理员用户管理
export const getUsers = params => http.get('/admin/users', { params })
export const getUserDetail = id => http.get(`/admin/users/${id}`)
export const createUser = data => http.post('/admin/users', data)
export const updateUser = (id, data) => http.put(`/admin/users/${id}`, data)
export const deleteUser = id => http.delete(`/admin/users/${id}`)

// 管理员维修人员管理
export const getRepairmen = params => http.get('/admin/repairmen', { params })
export const getRepairmanDetail = id => http.get(`/admin/repairmen/${id}`)
export const createRepairman = data => http.post('/admin/repairmen', data)
export const updateRepairman = (id, data) => http.put(`/admin/repairmen/${id}`, data)
export const deleteRepairman = id => http.delete(`/admin/repairmen/${id}`)

// 管理员库存管理
export const getInventory = params => http.get('/admin/inventory', { params })
export const getInventoryDetail = id => http.get(`/admin/inventory/${id}`)
export const addInventory = data => http.post('/admin/inventory', data)
export const updateInventory = (id, data) => http.put(`/admin/inventory/${id}`, data)
export const deleteInventory = id => http.delete(`/admin/inventory/${id}`)

// 管理员财务管理
export const getFinance = params => http.get('/admin/finance', { params })
export const getFinanceStats = () => http.get('/admin/finance/stats')
export const exportFinance = params => http.get('/admin/finance/export', { 
  params,
  responseType: 'blob'
})

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