/**
 * 获取当前用户的token
 * @returns {string|null} token字符串或null
 */
export const getToken = () => {
  return localStorage.getItem('token')
}

/**
 * 设置用户token
 * @param {string} token JWT token
 */
export const setToken = (token) => {
  localStorage.setItem('token', token)
}

/**
 * 移除用户token
 */
export const removeToken = () => {
  localStorage.removeItem('token')
}

/**
 * 登出
 * @returns {Promise<void>}
 */
export const logout = async () => {
  const token = getToken()
  if (token) {
    try {
      await fetch('/api/auth/logout', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ token })
      })
    } catch (error) {
      console.error('登出请求失败:', error)
    }
  }
  removeToken()
}

/**
 * 检查用户是否已登录
 * @returns {boolean}
 */
export const isAuthenticated = () => {
  return !!getToken()
}

/**
 * 获取请求头中的认证信息
 * @returns {Object} 包含Authorization头的对象
 */
export const getAuthHeader = () => {
  const token = getToken()
  return token ? { Authorization: `Bearer ${token}` } : {}
} 