// 存储类型
export const STORAGE_TYPES = {
  LOCAL: 'local',
  SESSION: 'session',
  COOKIE: 'cookie'
}

// 存储类型名称
export const STORAGE_TYPE_NAMES = {
  [STORAGE_TYPES.LOCAL]: '本地存储',
  [STORAGE_TYPES.SESSION]: '会话存储',
  [STORAGE_TYPES.COOKIE]: 'Cookie存储'
}

// 存储类型颜色
export const STORAGE_TYPE_COLORS = {
  [STORAGE_TYPES.LOCAL]: '#1890ff',
  [STORAGE_TYPES.SESSION]: '#52c41a',
  [STORAGE_TYPES.COOKIE]: '#722ed1'
}

// 存储键前缀
export const STORAGE_KEY_PREFIX = {
  [STORAGE_TYPES.LOCAL]: 'local_',
  [STORAGE_TYPES.SESSION]: 'session_',
  [STORAGE_TYPES.COOKIE]: 'cookie_'
}

// 存储键
export const STORAGE_KEYS = {
  // 用户相关
  USER: 'user',
  TOKEN: 'token',
  REFRESH_TOKEN: 'refresh_token',
  // 系统相关
  THEME: 'theme',
  LANGUAGE: 'language',
  SETTINGS: 'settings',
  // 业务相关
  CART: 'cart',
  FAVORITES: 'favorites',
  HISTORY: 'history'
}

// 存储键名称
export const STORAGE_KEY_NAMES = {
  [STORAGE_KEYS.USER]: '用户信息',
  [STORAGE_KEYS.TOKEN]: '访问令牌',
  [STORAGE_KEYS.REFRESH_TOKEN]: '刷新令牌',
  [STORAGE_KEYS.THEME]: '主题设置',
  [STORAGE_KEYS.LANGUAGE]: '语言设置',
  [STORAGE_KEYS.SETTINGS]: '系统设置',
  [STORAGE_KEYS.CART]: '购物车',
  [STORAGE_KEYS.FAVORITES]: '收藏夹',
  [STORAGE_KEYS.HISTORY]: '历史记录'
}

// 存储键颜色
export const STORAGE_KEY_COLORS = {
  [STORAGE_KEYS.USER]: '#1890ff',
  [STORAGE_KEYS.TOKEN]: '#52c41a',
  [STORAGE_KEYS.REFRESH_TOKEN]: '#722ed1',
  [STORAGE_KEYS.THEME]: '#fa8c16',
  [STORAGE_KEYS.LANGUAGE]: '#13c2c2',
  [STORAGE_KEYS.SETTINGS]: '#eb2f96',
  [STORAGE_KEYS.CART]: '#1890ff',
  [STORAGE_KEYS.FAVORITES]: '#52c41a',
  [STORAGE_KEYS.HISTORY]: '#722ed1'
}

// 存储类
export class Storage {
  constructor(type = STORAGE_TYPES.LOCAL) {
    this.type = type
    this.prefix = STORAGE_KEY_PREFIX[type]
    this.storage = type === STORAGE_TYPES.LOCAL ? localStorage : sessionStorage
  }

  // 获取存储键
  getKey(key) {
    return `${this.prefix}${key}`
  }

  // 设置值
  set(key, value) {
    try {
      const serializedValue = JSON.stringify(value)
      this.storage.setItem(this.getKey(key), serializedValue)
      return true
    } catch (error) {
      console.error('Storage set error:', error)
      return false
    }
  }

  // 获取值
  get(key, defaultValue = null) {
    try {
      const serializedValue = this.storage.getItem(this.getKey(key))
      if (serializedValue === null) {
        return defaultValue
      }
      return JSON.parse(serializedValue)
    } catch (error) {
      console.error('Storage get error:', error)
      return defaultValue
    }
  }

  // 删除值
  remove(key) {
    try {
      this.storage.removeItem(this.getKey(key))
      return true
    } catch (error) {
      console.error('Storage remove error:', error)
      return false
    }
  }

  // 清空所有值
  clear() {
    try {
      this.storage.clear()
      return true
    } catch (error) {
      console.error('Storage clear error:', error)
      return false
    }
  }

  // 获取所有键
  keys() {
    try {
      return Object.keys(this.storage)
        .filter(key => key.startsWith(this.prefix))
        .map(key => key.slice(this.prefix.length))
    } catch (error) {
      console.error('Storage keys error:', error)
      return []
    }
  }

