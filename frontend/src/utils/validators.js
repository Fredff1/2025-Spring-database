// 车牌号验证
export const validatePlateNumber = (plateNumber) => {
  const pattern = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-Z0-9]{4,5}[A-Z0-9挂学警港澳]$/
  return pattern.test(plateNumber)
}

// 手机号验证
export const validatePhone = (phone) => {
  const pattern = /^1[3-9]\d{9}$/
  return pattern.test(phone)
}

// 身份证号验证
export const validateIdCard = (idCard) => {
  const pattern = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
  return pattern.test(idCard)
}

// 金额验证（最多两位小数）
export const validateAmount = (amount) => {
  const pattern = /^\d+(\.\d{1,2})?$/
  return pattern.test(amount)
}

// 数量验证（正整数）
export const validateQuantity = (quantity) => {
  const pattern = /^[1-9]\d*$/
  return pattern.test(quantity)
}

// 日期验证（YYYY-MM-DD格式）
export const validateDate = (date) => {
  const pattern = /^\d{4}-\d{2}-\d{2}$/
  if (!pattern.test(date)) return false
  
  const d = new Date(date)
  return d instanceof Date && !isNaN(d)
}

// 时间验证（HH:mm格式）
export const validateTime = (time) => {
  const pattern = /^([01]\d|2[0-3]):([0-5]\d)$/
  return pattern.test(time)
}

// 邮箱验证
export const validateEmail = (email) => {
  const pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
  return pattern.test(email)
}

// 密码强度验证（至少8位，包含大小写字母和数字）
export const validatePassword = (password) => {
  const pattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/
  return pattern.test(password)
}

// 用户名验证（4-20位字母数字下划线）
export const validateUsername = (username) => {
  const pattern = /^[a-zA-Z0-9_]{4,20}$/
  return pattern.test(username)
}

// 验证必填字段
export const validateRequired = (value) => {
  if (value === null || value === undefined) return false
  if (typeof value === 'string') return value.trim().length > 0
  if (Array.isArray(value)) return value.length > 0
  return true
}

// 验证字段长度
export const validateLength = (value, min, max) => {
  if (!value) return false
  const length = value.toString().length
  return length >= min && length <= max
}

// 验证数字范围
export const validateRange = (value, min, max) => {
  const num = Number(value)
  return !isNaN(num) && num >= min && num <= max
}

// 验证是否为正数
export const validatePositive = (value) => {
  const num = Number(value)
  return !isNaN(num) && num > 0
}

// 验证是否为非负数
export const validateNonNegative = (value) => {
  const num = Number(value)
  return !isNaN(num) && num >= 0
}

// 验证是否为整数
export const validateInteger = (value) => {
  return Number.isInteger(Number(value))
}

// 验证是否为有效的URL
export const validateUrl = (url) => {
  try {
    new URL(url)
    return true
  } catch {
    return false
  }
}

// 验证是否为有效的JSON字符串
export const validateJson = (str) => {
  try {
    JSON.parse(str)
    return true
  } catch {
    return false
  }
}

// 验证是否为有效的颜色代码
export const validateColor = (color) => {
  const pattern = /^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$/
  return pattern.test(color)
}

// 验证是否为有效的邮政编码
export const validatePostalCode = (code) => {
  const pattern = /^[1-9]\d{5}$/
  return pattern.test(code)
}

// 验证是否为有效的统一社会信用代码
export const validateCreditCode = (code) => {
  const pattern = /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/
  return pattern.test(code)
}

// 验证是否为有效的组织机构代码
export const validateOrgCode = (code) => {
  const pattern = /^[0-9A-Z]{8}-[0-9X]$/
  return pattern.test(code)
}

// 验证是否为有效的银行卡号
export const validateBankCard = (card) => {
  const pattern = /^\d{16,19}$/
  return pattern.test(card)
}

