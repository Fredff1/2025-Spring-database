import http from './http';

export const register = data => http.post('/auth/register', data);
export const login    = data => http.post('/auth/login', data);
export const profile  = ()   => http.get('/users/profile');
export const listUserOrders = () => http.get('/users/orders');
export const submitRepair   = data => http.post('/users/repairs', data);
export const sendFeedback   = data => http.post('/users/feedback', data);