import axios from 'axios';
import { useAuthStore } from '@/store/modules/user';
import { ElMessage } from 'element-plus';

const http = axios.create({ baseURL: '/api', timeout: 8000 });

http.interceptors.request.use(config => {
  const token = useAuthStore().token;
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

http.interceptors.response.use(
  res => res.data,
  err => {
    ElMessage.error(err.response?.data?.message || '网络错误');
    return Promise.reject(err);
  }
);

export default http;