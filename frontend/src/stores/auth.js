import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { admin, repairman, customer } from '@/api'

export const useAuthStore = defineStore('auth', () => {
  // 状态
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))
  const role = ref(localStorage.getItem('role') || '')

  // 计算属性
  const isAuthenticated = computed(() => !!token.value)
  const isAdmin = computed(() => role.value === 'admin')
  const isRepairman = computed(() => role.value === 'repairman')
  const isCustomer = computed(() => role.value === 'customer')

  // 方法
  const setToken = (newToken) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUserInfo = (info) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  const setRole = (newRole) => {
    role.value = newRole
    localStorage.setItem('role', newRole)
  }

  const login = async (credentials) => {
    try {
      let res
      switch (credentials.role) {
        case 'ADMIN':
          res = await admin.login(credentials)
          break
        case 'REPAIRMAN':
          res = await repairman.login(credentials)
          break
        case 'CUSTOMER':
          console.log(credentials.role)
          res = await customer.login(credentials)
          break
        default:
          throw new Error('Invalid role')
      }
      

      setToken(res.token)
      //setUserInfo(res.userInfo)
      setRole(credentials.role)
      return res
    } catch (error) {
      console.error('Login failed:', error)
      throw error
    }
  }

  const logout = () => {
    token.value = ''
    userInfo.value = {}
    role.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    localStorage.removeItem('role')
  }

  // const updateUserInfo = async () => {
  //   try {
  //     let res
  //     switch (role.value) {
  //       case 'admin':
  //         res = await admin.getProfile()
  //         break
  //       case 'repairman':
  //         res = await repairman.getProfile()
  //         break
  //       case 'customer':
  //         res = await customer.getProfile()
  //         break
  //       default:
  //         throw new Error('Invalid role')
  //     }
  //     setUserInfo(res)
  //     return res
  //   } catch (error) {
  //     console.error('Update user info failed:', error)
  //     throw error
  //   }
  // }

  return {
    // 状态
    token,
    userInfo,
    role,
    // 计算属性
    isAuthenticated,
    isAdmin,
    isRepairman,
    isCustomer,
    // 方法
    setToken,
    setUserInfo,
    setRole,
    login,
    logout,
    // updateUserInfo
  }
}) 