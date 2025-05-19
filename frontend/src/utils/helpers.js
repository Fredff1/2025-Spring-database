// 生成随机字符串
export const generateRandomString = (length = 32) => {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'
  let result = ''
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return result
}

// 生成随机数字
export const generateRandomNumber = (min, max) => {
  return Math.floor(Math.random() * (max - min + 1)) + min
}

// 生成随机颜色
export const generateRandomColor = () => {
  return '#' + Math.floor(Math.random() * 16777215).toString(16).padStart(6, '0')
}

// 生成随机日期
export const generateRandomDate = (start, end) => {
  return new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()))
}

// 生成随机布尔值
export const generateRandomBoolean = () => {
  return Math.random() >= 0.5
}

// 生成随机数组
export const generateRandomArray = (length, generator) => {
  return Array.from({ length }, generator)
}

// 生成随机对象
export const generateRandomObject = (keys, generator) => {
  return keys.reduce((obj, key) => {
    obj[key] = generator()
    return obj
  }, {})
}

// 生成随机UUID
export const generateUUID = () => {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
    const r = Math.random() * 16 | 0
    const v = c === 'x' ? r : (r & 0x3 | 0x8)
    return v.toString(16)
  })
}

// 生成随机手机号
export const generateRandomPhone = () => {
  const prefixes = ['130', '131', '132', '133', '134', '135', '136', '137', '138', '139',
                   '150', '151', '152', '153', '155', '156', '157', '158', '159',
                   '180', '181', '182', '183', '184', '185', '186', '187', '188', '189']
  const prefix = prefixes[Math.floor(Math.random() * prefixes.length)]
  const suffix = Math.floor(Math.random() * 100000000).toString().padStart(8, '0')
  return prefix + suffix
}

// 生成随机身份证号
export const generateRandomIdCard = () => {
  const areaCode = '110101' // 北京市东城区
  const birthday = generateRandomDate(new Date('1960-01-01'), new Date('2000-12-31'))
  const dateStr = birthday.toISOString().slice(0, 10).replace(/-/g, '')
  const sequence = Math.floor(Math.random() * 1000).toString().padStart(3, '0')
  const checkCode = Math.floor(Math.random() * 10).toString()
  return areaCode + dateStr + sequence + checkCode
}

// 生成随机银行卡号
export const generateRandomBankCard = () => {
  const prefix = Math.floor(Math.random() * 10000).toString().padStart(4, '0')
  const middle = Array.from({ length: 3 }, () => Math.floor(Math.random() * 10000).toString().padStart(4, '0')).join('')
  const suffix = Math.floor(Math.random() * 10000).toString().padStart(4, '0')
  return prefix + middle + suffix
}

