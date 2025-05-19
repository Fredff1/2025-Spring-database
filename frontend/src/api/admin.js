import http from './http'

export const login = data => http.post('/admin/login', data)
export const getProfile = () => http.get('/admin/profile')
export const updateProfile = data => http.put('/admin/profile', data)

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