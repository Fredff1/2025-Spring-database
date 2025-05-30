import { createRouter, createWebHistory } from 'vue-router';


// 布局组件
import DefaultLayout from '@/layouts/DefaultLayout.vue';
import UserLayout from '@/layouts/UserLayout.vue';
import AdminLayout from '@/layouts/AdminLayout.vue';
import RepairmanLayout from '@/layouts/RepairmanLayout.vue';

// 公共页面
import Login from '@/views/Login.vue';
import Register from '@/views/Register.vue';


// 用户页面
import UserDashboard from '@/views/user/Dashboard.vue';
import UserVehicles from '@/views/user/Vehicles.vue';
import UserRepairOrders from '@/views/user/RepairOrders.vue';
import UserRepairHistory from '@/views/user/RepairHistory.vue';
import UserProfile from '@/views/user/Profile.vue';

// 管理员页面
import AdminDashboard from '@/views/admin/Dashboard.vue';
import AdminUsers from '@/views/admin/Users.vue';
import AdminRepairmen from '@/views/admin/Repairmen.vue';
import AdminVehicles from '@/views/admin/Vehicles.vue';
import AdminOrders from '@/views/admin/Orders.vue';
import AdminInventory from '@/views/admin/Inventory.vue';
import AdminFinance from '@/views/admin/Finance.vue';
import AdminStatistics from '@/views/admin/Statistics.vue';
import AdminSettings from '@/views/admin/Settings.vue';
import AdminProfile from '@/views/admin/Profile.vue';

// 维修人员页面
import RepairmanDashboard from '@/views/repairman/Dashboard.vue';
import RepairmanOrders from '@/views/repairman/Orders.vue';
import RepairmanHistory from '@/views/repairman/History.vue';
import RepairmanIncome from '@/views/repairman/Income.vue';
import RepairmanProfile from '@/views/repairman/Profile.vue';
import RepairmanAssignment from '@/views/repairman/Assignments.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      component: DefaultLayout,
      children: [
        {
          path: '',
          name: 'Login',
          component: Login
        }
      ]
    },
    {
      path: '/register',
      component: DefaultLayout,
      children: [
        {
          path: '',
          name: 'Register',
          component: Register
        }
      ]
    },
    // 用户路由
    {
      path: '/user',
      component: UserLayout,
      children: [
        {
          path: 'dashboard',
          name: 'UserDashboard',
          component: UserDashboard
        },
        {
          path: 'vehicles',
          name: 'UserVehicles',
          component: UserVehicles
        },
        {
          path: 'repair-orders',
          name: 'UserRepairOrders',
          component: UserRepairOrders
        },
        {
          path: 'repair-history',
          name: 'UserRepairHistory',
          component: UserRepairHistory
        },
        {
          path: 'profile',
          name: 'UserProfile',
          component: UserProfile
        }
      ]
    },
    // 管理员路由
    {
      path: '/admin',
      component: AdminLayout,
      children: [
        {
          path: 'dashboard',
          name: 'AdminDashboard',
          component: AdminDashboard
        },
        {
          path: 'users',
          name: 'AdminUsers',
          component: AdminUsers
        },
        {
          path: 'repairmen',
          name: 'AdminRepairmen',
          component: AdminRepairmen
        },
        {
          path: 'vehicles',
          name: 'AdminVehicles',
          component: AdminVehicles
        },
        {
          path: 'orders',
          name: 'AdminOrders',
          component: AdminOrders
        },
        {
          path: 'inventory',
          name: 'AdminInventory',
          component: AdminInventory
        },
        {
          path: 'finance',
          name: 'AdminFinance',
          component: AdminFinance
        },
        {
          path: 'statistics',
          name: 'AdminStatistics',
          component: AdminStatistics
        },
        {
          path: 'settings',
          name: 'AdminSettings',
          component: AdminSettings
        },
        {
          path: 'profile',
          name: 'AdminProfile',
          component: AdminProfile
        }
      ]
    },
    // 维修人员路由
    {
      path: '/repairman',
      component: () => import('@/layouts/RepairmanLayout.vue'),
      meta: { requiresAuth: true, role: 'REPAIRMAN' },
      children: [
        {
          path: 'dashboard',
          name: 'RepairmanDashboard',
          component: RepairmanDashboard
        },
        {
          path: 'assignments',
          name: 'RepairmanAssignments',
          component:RepairmanAssignment
        },
        {
          path: 'orders',
          name: 'RepairmanOrders',
          component: RepairmanOrders
        },
        {
          path: 'profile',
          name: 'RepairmanProfile',
          component: RepairmanProfile
        }
      ]
    },

  ]
});

// 路由守卫
router.beforeEach((to, from, next) => {
  next()
});

export default router;