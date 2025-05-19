import http from './http'

// 订单管理
export const getList = params => http.get('/orders', { params })
export const getDetail = id => http.get(`/orders/${id}`)
export const create = data => http.post('/orders', data)
export const cancel = id => http.post(`/orders/${id}/cancel`)
export const pay = id => http.post(`/orders/${id}/pay`)
export const review = (id, data) => http.post(`/orders/${id}/review`, data)

// 统计数据
export const getStats = () => http.get('/orders/stats')

// 维修人员相关
export const startRepair = id => http.post(`/orders/${id}/start-repair`)
export const completeRepair = (id, data) => http.post(`/orders/${id}/complete-repair`, data)

// 管理员相关
export const assignRepairman = (id, repairmanId) => http.post(`/orders/${id}/assign`, { repairmanId })
export const complete = id => http.post(`/orders/${id}/complete`) 