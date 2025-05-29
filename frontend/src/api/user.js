import http from './http';

// 用户认证
export const getProfile = () => http.get('/user/profile');

// 用户订单管理
export const getRepairOrders = params => http.get('/user/repair-orders', { params });
export const getOrderDetail = id => http.get(`/user/repair-orders/${id}`);
export const createOrder = data => http.post('/user/repair-orders', data);
export const cancelOrder = id => http.post(`/user/repair-orders/${id}/cancel`);
export const payOrder = id => http.post(`/user/repair-orders/${id}/pay`);
export const urgeOrder = id => http.post(`/user/repair-orders/${id}/urge`);

// 维修记录
export const getRepairRecords = orderId => http.get(`/user/repair-orders/${orderId}/records`);

// 材料使用记录
export const getMaterialUsages = orderId => http.get(`/user/repair-orders/${orderId}/materials`);

// 维修反馈
export const getFeedbackList = orderId => http.get(`/user/repair-orders/${orderId}/feedback`);
export const submitFeedback = data => http.post('/user/repair-feedback', data);

// 用户车辆管理
export const getVehicles = params => http.get('/user/vehicles', { params });
export const getVehicleDetail = id => http.get(`/user/vehicles/${id}`);
export const addVehicle = data => http.post('/user/vehicles', data);
export const updateVehicle = (id, data) => http.put(`/user/vehicles/${id}`, data);
export const deleteVehicle = id => http.delete(`/user/vehicles/${id}`);

export const profile = () => http.get('/users/profile');
export const getStats = () => http.get('/user/status');
export const listUserOrders = () => http.get('/users/orders');
export const submitRepair = data => http.post('/users/repairs', data);
export const sendFeedback = data => http.post('/users/feedback', data);

// 维修记录相关
export const getRepairHistory = params => http.get('/user/repair-records', { params });
export const getRepairDetail = id => http.get(`/user/repair-records/${id}`);
export const rateRepair = (id, data) => http.post(`/user/repair-records/${id}/rate`, data);