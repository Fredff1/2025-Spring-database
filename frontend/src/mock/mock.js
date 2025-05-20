import MockAdapter from 'axios-mock-adapter';
import http from '@/api/http';
import dayjs from 'dayjs';

// const mock = new MockAdapter(http, { delayResponse: 500 });
// // ===== 用户 =====
// mock.onPost('/users/register').reply(200, { success: true, userId: 1001 });
// mock.onPost('/users/login').reply(config => {
//   const { username } = JSON.parse(config.data);
//   return [200, { token: `mock-jwt-token-for-${username}`, role: 'USER' }];
// });
// mock.onGet('/users/profile').reply(200, {
//   userId: 1001,
//   username: '张三',
//   role: 'USER',
//   phone: '13800000000',
//   email: 'zhangsan@example.com',
//   status: 'ACTIVE'
// });
// mock.onGet('/users/orders').reply(200, [{
//   orderId: 5001,
//   vin: 'LXYZ1234567890ABC',
//   status: 'IN_PROGRESS',
//   createdAt: dayjs().subtract(1, 'day').format('YYYY-MM-DD HH:mm')
// }]);
// 其他角色和接口 