import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import { createPinia } from 'pinia';
import ElementPlus from 'element-plus';
import 'element-plus/theme-chalk/index.css';
import '@/styles/reset.scss';
import '@/styles/variables.scss';
import './assets/main.css';

// mock 数据
import '@/mock/mock';

const app = createApp(App);
app.use(createPinia());
app.use(router);
app.use(ElementPlus);
app.mount('#app');
