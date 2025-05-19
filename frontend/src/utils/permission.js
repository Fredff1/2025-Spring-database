import { ROLES, ROLE_PERMISSIONS } from './constants'
import { localStorage } from './storage'
import { STORAGE_KEYS } from './storage'

// 权限类型
export const PERMISSION_TYPES = {
  VIEW: 'view',
  CREATE: 'create',
  EDIT: 'edit',
  DELETE: 'delete',
  EXPORT: 'export',
  IMPORT: 'import',
  APPROVE: 'approve',
  REJECT: 'reject'
}

// 权限类型名称
export const PERMISSION_TYPE_NAMES = {
  [PERMISSION_TYPES.VIEW]: '查看',
  [PERMISSION_TYPES.CREATE]: '创建',
  [PERMISSION_TYPES.EDIT]: '编辑',
  [PERMISSION_TYPES.DELETE]: '删除',
  [PERMISSION_TYPES.EXPORT]: '导出',
  [PERMISSION_TYPES.IMPORT]: '导入',
  [PERMISSION_TYPES.APPROVE]: '审批',
  [PERMISSION_TYPES.REJECT]: '驳回'
}

// 权限类型颜色
export const PERMISSION_TYPE_COLORS = {
  [PERMISSION_TYPES.VIEW]: '#1890ff',
  [PERMISSION_TYPES.CREATE]: '#52c41a',
  [PERMISSION_TYPES.EDIT]: '#faad14',
  [PERMISSION_TYPES.DELETE]: '#f5222d',
  [PERMISSION_TYPES.EXPORT]: '#722ed1',
  [PERMISSION_TYPES.IMPORT]: '#13c2c2',
  [PERMISSION_TYPES.APPROVE]: '#eb2f96',
  [PERMISSION_TYPES.REJECT]: '#fa8c16'
}

// 权限模块
export const PERMISSION_MODULES = {
  DASHBOARD: 'dashboard',
  REPAIR_ORDERS: 'repair_orders',
  VEHICLES: 'vehicles',
  INVENTORY: 'inventory',
  FINANCE: 'finance',
  STATISTICS: 'statistics',
  SYSTEM: 'system'
}

// 权限模块名称
export const PERMISSION_MODULE_NAMES = {
  [PERMISSION_MODULES.DASHBOARD]: '仪表盘',
  [PERMISSION_MODULES.REPAIR_ORDERS]: '维修订单',
  [PERMISSION_MODULES.VEHICLES]: '车辆管理',
  [PERMISSION_MODULES.INVENTORY]: '库存管理',
  [PERMISSION_MODULES.FINANCE]: '财务管理',
  [PERMISSION_MODULES.STATISTICS]: '统计分析',
  [PERMISSION_MODULES.SYSTEM]: '系统管理'
}

// 权限模块颜色
export const PERMISSION_MODULE_COLORS = {
  [PERMISSION_MODULES.DASHBOARD]: '#1890ff',
  [PERMISSION_MODULES.REPAIR_ORDERS]: '#52c41a',
  [PERMISSION_MODULES.VEHICLES]: '#faad14',
  [PERMISSION_MODULES.INVENTORY]: '#f5222d',
  [PERMISSION_MODULES.FINANCE]: '#722ed1',
  [PERMISSION_MODULES.STATISTICS]: '#13c2c2',
  [PERMISSION_MODULES.SYSTEM]: '#eb2f96'
}

// 权限类
export class Permission {
  constructor() {
    this.user = localStorage.get(STORAGE_KEYS.USER)
    this.role = this.user?.role || ROLES.CUSTOMER
    this.permissions = ROLE_PERMISSIONS[this.role] || []
  }

  // 检查是否有权限
  has(permission) {
    if (!permission) {
      return false
    }

    // 管理员拥有所有权限
    if (this.role === ROLES.ADMIN) {
      return true
    }

    // 检查具体权限
    return this.permissions.includes(permission)
  }

  // 检查是否有模块权限
  hasModule(module) {
    if (!module) {
      return false
    }

    // 管理员拥有所有模块权限
    if (this.role === ROLES.ADMIN) {
      return true
    }

    // 检查模块权限
    return this.permissions.some(permission => permission.startsWith(`${PERMISSION_TYPES.VIEW}:${module}`))
  }

  // 检查是否有操作权限
  hasAction(module, action) {
    if (!module || !action) {
      return false
    }

    // 管理员拥有所有操作权限
    if (this.role === ROLES.ADMIN) {
      return true
    }

    // 检查操作权限
    return this.permissions.includes(`${action}:${module}`)
  }

  // 获取所有权限
  getAll() {
    return this.permissions
  }

  // 获取所有模块权限
  getAllModules() {
    return Object.values(PERMISSION_MODULES).filter(module => this.hasModule(module))
  }

  // 获取所有操作权限
  getAllActions(module) {
    if (!module) {
      return []
    }

    return Object.values(PERMISSION_TYPES).filter(action => this.hasAction(module, action))
  }

  // 获取用户角色
  getRole() {
    return this.role
  }

  // 获取用户信息
  getUser() {
    return this.user
  }

  // 更新用户信息
  updateUser(user) {
    this.user = user
    this.role = user?.role || ROLES.CUSTOMER
    this.permissions = ROLE_PERMISSIONS[this.role] || []
    localStorage.set(STORAGE_KEYS.USER, user)
  }

  // 清除用户信息
  clearUser() {
    this.user = null
    this.role = ROLES.CUSTOMER
    this.permissions = ROLE_PERMISSIONS[this.role] || []
    localStorage.remove(STORAGE_KEYS.USER)
  }
}

// 创建权限实例
export const permission = new Permission()

// 导出所有权限相关的内容
export default {
  PERMISSION_TYPES,
  PERMISSION_TYPE_NAMES,
  PERMISSION_TYPE_COLORS,
  PERMISSION_MODULES,
  PERMISSION_MODULE_NAMES,
  PERMISSION_MODULE_COLORS,
  Permission,
  permission
} 