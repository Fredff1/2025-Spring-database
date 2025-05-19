// 错误类型
export const ERROR_TYPES = {
  NETWORK: 'network',
  AUTH: 'auth',
  VALIDATION: 'validation',
  BUSINESS: 'business',
  SYSTEM: 'system'
}

// 错误类型名称
export const ERROR_TYPE_NAMES = {
  [ERROR_TYPES.NETWORK]: '网络错误',
  [ERROR_TYPES.AUTH]: '认证错误',
  [ERROR_TYPES.VALIDATION]: '验证错误',
  [ERROR_TYPES.BUSINESS]: '业务错误',
  [ERROR_TYPES.SYSTEM]: '系统错误'
}

// 错误类型颜色
export const ERROR_TYPE_COLORS = {
  [ERROR_TYPES.NETWORK]: '#1890ff',
  [ERROR_TYPES.AUTH]: '#faad14',
  [ERROR_TYPES.VALIDATION]: '#f5222d',
  [ERROR_TYPES.BUSINESS]: '#722ed1',
  [ERROR_TYPES.SYSTEM]: '#eb2f96'
}

// 错误代码
export const ERROR_CODES = {
  // 网络错误
  NETWORK_ERROR: 'NETWORK_ERROR',
  TIMEOUT: 'TIMEOUT',
  SERVER_ERROR: 'SERVER_ERROR',
  // 认证错误
  UNAUTHORIZED: 'UNAUTHORIZED',
  FORBIDDEN: 'FORBIDDEN',
  TOKEN_EXPIRED: 'TOKEN_EXPIRED',
  // 验证错误
  INVALID_PARAMS: 'INVALID_PARAMS',
  MISSING_PARAMS: 'MISSING_PARAMS',
  // 业务错误
  BUSINESS_ERROR: 'BUSINESS_ERROR',
  DATA_NOT_FOUND: 'DATA_NOT_FOUND',
  DATA_ALREADY_EXISTS: 'DATA_ALREADY_EXISTS',
  // 系统错误
  SYSTEM_ERROR: 'SYSTEM_ERROR',
  UNKNOWN_ERROR: 'UNKNOWN_ERROR'
}

// 错误代码名称
export const ERROR_CODE_NAMES = {
  [ERROR_CODES.NETWORK_ERROR]: '网络连接错误',
  [ERROR_CODES.TIMEOUT]: '请求超时',
  [ERROR_CODES.SERVER_ERROR]: '服务器错误',
  [ERROR_CODES.UNAUTHORIZED]: '未授权',
  [ERROR_CODES.FORBIDDEN]: '禁止访问',
  [ERROR_CODES.TOKEN_EXPIRED]: '令牌已过期',
  [ERROR_CODES.INVALID_PARAMS]: '无效的参数',
  [ERROR_CODES.MISSING_PARAMS]: '缺少参数',
  [ERROR_CODES.BUSINESS_ERROR]: '业务错误',
  [ERROR_CODES.DATA_NOT_FOUND]: '数据不存在',
  [ERROR_CODES.DATA_ALREADY_EXISTS]: '数据已存在',
  [ERROR_CODES.SYSTEM_ERROR]: '系统错误',
  [ERROR_CODES.UNKNOWN_ERROR]: '未知错误'
}

// 错误代码颜色
export const ERROR_CODE_COLORS = {
  [ERROR_CODES.NETWORK_ERROR]: '#1890ff',
  [ERROR_CODES.TIMEOUT]: '#faad14',
  [ERROR_CODES.SERVER_ERROR]: '#f5222d',
  [ERROR_CODES.UNAUTHORIZED]: '#faad14',
  [ERROR_CODES.FORBIDDEN]: '#f5222d',
  [ERROR_CODES.TOKEN_EXPIRED]: '#faad14',
  [ERROR_CODES.INVALID_PARAMS]: '#f5222d',
  [ERROR_CODES.MISSING_PARAMS]: '#f5222d',
  [ERROR_CODES.BUSINESS_ERROR]: '#722ed1',
  [ERROR_CODES.DATA_NOT_FOUND]: '#722ed1',
  [ERROR_CODES.DATA_ALREADY_EXISTS]: '#722ed1',
  [ERROR_CODES.SYSTEM_ERROR]: '#eb2f96',
  [ERROR_CODES.UNKNOWN_ERROR]: '#eb2f96'
}

