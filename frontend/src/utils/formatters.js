/**
 * 格式化日期时间
 * @param {string|Date} dateTime 日期时间字符串或Date对象
 * @returns {string} 格式化后的日期时间字符串
 */
export const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

/**
 * 格式化金额
 * @param {number} amount 金额
 * @returns {string} 格式化后的金额字符串
 */
export const formatAmount = (amount) => {
  if (amount === null || amount === undefined) return ''
  return new Intl.NumberFormat('zh-CN', {
    style: 'currency',
    currency: 'CNY'
  }).format(amount)
}

/**
 * 格式化维修类型
 * @param {string} type 维修类型
 * @returns {string} 格式化后的维修类型
 */
export const formatRepairType = (type) => {
  const types = {
    MAINTENANCE: '常规保养',
    REPAIR: '故障维修',
    ACCIDENT: '事故维修'
  }
  return types[type] || type
}

/**
 * 格式化订单状态
 * @param {string} status 订单状态
 * @returns {string} 格式化后的订单状态
 */
export const formatOrderStatus = (status) => {
  const statuses = {
    PENDING: '待处理',
    ASSIGNED: '已分配',
    ACCEPTED: '已接受',
    REJECTED: '已拒绝',
    PROCESSING: '处理中',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return statuses[status] || status
} 