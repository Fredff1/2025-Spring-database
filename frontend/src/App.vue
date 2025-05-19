<template>
  <router-view></router-view>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
:root {
  /* 主题色 */
  --primary: #1890ff;
  --primary-dark: #096dd9;
  --primary-light: #e6f7ff;
  
  /* 功能色 */
  --success: #52c41a;
  --success-light: #f6ffed;
  --warning: #faad14;
  --warning-light: #fffbe6;
  --danger: #ff4d4f;
  --danger-light: #fff1f0;
  --info: #1890ff;
  --info-light: #e6f7ff;
  
  /* 中性色 */
  --text-primary: #262626;
  --text-secondary: #595959;
  --text-disabled: #bfbfbf;
  --border: #d9d9d9;
  --background: #f0f2f5;
  --background-light: #fafafa;
  --disabled: #f5f5f5;
  
  /* 间距 */
  --spacing-xs: 4px;
  --spacing-sm: 8px;
  --spacing-md: 16px;
  --spacing-lg: 24px;
  --spacing-xl: 32px;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial,
    'Noto Sans', sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol',
    'Noto Color Emoji';
  font-size: 14px;
  line-height: 1.5;
  color: var(--text-primary);
  background-color: var(--background);
}

a {
  color: var(--primary);
  text-decoration: none;
}

a:hover {
  color: var(--primary-dark);
}

button {
  font-family: inherit;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: var(--background-light);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: var(--border);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: var(--text-disabled);
}
</style>
