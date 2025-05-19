import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/store/modules/user';
import MainLayout from '@/layouts/MainLayout.vue';

const routes = [
  {
    path: '/',
    component: MainLayout,
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue')
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue')
      },
      {
        path: 'repair-orders',
        name: 'RepairOrders',
        component: () => import('@/views/RepairOrders.vue')
      },
      
      {
        path: 'vehicles',
        name: 'Vehicles',
        component: () => import('@/views/Vehicles.vue')
      },
      {
        path: 'inventory',
        name: 'Inventory',
        component: () => import('@/views/Inventory.vue')
      },
      {
        path: 'finance',
        name: 'Finance',
        component: () => import('@/views/Finance.vue')
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('@/views/Statistics.vue')
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { guest: true }
  }
];

const router = createRouter({ history: createWebHistory(), routes });

// 路由守卫：未认证跳转登录
// router.beforeEach((to, from, next) => {
//   const { token } = useAuthStore();
//   if (!token && !to.meta.guest) {
//     next('/login');
//   } else {
//     next();
//   }
// });

export default router;