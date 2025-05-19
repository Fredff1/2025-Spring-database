import http from './http';

export const register = data => http.post('/users/register', data);
export const login    = data => http.post('/users/login', data);
export const profile  = ()   => http.get('/users/profile');
export const updateProfile = data => http.put('/users/profile', data);
export const listUserOrders = () => http.get('/users/orders');
export const submitRepair   = data => http.post('/users/repairs', data);
export const sendFeedback   = data => http.post('/users/feedback', data);