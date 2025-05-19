import axios from 'axios'
import { handleError } from './errors'
import { localStorage } from './storage'
import { STORAGE_KEYS } from './storage'

// 创建axios实例
const http = axios.create({
  baseURL: process.env.VUE_APP_API_URL || '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
http.interceptors.request.use(
  config => {
    // 从本地存储获取token
    const token = localStorage.get(STORAGE_KEYS.TOKEN)
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(handleError(error))
  }
)

// 响应拦截器
http.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    return Promise.reject(handleError(error))
  }
)

// 请求方法
export const request = {
  // GET请求
  get(url, params = {}, config = {}) {
    return http.get(url, { params, ...config })
  },

  // POST请求
  post(url, data = {}, config = {}) {
    return http.post(url, data, config)
  },

  // PUT请求
  put(url, data = {}, config = {}) {
    return http.put(url, data, config)
  },

  // DELETE请求
  delete(url, params = {}, config = {}) {
    return http.delete(url, { params, ...config })
  },

  // PATCH请求
  patch(url, data = {}, config = {}) {
    return http.patch(url, data, config)
  },

  // 上传文件
  upload(url, file, onProgress = null, config = {}) {
    const formData = new FormData()
    formData.append('file', file)

    return http.post(url, formData, {
      ...config,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      onUploadProgress: onProgress
    })
  },

  // 下载文件
  download(url, params = {}, config = {}) {
    return http.get(url, {
      ...config,
      params,
      responseType: 'blob'
    })
  },

  // 并发请求
  all(requests) {
    return axios.all(requests)
  },

  // 并发请求（带进度）
  allWithProgress(requests, onProgress = null) {
    return axios.all(requests).then(
      axios.spread((...responses) => {
        if (onProgress) {
          onProgress(1)
        }
        return responses
      })
    )
  },

  // 取消请求
  cancel(token) {
    if (token) {
      token.cancel('请求已取消')
    }
  },

  // 创建取消令牌
  createCancelToken() {
    return axios.CancelToken.source()
  }
}

// 导出所有HTTP请求相关的内容
export default {
  http,
  request
} 