// 错误处理类
export class AppError extends Error {
  constructor(type, code, message, details = null) {
    super(message)
    this.type = type
    this.code = code
    this.details = details
    this.timestamp = new Date()
  }

  // 获取错误类型名称
  getTypeName() {
    return ERROR_TYPE_NAMES[this.type] || '未知错误类型'
  }

  // 获取错误代码名称
  getCodeName() {
    return ERROR_CODE_NAMES[this.code] || '未知错误代码'
  }

  // 获取错误类型颜色
  getTypeColor() {
    return ERROR_TYPE_COLORS[this.type] || '#eb2f96'
  }

  // 获取错误代码颜色
  getCodeColor() {
    return ERROR_CODE_COLORS[this.code] || '#eb2f96'
  }

  // 转换为JSON对象
  toJSON() {
    return {
      type: this.type,
      code: this.code,
      message: this.message,
      details: this.details,
      timestamp: this.timestamp
    }
  }

  // 转换为字符串
  toString() {
    return `[${this.getTypeName()}] ${this.getCodeName()}: ${this.message}`
  }
}

// 创建网络错误
export function createNetworkError(code, message, details = null) {
  return new AppError(ERROR_TYPES.NETWORK, code, message, details)
}

// 创建认证错误
export function createAuthError(code, message, details = null) {
  return new AppError(ERROR_TYPES.AUTH, code, message, details)
}

// 创建验证错误
export function createValidationError(code, message, details = null) {
  return new AppError(ERROR_TYPES.VALIDATION, code, message, details)
}

// 创建业务错误
export function createBusinessError(code, message, details = null) {
  return new AppError(ERROR_TYPES.BUSINESS, code, message, details)
}

// 创建系统错误
export function createSystemError(code, message, details = null) {
  return new AppError(ERROR_TYPES.SYSTEM, code, message, details)
}

// 处理错误
export function handleError(error) {
  if (error instanceof AppError) {
    return error
  }

  if (error.response) {
    // 处理HTTP错误
    const { status, data } = error.response
    switch (status) {
      case 400:
        return createValidationError(ERROR_CODES.INVALID_PARAMS, data.message || '无效的请求参数', data.details)
      case 401:
        return createAuthError(ERROR_CODES.UNAUTHORIZED, data.message || '未授权', data.details)
      case 403:
        return createAuthError(ERROR_CODES.FORBIDDEN, data.message || '禁止访问', data.details)
      case 404:
        return createBusinessError(ERROR_CODES.DATA_NOT_FOUND, data.message || '数据不存在', data.details)
      case 409:
        return createBusinessError(ERROR_CODES.DATA_ALREADY_EXISTS, data.message || '数据已存在', data.details)
      case 500:
        return createSystemError(ERROR_CODES.SERVER_ERROR, data.message || '服务器错误', data.details)
      default:
        return createSystemError(ERROR_CODES.UNKNOWN_ERROR, data.message || '未知错误', data.details)
    }
  }

  if (error.request) {
    // 处理请求错误
    return createNetworkError(ERROR_CODES.NETWORK_ERROR, '网络连接错误', error.message)
  }

  // 处理其他错误
  return createSystemError(ERROR_CODES.UNKNOWN_ERROR, error.message || '未知错误', error.stack)
}

// 导出所有错误处理相关的内容
export default {
  ERROR_TYPES,
  ERROR_TYPE_NAMES,
  ERROR_TYPE_COLORS,
  ERROR_CODES,
  ERROR_CODE_NAMES,
  ERROR_CODE_COLORS,
  AppError,
  createNetworkError,
  createAuthError,
  createValidationError,
  createBusinessError,
  createSystemError,
  handleError
} 