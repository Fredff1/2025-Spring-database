// 格式化日期（YYYY-MM-DD）
export const formatDate = (date) => {
  if (!date) return ''
  const d = new Date(date)
  if (isNaN(d.getTime())) return ''
  
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  
  return `${year}-${month}-${day}`
}

// 格式化时间（HH:mm:ss）
export const formatTime = (date) => {
  if (!date) return ''
  const d = new Date(date)
  if (isNaN(d.getTime())) return ''
  
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  
  return `${hours}:${minutes}:${seconds}`
}

// 格式化日期时间（YYYY-MM-DD HH:mm:ss）
export const formatDateTime = (date) => {
  if (!date) return ''
  const d = new Date(date)
  if (isNaN(d.getTime())) return ''
  
  return `${formatDate(d)} ${formatTime(d)}`
}

// 格式化金额（保留两位小数，添加千分位）
export const formatAmount = (amount) => {
  if (amount === null || amount === undefined) return ''
  const num = Number(amount)
  if (isNaN(num)) return ''
  
  return num.toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

// 格式化百分比（保留两位小数）
export const formatPercent = (value) => {
  if (value === null || value === undefined) return ''
  const num = Number(value)
  if (isNaN(num)) return ''
  
  return `${(num * 100).toFixed(2)}%`
}

// 格式化文件大小
export const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B'
  
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  
  return `${(bytes / Math.pow(k, i)).toFixed(2)} ${sizes[i]}`
}

// 格式化手机号（中间四位用*代替）
export const formatPhone = (phone) => {
  if (!phone) return ''
  return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
}

// 格式化身份证号（中间八位用*代替）
export const formatIdCard = (idCard) => {
  if (!idCard) return ''
  return idCard.replace(/(\d{4})\d{8}(\d{4})/, '$1********$2')
}

// 格式化银行卡号（每四位用空格分隔）
export const formatBankCard = (card) => {
  if (!card) return ''
  return card.replace(/(\d{4})(?=\d)/g, '$1 ')
}

// 格式化统一社会信用代码（每四位用空格分隔）
export const formatCreditCode = (code) => {
  if (!code) return ''
  return code.replace(/(\w{4})(?=\w)/g, '$1 ')
}

// 格式化组织机构代码（中间用空格分隔）
export const formatOrgCode = (code) => {
  if (!code) return ''
  return code.replace(/(\w{8})(\w)/, '$1 $2')
}

// 格式化车牌号（添加空格）
export const formatPlateNumber = (plateNumber) => {
  if (!plateNumber) return ''
  return plateNumber.replace(/([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领])([A-Z])([A-Z0-9]{4,5})([A-Z0-9挂学警港澳新能源])/, '$1 $2 $3 $4')
}

// 格式化IP地址（添加空格）
export const formatIpAddress = (ip) => {
  if (!ip) return ''
  return ip.replace(/(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})/, '$1.$2.$3.$4')
}

// 格式化MAC地址（添加空格）
export const formatMacAddress = (mac) => {
  if (!mac) return ''
  return mac.replace(/([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})/, '$1:$2:$3:$4:$5:$6')
}

// 格式化域名（添加空格）
export const formatDomain = (domain) => {
  if (!domain) return ''
  return domain.replace(/([a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9])\.([a-zA-Z]{2,})/, '$1.$2')
}

// 格式化IPv6地址（添加空格）
export const formatIpv6 = (ipv6) => {
  if (!ipv6) return ''
  return ipv6.replace(/([0-9a-fA-F]{1,4})([0-9a-fA-F]{1,4})([0-9a-fA-F]{1,4})([0-9a-fA-F]{1,4})([0-9a-fA-F]{1,4})([0-9a-fA-F]{1,4})([0-9a-fA-F]{1,4})([0-9a-fA-F]{1,4})/, '$1:$2:$3:$4:$5:$6:$7:$8')
}

// 格式化颜色代码（添加#）
export const formatColor = (color) => {
  if (!color) return ''
  return color.startsWith('#') ? color : `#${color}`
}

// 格式化JSON字符串（美化输出）
export const formatJson = (json) => {
  if (!json) return ''
  try {
    return JSON.stringify(JSON.parse(json), null, 2)
  } catch {
    return json
  }
}

// 格式化URL（添加协议）
export const formatUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http://') || url.startsWith('https://')) return url
  return `https://${url}`
}

// 格式化数字（添加千分位）
export const formatNumber = (num) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  return n.toLocaleString('zh-CN')
}

// 格式化数字（保留指定位数小数）
export const formatDecimal = (num, digits = 2) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  return n.toFixed(digits)
}

// 格式化数字（科学计数法）
export const formatScientific = (num) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  return n.toExponential()
}