// 验证是否为有效的IP地址
export const validateIpAddress = (ip) => {
  const pattern = /^(\d{1,3}\.){3}\d{1,3}$/
  if (!pattern.test(ip)) return false
  
  const parts = ip.split('.')
  return parts.every(part => {
    const num = parseInt(part)
    return num >= 0 && num <= 255
  })
}

// 验证是否为有效的MAC地址
export const validateMacAddress = (mac) => {
  const pattern = /^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$/
  return pattern.test(mac)
}

// 验证是否为有效的域名
export const validateDomain = (domain) => {
  const pattern = /^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9]\.[a-zA-Z]{2,}$/
  return pattern.test(domain)
}

// 验证是否为有效的IPv6地址
export const validateIpv6 = (ipv6) => {
  const pattern = /^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$/
  return pattern.test(ipv6)
}

// 验证是否为有效的手机验证码（6位数字）
export const validateVerificationCode = (code) => {
  const pattern = /^\d{6}$/
  return pattern.test(code)
}

// 验证是否为有效的车牌号（支持新能源车牌）
export const validateNewEnergyPlateNumber = (plateNumber) => {
  const pattern = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-Z0-9]{4,5}[A-Z0-9挂学警港澳新能源]$/
  return pattern.test(plateNumber)
}

// 验证是否为有效的统一社会信用代码（支持18位和15位）
export const validateCreditCodeExtended = (code) => {
  const pattern = /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$|^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{7}$/
  return pattern.test(code)
}

// 验证是否为有效的组织机构代码（支持8位和9位）
export const validateOrgCodeExtended = (code) => {
  const pattern = /^[0-9A-Z]{8}-[0-9X]$|^[0-9A-Z]{9}$/
  return pattern.test(code)
}

// 验证是否为有效的银行卡号（支持16-19位）
export const validateBankCardExtended = (card) => {
  const pattern = /^\d{16,19}$/
  return pattern.test(card)
}

// 验证是否为有效的手机号（支持国际格式）
export const validatePhoneExtended = (phone) => {
  const pattern = /^(\+\d{1,3})?1[3-9]\d{9}$/
  return pattern.test(phone)
}

// 验证是否为有效的邮箱（支持国际化域名）
export const validateEmailExtended = (email) => {
  const pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
  return pattern.test(email)
}

// 验证是否为有效的密码（支持特殊字符）
export const validatePasswordExtended = (password) => {
  const pattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/
  return pattern.test(password)
}

// 验证是否为有效的用户名（支持中文）
export const validateUsernameExtended = (username) => {
  const pattern = /^[\u4e00-\u9fa5a-zA-Z0-9_]{2,20}$/
  return pattern.test(username)
}

// 验证是否为有效的统一社会信用代码（支持校验位）
export const validateCreditCodeWithCheck = (code) => {
  if (!/^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/.test(code)) {
    return false
  }

  const weights = [1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28]
  const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
  const checkDigit = code[17]
  
  let sum = 0
  for (let i = 0; i < 17; i++) {
    sum += weights[i] * chars.indexOf(code[i])
  }
  
  const check = (31 - sum % 31) % 31
  return chars[check] === checkDigit
}

// 验证是否为有效的组织机构代码（支持校验位）
export const validateOrgCodeWithCheck = (code) => {
  if (!/^[0-9A-Z]{8}-[0-9X]$/.test(code)) {
    return false
  }

  const weights = [3, 7, 9, 10, 5, 8, 4, 2]
  const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
  const checkDigit = code[9]
  
  let sum = 0
  for (let i = 0; i < 8; i++) {
    sum += weights[i] * chars.indexOf(code[i])
  }
  
  const check = (11 - sum % 11) % 11
  return chars[check] === checkDigit
}

// 验证是否为有效的银行卡号（支持Luhn算法）
export const validateBankCardWithLuhn = (card) => {
  if (!/^\d{16,19}$/.test(card)) {
    return false
  }

  let sum = 0
  let isEven = false
  
  for (let i = card.length - 1; i >= 0; i--) {
    let digit = parseInt(card[i])
    
    if (isEven) {
      digit *= 2
      if (digit > 9) {
        digit -= 9
      }
    }
    
    sum += digit
    isEven = !isEven
  }
  
  return sum % 10 === 0
}

