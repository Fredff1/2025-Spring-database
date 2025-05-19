import http from './http'

// 车辆管理
export const getList = params => http.get('/vehicles', { params })
export const getDetail = id => http.get(`/vehicles/${id}`)
export const create = data => http.post('/vehicles', data)
export const update = (id, data) => http.put(`/vehicles/${id}`, data)
export const remove = id => http.delete(`/vehicles/${id}`)

// 车辆维修记录
export const getRepairHistory = id => http.get(`/vehicles/${id}/repair-history`) 