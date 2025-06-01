import http from './http';

// 用户认证
export const getProfile = () => http.get('/user/profile');
export const getStats = () => http.get('/user/status');

// 用户订单管理
export const getRepairOrders = params => http.get('/user/repair-orders', { params });
export const getOrderDetail = id => http.get(`/user/repair-orders/${id}`);
export const createOrder = data => http.post('/user/repair-orders', data);
export const cancelOrder = id => http.post(`/user/repair-orders/${id}/cancel`);
export const payOrder = id => http.post(`/user/repair-orders/${id}/pay`);

// 批量创建订单
export const createOrderList = data => http.post('/user/repair-orders/batch', data);

// 维修记录
export const getRepairRecords = orderId => http.get(`/user/repair-orders/${orderId}/records`);
export const getMaterialUsages = orderId => http.get(`/user/repair-orders/${orderId}/materials`);
export const getFeedbackList = orderId => http.get(`/user/repair-orders/${orderId}/feedback`);
export const submitFeedback = data => http.post('/user/repair-orders/feedback', data);

// 用户车辆管理
export const getVehicles = params => http.get('/user/vehicles', { params });
export const getVehicleDetail = id => http.get(`/user/vehicles/${id}`);
export const addVehicle = data => http.post('/user/vehicles', data);
export const updateVehicle = (id, data) => http.put(`/user/vehicles/${id}`, data);
export const deleteVehicle = id => http.delete(`/user/vehicles/${id}`);




