import { defineStore } from 'pinia';

/**
 * 用户认证状态管理 Store
 */
export const useAuthStore = defineStore('auth', {
  state: () => ({
    /** JWT Token */
    token: '',
    /** 用户角色 */
    role: '',
    /** 用户名 */
    username: '',
  }),
  actions: {
    /** 重置登录状态 */
    $reset() {
      this.token = '';
      this.role = '';
      this.username = '';
    },
  },
});