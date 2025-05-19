<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h1>车辆维修管理系统</h1>
        <p>请登录以继续</p>
      </div>
      <form class="login-form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label class="form-label">用户名</label>
          <input
            type="text"
            class="form-input"
            v-model="form.username"
            placeholder="请输入用户名"
            required
          >
        </div>
        <div class="form-group">
          <label class="form-label">密码</label>
          <input
            type="password"
            class="form-input"
            v-model="form.password"
            placeholder="请输入密码"
            required
          >
        </div>
        <div class="form-group">
          <label class="form-label">角色</label>
          <select class="form-input" v-model="form.role" required>
            <option value="">请选择角色</option>
            <option value="user">用户</option>
            <option value="mechanic">维修人员</option>
            <option value="admin">系统管理员</option>
          </select>
        </div>
        <button type="submit" class="btn btn-primary login-btn">
          登录
        </button>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      form: {
        username: '',
        password: '',
        role: ''
      }
    }
  },
  methods: {
    handleLogin() {
      // 这里实现登录逻辑
      console.log('登录信息：', this.form)
      // 模拟登录成功
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #2c3e50 0%, #3498db 100%);
  padding: var(--spacing-md);
}

.login-card {
  background-color: var(--background-white);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  padding: var(--spacing-xl);
  width: 100%;
  max-width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: var(--spacing-xl);
}

.login-header h1 {
  color: var(--text-primary);
  font-size: 1.8rem;
  margin-bottom: var(--spacing-xs);
}

.login-header p {
  color: var(--text-secondary);
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.login-btn {
  margin-top: var(--spacing-md);
  width: 100%;
  padding: var(--spacing-md);
  font-size: 1.1rem;
}

/* 响应式调整 */
@media (max-width: 480px) {
  .login-card {
    padding: var(--spacing-lg);
  }
  
  .login-header h1 {
    font-size: 1.5rem;
  }
}
</style>