  // 获取所有值
  values() {
    try {
      return this.keys().map(key => this.get(key))
    } catch (error) {
      console.error('Storage values error:', error)
      return []
    }
  }

  // 获取所有键值对
  entries() {
    try {
      return this.keys().map(key => [key, this.get(key)])
    } catch (error) {
      console.error('Storage entries error:', error)
      return []
    }
  }

  // 获取存储大小
  size() {
    try {
      return this.keys().length
    } catch (error) {
      console.error('Storage size error:', error)
      return 0
    }
  }

  // 检查键是否存在
  has(key) {
    try {
      return this.get(key) !== null
    } catch (error) {
      console.error('Storage has error:', error)
      return false
    }
  }
}

// 创建本地存储实例
export const localStorage = new Storage(STORAGE_TYPES.LOCAL)

// 创建会话存储实例
export const sessionStorage = new Storage(STORAGE_TYPES.SESSION)

// Cookie存储类
export class CookieStorage {
  constructor() {
    this.prefix = STORAGE_KEY_PREFIX[STORAGE_TYPES.COOKIE]
  }

  // 获取存储键
  getKey(key) {
    return `${this.prefix}${key}`
  }

  // 设置值
  set(key, value, options = {}) {
    try {
      const serializedValue = JSON.stringify(value)
      const cookieOptions = {
        path: '/',
        ...options
      }
      let cookie = `${this.getKey(key)}=${encodeURIComponent(serializedValue)}`
      for (const [optionKey, optionValue] of Object.entries(cookieOptions)) {
        cookie += `; ${optionKey}=${optionValue}`
      }
      document.cookie = cookie
      return true
    } catch (error) {
      console.error('CookieStorage set error:', error)
      return false
    }
  }

  // 获取值
  get(key, defaultValue = null) {
    try {
      const cookies = document.cookie.split(';')
      const cookie = cookies.find(c => c.trim().startsWith(this.getKey(key)))
      if (!cookie) {
        return defaultValue
      }
      const serializedValue = cookie.split('=')[1]
      return JSON.parse(decodeURIComponent(serializedValue))
    } catch (error) {
      console.error('CookieStorage get error:', error)
      return defaultValue
    }
  }

  // 删除值
  remove(key) {
    try {
      document.cookie = `${this.getKey(key)}=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/`
      return true
    } catch (error) {
      console.error('CookieStorage remove error:', error)
      return false
    }
  }

  // 清空所有值
  clear() {
    try {
      const cookies = document.cookie.split(';')
      for (const cookie of cookies) {
        const key = cookie.split('=')[0].trim()
        if (key.startsWith(this.prefix)) {
          this.remove(key.slice(this.prefix.length))
        }
      }
      return true
    } catch (error) {
      console.error('CookieStorage clear error:', error)
      return false
    }
  }

  // 获取所有键
  keys() {
    try {
      const cookies = document.cookie.split(';')
      return cookies
        .map(cookie => cookie.split('=')[0].trim())
        .filter(key => key.startsWith(this.prefix))
        .map(key => key.slice(this.prefix.length))
    } catch (error) {
      console.error('CookieStorage keys error:', error)
      return []
    }
  }

  // 获取所有值
  values() {
    try {
      return this.keys().map(key => this.get(key))
    } catch (error) {
      console.error('CookieStorage values error:', error)
      return []
    }
  }

  // 获取所有键值对
  entries() {
    try {
      return this.keys().map(key => [key, this.get(key)])
    } catch (error) {
      console.error('CookieStorage entries error:', error)
      return []
    }
  }

  // 获取存储大小
  size() {
    try {
      return this.keys().length
    } catch (error) {
      console.error('CookieStorage size error:', error)
      return 0
    }
  }

  // 检查键是否存在
  has(key) {
    try {
      return this.get(key) !== null
    } catch (error) {
      console.error('CookieStorage has error:', error)
      return false
    }
  }
}

// 创建Cookie存储实例
export const cookieStorage = new CookieStorage()

// 导出所有存储相关的内容
export default {
  STORAGE_TYPES,
  STORAGE_TYPE_NAMES,
  STORAGE_TYPE_COLORS,
  STORAGE_KEY_PREFIX,
  STORAGE_KEYS,
  STORAGE_KEY_NAMES,
  STORAGE_KEY_COLORS,
  Storage,
  localStorage,
  sessionStorage,
  CookieStorage,
  cookieStorage
} 