// 格式化数字（中文大写）
export const formatChineseNumber = (num) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  const digits = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖']
  const units = ['', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿']
  
  const numStr = Math.floor(n).toString()
  let result = ''
  
  for (let i = 0; i < numStr.length; i++) {
    const digit = parseInt(numStr[i])
    const unit = units[numStr.length - 1 - i]
    
    if (digit === 0) {
      if (result.endsWith('零')) continue
      result += '零'
    } else {
      result += digits[digit] + unit
    }
  }
  
  return result.replace(/零+$/, '')
}

// 格式化数字（中文小写）
export const formatChineseNumberLower = (num) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  const digits = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九']
  const units = ['', '十', '百', '千', '万', '十', '百', '千', '亿']
  
  const numStr = Math.floor(n).toString()
  let result = ''
  
  for (let i = 0; i < numStr.length; i++) {
    const digit = parseInt(numStr[i])
    const unit = units[numStr.length - 1 - i]
    
    if (digit === 0) {
      if (result.endsWith('零')) continue
      result += '零'
    } else {
      result += digits[digit] + unit
    }
  }
  
  return result.replace(/零+$/, '')
}

// 格式化数字（罗马数字）
export const formatRomanNumber = (num) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  const romanNumerals = [
    { value: 1000, symbol: 'M' },
    { value: 900, symbol: 'CM' },
    { value: 500, symbol: 'D' },
    { value: 400, symbol: 'CD' },
    { value: 100, symbol: 'C' },
    { value: 90, symbol: 'XC' },
    { value: 50, symbol: 'L' },
    { value: 40, symbol: 'XL' },
    { value: 10, symbol: 'X' },
    { value: 9, symbol: 'IX' },
    { value: 5, symbol: 'V' },
    { value: 4, symbol: 'IV' },
    { value: 1, symbol: 'I' }
  ]
  
  let result = ''
  let remaining = n
  
  for (const { value, symbol } of romanNumerals) {
    while (remaining >= value) {
      result += symbol
      remaining -= value
    }
  }
  
  return result
}

// 格式化数字（二进制）
export const formatBinary = (num) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  return n.toString(2)
}

// 格式化数字（八进制）
export const formatOctal = (num) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  return n.toString(8)
}

// 格式化数字（十六进制）
export const formatHex = (num) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  return n.toString(16).toUpperCase()
}

// 格式化数字（自定义进制）
export const formatCustomBase = (num, base) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  return n.toString(base)
}

// 格式化数字（添加单位）
export const formatWithUnit = (num, unit) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  return `${n}${unit}`
}

// 格式化数字（添加前缀）
export const formatWithPrefix = (num, prefix) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  return `${prefix}${n}`
}

// 格式化数字（添加后缀）
export const formatWithSuffix = (num, suffix) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  return `${n}${suffix}`
}

// 格式化数字（添加前缀和后缀）
export const formatWithPrefixAndSuffix = (num, prefix, suffix) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  return `${prefix}${n}${suffix}`
}

// 格式化数字（添加千分位和单位）
export const formatNumberWithUnit = (num, unit) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  return `${n.toLocaleString('zh-CN')}${unit}`
}

// 格式化数字（添加千分位和前缀）
export const formatNumberWithPrefix = (num, prefix) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  return `${prefix}${n.toLocaleString('zh-CN')}`
}

// 格式化数字（添加千分位和后缀）
export const formatNumberWithSuffix = (num, suffix) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  return `${n.toLocaleString('zh-CN')}${suffix}`
}

// 格式化数字（添加千分位、前缀和后缀）
export const formatNumberWithPrefixAndSuffix = (num, prefix, suffix) => {
  if (num === null || num === undefined) return ''
  const n = Number(num)
  if (isNaN(n)) return ''
  
  return `${prefix}${n.toLocaleString('zh-CN')}${suffix}`
}

// 导出所有格式化函数
export default {
  formatDate,
  formatTime,
  formatDateTime,
  formatAmount,
  formatPercent,
  formatFileSize,
  formatPhone,
  formatIdCard,
  formatBankCard,
  formatCreditCode,
  formatOrgCode,
  formatPlateNumber,
  formatIpAddress,
  formatMacAddress,
  formatDomain,
  formatIpv6,
  formatColor,
  formatJson,
  formatUrl,
  formatNumber,
  formatDecimal,
  formatScientific,
  formatChineseNumber,
  formatChineseNumberLower,
  formatRomanNumber,
  formatBinary,
  formatOctal,
  formatHex,
  formatCustomBase,
  formatWithUnit,
  formatWithPrefix,
  formatWithSuffix,
  formatWithPrefixAndSuffix,
  formatNumberWithUnit,
  formatNumberWithPrefix,
  formatNumberWithSuffix,
  formatNumberWithPrefixAndSuffix
} 