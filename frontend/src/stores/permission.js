import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { useAuthStore } from './auth'

export const usePermissionStore = defineStore('permission', () => {
  const authStore = useAuthStore()
  
  const routes = ref([])
  const addRoutes = ref([])

  const hasPermission = computed(() => {
    return (permission) => {
      if (!permission) return true
      
      if (!authStore.isAuthenticated) return false
      
      const role = authStore.role
      switch (role) {
        case 'admin':
          return true 
        case 'repairman':
          return [
            'view_orders',
            'edit_orders',
            'complete_orders',
            'view_history',
            'view_income',
            'edit_profile'
          ].includes(permission)
        case 'customer':
          return [
            'view_orders',
            'create_orders',
            'view_history',
            'edit_profile'
          ].includes(permission)
        default:
          return false
      }
    }
  })

  const generateRoutes = () => {
    const allRoutes = [
      {
        path: '/admin',
        component: 'Layout',
        redirect: '/admin/dashboard',
        children: [
          {
            path: 'dashboard',
            name: 'AdminDashboard',
            component: 'admin/Dashboard',
            meta: { title: '工作台', icon: 'dashboard' }
          },
          {
            path: 'orders',
            name: 'AdminOrders',
            component: 'admin/Orders',
            meta: { title: '维修订单', icon: 'order' }
          },
          {
            path: 'vehicles',
            name: 'AdminVehicles',
            component: 'admin/Vehicles',
            meta: { title: '车辆管理', icon: 'car' }
          },
          {
            path: 'inventory',
            name: 'AdminInventory',
            component: 'admin/Inventory',
            meta: { title: '库存管理', icon: 'box' }
          },
          {
            path: 'finance',
            name: 'AdminFinance',
            component: 'admin/Finance',
            meta: { title: '财务管理', icon: 'money' }
          },
          {
            path: 'statistics',
            name: 'AdminStatistics',
            component: 'admin/Statistics',
            meta: { title: '统计报表', icon: 'chart' }
          },
          {
            path: 'settings',
            name: 'AdminSettings',
            component: 'admin/Settings',
            meta: { title: '系统设置', icon: 'setting' }
          }
        ]
      },
      {
        path: '/repairman',
        component: 'Layout',
        redirect: '/repairman/dashboard',
        children: [
          {
            path: 'dashboard',
            name: 'RepairmanDashboard',
            component: 'repairman/Dashboard',
            meta: { title: '工作台', icon: 'dashboard' }
          },
          {
            path: 'orders',
            name: 'RepairmanOrders',
            component: 'repairman/Orders',
            meta: { title: '维修订单', icon: 'order' }
          },
          {
            path: 'history',
            name: 'RepairmanHistory',
            component: 'repairman/History',
            meta: { title: '历史记录', icon: 'history' }
          },
          {
            path: 'income',
            name: 'RepairmanIncome',
            component: 'repairman/Income',
            meta: { title: '收入统计', icon: 'money' }
          },
          {
            path: 'profile',
            name: 'RepairmanProfile',
            component: 'repairman/Profile',
            meta: { title: '个人资料', icon: 'user' }
          }
        ]
      },
      {
        path: '/customer',
        component: 'Layout',
        redirect: '/customer/dashboard',
        children: [
          {
            path: 'dashboard',
            name: 'CustomerDashboard',
            component: 'customer/Dashboard',
            meta: { title: '工作台', icon: 'dashboard' }
          },
          {
            path: 'orders',
            name: 'CustomerOrders',
            component: 'customer/Orders',
            meta: { title: '维修订单', icon: 'order' }
          },
          {
            path: 'history',
            name: 'CustomerHistory',
            component: 'customer/History',
            meta: { title: '历史记录', icon: 'history' }
          },
          {
            path: 'profile',
            name: 'CustomerProfile',
            component: 'customer/Profile',
            meta: { title: '个人资料', icon: 'user' }
          }
        ]
      }
    ]

    routes.value = allRoutes
    addRoutes.value = allRoutes
    return allRoutes
  }

  const resetRoutes = () => {
    routes.value = []
    addRoutes.value = []
  }

  return {
    routes,
    addRoutes,
    hasPermission,
    generateRoutes,
    resetRoutes
  }
}) 