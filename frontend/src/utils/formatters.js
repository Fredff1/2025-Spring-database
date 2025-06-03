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



