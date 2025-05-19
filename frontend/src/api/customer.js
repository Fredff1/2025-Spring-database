import http from './http'

export const login = data => http.post('/customer/login', data)
export const register = data => http.post('/customer/register', data)
export const getProfile = () => http.get('/customer/profile')
export const updateProfile = data => http.put('/customer/profile', data)

// 订单管理
export const getOrders = params => http.get('/customer/orders', { params })
export const getOrderDetail = id => http.get(`/customer/orders/${id}`)
export const createOrder = data => http.post('/customer/orders', data)
export const cancelOrder = id => http.post(`/customer/orders/${id}/cancel`)
export const rateOrder = (id, data) => http.post(`/customer/orders/${id}/rate`, data)

// 历史记录
export const getHistory = params => http.get('/customer/history', { params }) 