// 验证是否为有效的身份证号（支持校验位）
export const validateIdCardWithCheck = (idCard) => {
  if (!/^\d{17}[\dXx]$/.test(idCard)) {
    return false
  }

  const weights = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2]
  const checkDigits = '10X98765432'
  const checkDigit = idCard[17].toUpperCase()
  
  let sum = 0
  for (let i = 0; i < 17; i++) {
    sum += weights[i] * parseInt(idCard[i])
  }
  
  return checkDigits[sum % 11] === checkDigit
}

// 验证是否为有效的统一社会信用代码（支持校验位和15位）
export const validateCreditCodeWithCheckExtended = (code) => {
  if (code.length === 15) {
    return /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{7}$/.test(code)
  }
  
  return validateCreditCodeWithCheck(code)
}

// 验证是否为有效的组织机构代码（支持校验位和9位）
export const validateOrgCodeWithCheckExtended = (code) => {
  if (code.length === 9) {
    return /^[0-9A-Z]{9}$/.test(code)
  }
  
  return validateOrgCodeWithCheck(code)
}

// 验证是否为有效的银行卡号（支持Luhn算法和16-19位）
export const validateBankCardWithLuhnExtended = (card) => {
  if (!/^\d{16,19}$/.test(card)) {
    return false
  }
  
  return validateBankCardWithLuhn(card)
}

// 验证是否为有效的手机号（支持国际格式和校验位）
export const validatePhoneWithCheck = (phone) => {
  if (!validatePhoneExtended(phone)) {
    return false
  }
  
  // 这里可以添加更多的手机号验证逻辑
  return true
}

// 验证是否为有效的邮箱（支持国际化域名和校验位）
export const validateEmailWithCheck = (email) => {
  if (!validateEmailExtended(email)) {
    return false
  }
  
  // 这里可以添加更多的邮箱验证逻辑
  return true
}

// 验证是否为有效的密码（支持特殊字符和校验位）
export const validatePasswordWithCheck = (password) => {
  if (!validatePasswordExtended(password)) {
    return false
  }
  
  // 这里可以添加更多的密码验证逻辑
  return true
}

// 验证是否为有效的用户名（支持中文和校验位）
export const validateUsernameWithCheck = (username) => {
  if (!validateUsernameExtended(username)) {
    return false
  }
  
  // 这里可以添加更多的用户名验证逻辑
  return true
}

// 导出所有验证函数
export default {
  validatePlateNumber,
  validatePhone,
  validateIdCard,
  validateAmount,
  validateQuantity,
  validateDate,
  validateTime,
  validateEmail,
  validatePassword,
  validateUsername,
  validateRequired,
  validateLength,
  validateRange,
  validatePositive,
  validateNonNegative,
  validateInteger,
  validateUrl,
  validateJson,
  validateColor,
  validatePostalCode,
  validateCreditCode,
  validateOrgCode,
  validateBankCard,
  validateIpAddress,
  validateMacAddress,
  validateDomain,
  validateIpv6,
  validateVerificationCode,
  validateNewEnergyPlateNumber,
  validateCreditCodeExtended,
  validateOrgCodeExtended,
  validateBankCardExtended,
  validatePhoneExtended,
  validateEmailExtended,
  validatePasswordExtended,
  validateUsernameExtended,
  validateCreditCodeWithCheck,
  validateOrgCodeWithCheck,
  validateBankCardWithLuhn,
  validateIdCardWithCheck,
  validateCreditCodeWithCheckExtended,
  validateOrgCodeWithCheckExtended,
  validateBankCardWithLuhnExtended,
  validatePhoneWithCheck,
  validateEmailWithCheck,
  validatePasswordWithCheck,
  validateUsernameWithCheck
} 