// 生成随机统一社会信用代码
export const generateRandomCreditCode = () => {
  const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
  let result = ''
  for (let i = 0; i < 17; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return result + chars.charAt(Math.floor(Math.random() * 10))
}

// 生成随机组织机构代码
export const generateRandomOrgCode = () => {
  const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
  let result = ''
  for (let i = 0; i < 8; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return result + '-' + chars.charAt(Math.floor(Math.random() * chars.length))
}

// 生成随机车牌号
export const generateRandomPlateNumber = () => {
  const provinces = '京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领'
  const letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
  const numbers = '0123456789'
  const province = provinces.charAt(Math.floor(Math.random() * provinces.length))
  const letter = letters.charAt(Math.floor(Math.random() * letters.length))
  const number = Array.from({ length: 5 }, () => numbers.charAt(Math.floor(Math.random() * numbers.length))).join('')
  return province + letter + number
}

// 生成随机IP地址
export const generateRandomIpAddress = () => {
  return Array.from({ length: 4 }, () => Math.floor(Math.random() * 256)).join('.')
}

// 生成随机MAC地址
export const generateRandomMacAddress = () => {
  const hex = '0123456789ABCDEF'
  return Array.from({ length: 6 }, () => {
    return Array.from({ length: 2 }, () => hex.charAt(Math.floor(Math.random() * hex.length))).join('')
  }).join(':')
}

// 生成随机域名
export const generateRandomDomain = () => {
  const chars = 'abcdefghijklmnopqrstuvwxyz0123456789'
  const tlds = ['com', 'net', 'org', 'edu', 'gov', 'mil', 'biz', 'info', 'name', 'mobi', 'pro', 'travel', 'museum', 'int', 'aero', 'post', 'rec', 'asia']
  const length = Math.floor(Math.random() * 10) + 5
  const name = Array.from({ length }, () => chars.charAt(Math.floor(Math.random() * chars.length))).join('')
  const tld = tlds[Math.floor(Math.random() * tlds.length)]
  return name + '.' + tld
}

// 生成随机IPv6地址
export const generateRandomIpv6 = () => {
  const hex = '0123456789abcdef'
  return Array.from({ length: 8 }, () => {
    return Array.from({ length: 4 }, () => hex.charAt(Math.floor(Math.random() * hex.length))).join('')
  }).join(':')
}

// 生成随机颜色代码
export const generateRandomColorCode = () => {
  return '#' + Array.from({ length: 6 }, () => {
    return '0123456789abcdef'.charAt(Math.floor(Math.random() * 16))
  }).join('')
}

// 生成随机JSON字符串
export const generateRandomJson = () => {
  const obj = {
    id: generateUUID(),
    name: generateRandomString(10),
    age: generateRandomNumber(18, 60),
    email: generateRandomString(10) + '@example.com',
    phone: generateRandomPhone(),
    address: {
      street: generateRandomString(20),
      city: generateRandomString(10),
      country: generateRandomString(10)
    },
    tags: generateRandomArray(3, () => generateRandomString(5)),
    active: generateRandomBoolean(),
    createdAt: new Date().toISOString()
  }
  return JSON.stringify(obj)
}

// 生成随机URL
export const generateRandomUrl = () => {
  const protocols = ['http', 'https']
  const protocol = protocols[Math.floor(Math.random() * protocols.length)]
  const domain = generateRandomDomain()
  const path = '/' + generateRandomString(10)
  return `${protocol}://${domain}${path}`
}

// 生成随机数字（带千分位）
export const generateRandomNumberWithCommas = () => {
  return generateRandomNumber(1000, 9999999).toLocaleString('zh-CN')
}

// 生成随机数字（带单位）
export const generateRandomNumberWithUnit = (unit) => {
  return generateRandomNumber(1, 1000) + unit
}

// 生成随机数字（带前缀）
export const generateRandomNumberWithPrefix = (prefix) => {
  return prefix + generateRandomNumber(1, 1000)
}

// 生成随机数字（带后缀）
export const generateRandomNumberWithSuffix = (suffix) => {
  return generateRandomNumber(1, 1000) + suffix
}

// 生成随机数字（带前缀和后缀）
export const generateRandomNumberWithPrefixAndSuffix = (prefix, suffix) => {
  return prefix + generateRandomNumber(1, 1000) + suffix
}

// 生成随机数字（带千分位和单位）
export const generateRandomNumberWithCommasAndUnit = (unit) => {
  return generateRandomNumber(1000, 9999999).toLocaleString('zh-CN') + unit
}

// 生成随机数字（带千分位和前缀）
export const generateRandomNumberWithCommasAndPrefix = (prefix) => {
  return prefix + generateRandomNumber(1000, 9999999).toLocaleString('zh-CN')
}

// 生成随机数字（带千分位和后缀）
export const generateRandomNumberWithCommasAndSuffix = (suffix) => {
  return generateRandomNumber(1000, 9999999).toLocaleString('zh-CN') + suffix
}

// 生成随机数字（带千分位、前缀和后缀）
export const generateRandomNumberWithCommasPrefixAndSuffix = (prefix, suffix) => {
  return prefix + generateRandomNumber(1000, 9999999).toLocaleString('zh-CN') + suffix
}

// 生成随机日期（YYYY-MM-DD）
export const generateRandomDateString = () => {
  const date = generateRandomDate(new Date('2000-01-01'), new Date())
  return date.toISOString().slice(0, 10)
}

// 生成随机时间（HH:mm:ss）
export const generateRandomTimeString = () => {
  const date = new Date()
  date.setHours(Math.floor(Math.random() * 24))
  date.setMinutes(Math.floor(Math.random() * 60))
  date.setSeconds(Math.floor(Math.random() * 60))
  return date.toTimeString().slice(0, 8)
}

// 生成随机日期时间（YYYY-MM-DD HH:mm:ss）
export const generateRandomDateTimeString = () => {
  const date = generateRandomDate(new Date('2000-01-01'), new Date())
  return date.toISOString().slice(0, 19).replace('T', ' ')
}

// 生成随机金额（保留两位小数）
export const generateRandomAmount = () => {
  return (Math.random() * 10000).toFixed(2)
}

// 生成随机百分比（保留两位小数）
export const generateRandomPercent = () => {
  return (Math.random() * 100).toFixed(2) + '%'
}

// 生成随机文件大小
export const generateRandomFileSize = () => {
  const units = ['B', 'KB', 'MB', 'GB', 'TB']
  const unit = units[Math.floor(Math.random() * units.length)]
  const size = Math.random() * 1000
  return size.toFixed(2) + ' ' + unit
}

// 生成随机手机号（带掩码）
export const generateRandomMaskedPhone = () => {
  const phone = generateRandomPhone()
  return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
}

// 生成随机身份证号（带掩码）
export const generateRandomMaskedIdCard = () => {
  const idCard = generateRandomIdCard()
  return idCard.replace(/(\d{4})\d{8}(\d{4})/, '$1********$2')
}

// 生成随机银行卡号（带空格）
export const generateRandomSpacedBankCard = () => {
  const card = generateRandomBankCard()
  return card.replace(/(\d{4})(?=\d)/g, '$1 ')
}

// 生成随机统一社会信用代码（带空格）
export const generateRandomSpacedCreditCode = () => {
  const code = generateRandomCreditCode()
  return code.replace(/(\w{4})(?=\w)/g, '$1 ')
}

// 生成随机组织机构代码（带空格）
export const generateRandomSpacedOrgCode = () => {
  const code = generateRandomOrgCode()
  return code.replace(/(\w{8})(\w)/, '$1 $2')
}

// 生成随机车牌号（带空格）
export const generateRandomSpacedPlateNumber = () => {
  const plate = generateRandomPlateNumber()
  return plate.replace(/([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领])([A-Z])([A-Z0-9]{4,5})([A-Z0-9挂学警港澳新能源])/, '$1 $2 $3 $4')
}

// 生成随机IP地址（带点）
export const generateRandomDottedIpAddress = () => {
  return generateRandomIpAddress()
}

// 生成随机MAC地址（带冒号）
export const generateRandomColonedMacAddress = () => {
  return generateRandomMacAddress()
}

// 生成随机域名（带点）
export const generateRandomDottedDomain = () => {
  return generateRandomDomain()
}

// 生成随机IPv6地址（带冒号）
export const generateRandomColonedIpv6 = () => {
  return generateRandomIpv6()
}

// 生成随机颜色代码（带#）
export const generateRandomHashedColor = () => {
  return generateRandomColorCode()
}

// 生成随机JSON字符串（美化）
export const generateRandomPrettyJson = () => {
  return JSON.stringify(JSON.parse(generateRandomJson()), null, 2)
}

// 生成随机URL（带协议）
export const generateRandomProtocoledUrl = () => {
  return generateRandomUrl()
}

// 导出所有工具函数
export default {
  generateRandomString,
  generateRandomNumber,
  generateRandomColor,
  generateRandomDate,
  generateRandomBoolean,
  generateRandomArray,
  generateRandomObject,
  generateUUID,
  generateRandomPhone,
  generateRandomIdCard,
  generateRandomBankCard,
  generateRandomCreditCode,
  generateRandomOrgCode,
  generateRandomPlateNumber,
  generateRandomIpAddress,
  generateRandomMacAddress,
  generateRandomDomain,
  generateRandomIpv6,
  generateRandomColorCode,
  generateRandomJson,
  generateRandomUrl,
  generateRandomNumberWithCommas,
  generateRandomNumberWithUnit,
  generateRandomNumberWithPrefix,
  generateRandomNumberWithSuffix,
  generateRandomNumberWithPrefixAndSuffix,
  generateRandomNumberWithCommasAndUnit,
  generateRandomNumberWithCommasAndPrefix,
  generateRandomNumberWithCommasAndSuffix,
  generateRandomNumberWithCommasPrefixAndSuffix,
  generateRandomDateString,
  generateRandomTimeString,
  generateRandomDateTimeString,
  generateRandomAmount,
  generateRandomPercent,
  generateRandomFileSize,
  generateRandomMaskedPhone,
  generateRandomMaskedIdCard,
  generateRandomSpacedBankCard,
  generateRandomSpacedCreditCode,
  generateRandomSpacedOrgCode,
  generateRandomSpacedPlateNumber,
  generateRandomDottedIpAddress,
  generateRandomColonedMacAddress,
  generateRandomDottedDomain,
  generateRandomColonedIpv6,
  generateRandomHashedColor,
  generateRandomPrettyJson,
  generateRandomProtocoledUrl
} 