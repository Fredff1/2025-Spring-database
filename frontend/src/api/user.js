import http from './http';

// 用户认证
export const getProfile = () => http.get('/user/profile');

// 用户订单管理
export const getOrders = params => http.get('/user/orders', { params });
export const getOrderDetail = id => http.get(`/user/orders/${id}`);
export const createOrder = data => http.post('/user/orders', data);
export const cancelOrder = id => http.post(`/user/orders/${id}/cancel`);
export const payOrder = id => http.post(`/user/orders/${id}/pay`);
export const reviewOrder = (id, data) => http.post(`/user/orders/${id}/review`, data);

// 用户车辆管理
export const getVehicles = params => http.get('/user/vehicles', { params });
export const getVehicleDetail = id => http.get(`/user/vehicles/${id}`);
export const addVehicle = data => http.post('/user/vehicles', data);
export const updateVehicle = (id, data) => http.put(`/user/vehicles/${id}`, data);
export const deleteVehicle = id => http.delete(`/user/vehicles/${id}`);

export const profile = () => http.get('/users/profile');
export const listUserOrders = () => http.get('/users/orders');
export const submitRepair = data => http.post('/users/repairs', data);
export const sendFeedback = data => http.post('/users/feedback', data);