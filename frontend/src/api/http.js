import axios from 'axios';
import { getToken, removeToken } from '@/utils/auth';
import { ElMessage } from 'element-plus';
import router from '@/router';

const http = axios.create({
  baseURL: '/api',
  timeout: 15000
});

http.interceptors.request.use(
  config => {
    const token = getToken();
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

http.interceptors.response.use(
  response => {
    const res = response.data;
    
    return res.data || res;
  },
  error => {
    console.error('Response error:', error);
    
    if (error.response?.status === 401) {
      if (error.config.headers['Authorization']) {
        removeToken();
        ElMessage.error('登录已过期，请重新登录');
        router.push('/login');
      } else {
        ElMessage.error('请先登录');
      }
    } else {
      ElMessage.error(error.response?.data?.message || '网络错误');
    }
    
    return Promise.reject(error);
  }
);

export default http;