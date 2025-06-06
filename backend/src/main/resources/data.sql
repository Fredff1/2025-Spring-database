-- ================================================
-- data.sql —— 示例数据初始化（与代码层初始化互不重合）
-- ================================================

-- === 1. 用户（CUSTOMER 与 REPAIRMAN）示例 ===
INSERT INTO `users` (
  username, role, password, email, phone, status, created_at, updated_at
) VALUES
  ('charlie',   'CUSTOMER', '$2a$10$bD3ySXDVhoz/HBCGztXgq.dSr3WLWEGlz0LOcl8RmWZ4tsZOsCSvq', 'charlie@example.com', '100-200-3000', 'ACTIVE', NOW(), NOW()),
  ('diana',     'CUSTOMER', '$2a$10$bD3ySXDVhoz/HBCGztXgq.dSr3WLWEGlz0LOcl8RmWZ4tsZOsCSvq',   'diana@example.com',   '100-200-3001', 'ACTIVE', NOW(), NOW()),
  ('eve',       'REPAIRMAN','$2a$10$bD3ySXDVhoz/HBCGztXgq.dSr3WLWEGlz0LOcl8RmWZ4tsZOsCSvq',     'eve@repairhub.com',   '100-200-3002', 'ACTIVE', NOW(), NOW()),
  ('frank',     'REPAIRMAN','$2a$10$bD3ySXDVhoz/HBCGztXgq.dSr3WLWEGlz0LOcl8RmWZ4tsZOsCSvq',   'frank@repairhub.com', '100-200-3003', 'ACTIVE', NOW(), NOW()),
  ('grace',     'REPAIRMAN','$2a$10$bD3ySXDVhoz/HBCGztXgq.dSr3WLWEGlz0LOcl8RmWZ4tsZOsCSvq',   'grace@repairhub.com', '100-200-3004', 'ACTIVE', NOW(), NOW()),
  ('henry',     'CUSTOMER', '$2a$10$bD3ySXDVhoz/HBCGztXgq.dSr3WLWEGlz0LOcl8RmWZ4tsZOsCSvq', 'henry@example.com', '100-200-3005', 'ACTIVE', NOW(), NOW()),
  ('irene',     'CUSTOMER', '$2a$10$bD3ySXDVhoz/HBCGztXgq.dSr3WLWEGlz0LOcl8RmWZ4tsZOsCSvq', 'irene@example.com', '100-200-3006', 'ACTIVE', NOW(), NOW()),
  ('jack',      'REPAIRMAN','$2a$10$bD3ySXDVhoz/HBCGztXgq.dSr3WLWEGlz0LOcl8RmWZ4tsZOsCSvq', 'jack@repairhub.com', '100-200-3007', 'ACTIVE', NOW(), NOW()),
  ('kate',      'REPAIRMAN','$2a$10$bD3ySXDVhoz/HBCGztXgq.dSr3WLWEGlz0LOcl8RmWZ4tsZOsCSvq', 'kate@repairhub.com', '100-200-3008', 'ACTIVE', NOW(), NOW()),
  ('lily',      'CUSTOMER', '$2a$10$bD3ySXDVhoz/HBCGztXgq.dSr3WLWEGlz0LOcl8RmWZ4tsZOsCSvq', 'lily@example.com', '100-200-3010', 'ACTIVE', NOW(), NOW()),
  ('mike',      'REPAIRMAN', '$2a$10$bD3ySXDVhoz/HBCGztXgq.dSr3WLWEGlz0LOcl8RmWZ4tsZOsCSvq', 'mike@repairhub.com', '100-200-3011', 'ACTIVE', NOW(), NOW()),
  ('nancy',     'REPAIRMAN', '$2a$10$bD3ySXDVhoz/HBCGztXgq.dSr3WLWEGlz0LOcl8RmWZ4tsZOsCSvq', 'nancy@repairhub.com', '100-200-3012', 'ACTIVE', NOW(), NOW());
-- === 2. 维修人员档案（repairman_profile）示例 ===
-- eve 擅长 MAINTENANCE，时薪 90.00
INSERT INTO `repairman_profile` (user_id, specialty, hourly_money_rate) VALUES
  (
    (SELECT user_id FROM users WHERE username = 'eve'),
    'MAINTENANCE',
    90.00
  );
-- frank 擅长 REPAIR，时薪 80.00
INSERT INTO `repairman_profile` (user_id, specialty, hourly_money_rate) VALUES
  (
    (SELECT user_id FROM users WHERE username = 'frank'),
    'REPAIR',
    80.00
  );
-- grace 擅长 PAINT，时薪 75.00
INSERT INTO `repairman_profile` (user_id, specialty, hourly_money_rate) VALUES
  (
    (SELECT user_id FROM users WHERE username = 'grace'),
    'PAINT',
    75.00
  );
-- jack,mike,nancy 擅长 ELECTRICAL，时薪如下
-- kate 擅长 BODYWORK，时薪 78.00
INSERT INTO `repairman_profile` (user_id, specialty, hourly_money_rate) VALUES
  ((SELECT user_id FROM users WHERE username = 'jack'), 'ELECTRICAL', 85.00),
  ((SELECT user_id FROM users WHERE username = 'kate'), 'BODYWORK', 78.00),
  ((SELECT user_id FROM users WHERE username = 'mike'), 'ELECTRICAL', 88.00),
  ((SELECT user_id FROM users WHERE username = 'nancy'), 'ELECTRICAL', 82.00);


-- === 3. 车辆（vehicle）示例 === sedan即小轿车 suv即多功能 pickup即皮卡 待选包括Van卡车  Hatchback小型车 Coupe 跑车 Convertible敞篷车
INSERT INTO `vehicle` (owner_id, brand, model, license_plate, register_date) VALUES
  ((SELECT user_id FROM users WHERE username = 'charlie'),'Toyota', 'Sedan', '浙A111AA', '2023-08-15'),
  ((SELECT user_id FROM users WHERE username = 'charlie'),'Honda',  'Sedan',   '浙A222BB', '2022-06-30'),
  ((SELECT user_id FROM users WHERE username = 'charlie'),'BrandComplex1', 'SUV', '浙A333CC', DATE('2025-05-26')),
  ((SELECT user_id FROM users WHERE username = 'charlie'),'BrandComplex3', 'Sedan', '浙A444DD', DATE('2025-05-11')),
  ((SELECT user_id FROM users WHERE username = 'diana'),'Ford',   'SUV',   '沪B333CC', '2021-12-01');

INSERT INTO `vehicle` (owner_id, brand, model, license_plate, register_date) VALUES
  ((SELECT user_id FROM users WHERE username = 'henry'), 'Mazda', 'Pickup', '苏C444DD', '2021-03-10'),
  ((SELECT user_id FROM users WHERE username = 'henry'),'BrandComplex4', 'SUV', '苏C555EE', DATE('2025-05-27')),
  ((SELECT user_id FROM users WHERE username = 'irene'), 'BMW', 'Sedan', '粤D555EE', '2020-09-25'),
  ((SELECT user_id FROM users WHERE username = 'irene'),'BrandComplex0', 'SUV', '豫Y000XX', DATE('2025-05-08')),
  ((SELECT user_id FROM users WHERE username = 'lily'), 'Nissan', 'SUV', '湘A666FF', '2021-11-11'),
  ((SELECT user_id FROM users WHERE username = 'lily'),'BrandComplex2', 'Pickup', '豫Y2222XX', DATE('2025-05-07'));


-- === 4. 报修工单（repair_order）示例 ===
-- charlie 针对第一辆车提交了一条 MAINTENANCE 故障工单.一共提交了三份工单
INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES
  (
    (SELECT user_id FROM users WHERE username = 'charlie'),
    (SELECT vehicle_id FROM vehicle WHERE license_plate = '浙A111AA'),
    '发动机异响，需要全面检查', 0.00, '2025-05-08 11:15:07', '2025-05-08 11:15:07', 'PENDING', 'MAINTENANCE', FALSE
  );

INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES (
  (SELECT user_id FROM users WHERE username = 'charlie'),
  (SELECT vehicle_id FROM vehicle WHERE license_plate = '浙A333CC'),
  '轮胎鼓包', 0.00, '2025-05-26 11:15:07', '2025-05-26 11:15:07', 'PENDING', 'REPAIR', FALSE
);

INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES (
  (SELECT user_id FROM users WHERE username = 'charlie'),
  (SELECT vehicle_id FROM vehicle WHERE license_plate = '浙A444DD'),
  '车窗升降失灵', 0.00, '2025-05-11 11:15:07', '2025-05-11 11:15:07', 'PENDING', 'BODYWORK', FALSE
);

INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES
  (
    (SELECT user_id FROM users WHERE username = 'charlie'),
    (SELECT vehicle_id FROM vehicle WHERE license_plate = '浙A111AA'),
    '常规保养浙A111AA', 0.00, '2025-06-04 11:15:07', '2025-06-04 11:15:07', 'PENDING', 'MAINTENANCE', FALSE
  );

INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES (
  (SELECT user_id FROM users WHERE username = 'charlie'),
  (SELECT vehicle_id FROM vehicle WHERE license_plate = '浙A333CC'),
  '日常检修浙A333CC', 0.00, '2025-06-04 11:15:07', '2025-06-04 11:15:07', 'PENDING', 'REPAIR', FALSE
);

INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES (
  (SELECT user_id FROM users WHERE username = 'charlie'),
  (SELECT vehicle_id FROM vehicle WHERE license_plate = '浙A444DD'),
  '日常检修浙A444DD', 0.00, '2025-06-04 11:15:07', '2025-06-04 11:15:07', 'PENDING', 'BODYWORK', FALSE
);

-- diana 针对自己车辆提交了一条 REPAIR 故障工单
INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES
  (
    (SELECT user_id FROM users WHERE username = 'diana'),
    (SELECT vehicle_id FROM vehicle WHERE license_plate = '沪B333CC'),
    '刹车片磨损，应更换', 0.00, '2025-04-01 11:11:11', '2025-04-01 11:11:11', 'PENDING', 'REPAIR', FALSE
  );

INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES
  (
    (SELECT user_id FROM users WHERE username = 'diana'),
    (SELECT vehicle_id FROM vehicle WHERE license_plate = '沪B333CC'),
    '刹车片再次磨损，仍需更换', 0.00, '2025-06-06 11:11:11', '2025-06-06 11:11:11', 'PENDING', 'REPAIR', FALSE
  );
  
  -- henry 的 4份故障工单
INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES
  ((SELECT user_id FROM users WHERE username = 'henry'),
   (SELECT vehicle_id FROM vehicle WHERE license_plate = '苏C444DD'),
   '车灯无法点亮，需要电路检查', 0.00, '2025-04-05 11:11:11', '2025-04-05 11:11:11', 'PROCESSING', 'ELECTRICAL', FALSE);

INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES (
  (SELECT user_id FROM users WHERE username = 'henry'),
  (SELECT vehicle_id FROM vehicle WHERE license_plate = '苏C555EE'),
  '车漆开裂，需要喷涂', 0.00, '2025-05-27 11:15:07', '2025-05-27 11:15:07', 'PENDING', 'PAINT', FALSE
);

INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES
  ((SELECT user_id FROM users WHERE username = 'henry'),
   (SELECT vehicle_id FROM vehicle WHERE license_plate = '苏C444DD'),
   '常规保养苏C444DD', 0.00, '2025-06-05 11:11:11', '2025-06-05 11:11:11', 'PENDING', 'ELECTRICAL', FALSE);

INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES (
  (SELECT user_id FROM users WHERE username = 'henry'),
  (SELECT vehicle_id FROM vehicle WHERE license_plate = '苏C555EE'),
  '常规保养苏C555EE', 0.00, '2025-06-27 11:15:07', '2025-06-27 11:15:07', 'PENDING', 'PAINT', FALSE
);

-- irene 的 两份故障工单
INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES
  ((SELECT user_id FROM users WHERE username = 'irene'),
   (SELECT vehicle_id FROM vehicle WHERE license_plate = '粤D555EE'),
   '车门有划痕，需要钣金修复', 0.00, '2025-06-05 11:11:11', '2025-06-05 11:11:11', 'PENDING', 'BODYWORK', FALSE);



INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES (
  (SELECT user_id FROM users WHERE username = 'irene'),
  (SELECT vehicle_id FROM vehicle WHERE license_plate = '豫Y000XX'),
  '电池电量掉得快', 0.00, '2025-05-08 11:15:07', '2025-05-08 11:15:07', 'PENDING', 'ELECTRICAL', FALSE
);

-- lily 的 两份故障工单

INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES (
  (SELECT user_id FROM users WHERE username = 'lily'),
  (SELECT vehicle_id FROM vehicle WHERE license_plate = '豫Y2222XX'),
  '导航系统崩溃', 0.00, '2025-05-07 11:15:07', '2025-05-07 11:15:07', 'PENDING', 'ELECTRICAL', FALSE
);

INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES (
  (SELECT user_id FROM users WHERE username = 'lily'),
  (SELECT vehicle_id FROM vehicle WHERE license_plate = '湘A666FF'),
  '中控系统失灵，仪表盘无响应', 0.00, NOW(), NOW(), 'PENDING', 'ELECTRICAL', FALSE
);



-- === 5. 派单（assignment）示例 ===
-- 为 charlie 的第一个工单派发给 eve、frank、grace 三位技师
-- (assignment_status: PENDING/ACCEPTED/REJECTED)
INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%发动机异响%'),
    (SELECT user_id FROM users WHERE username = 'eve'),
    '2025-05-08 13:15:07', 'PENDING'
  ),
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%发动机异响%'),
    (SELECT user_id FROM users WHERE username = 'frank'),
    '2025-05-08 13:15:07', 'PENDING'
  ),
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%发动机异响%'),
    (SELECT user_id FROM users WHERE username = 'grace'),
    '2025-05-08 13:15:07', 'PENDING'
  );

-- 为 charlie 的第二三四个工单派发给 技师
  INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%常规保养浙A111AA%'),
    (SELECT user_id FROM users WHERE username = 'eve'),
    '2025-05-08 13:15:07', 'ACCEPTED'
  );
   INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%日常检修浙A333CC%'),
    (SELECT user_id FROM users WHERE username = 'frank'),
    '2025-05-08 13:15:07', 'ACCEPTED'
  );
  INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%日常检修浙A444DD%'),
    (SELECT user_id FROM users WHERE username = 'frank'),
    '2025-05-08 13:15:07', 'PENDING'
  );

-- 为 diana 的工单派发给 frank 与 eve 两位技师，其中 frank 已接单，eve 待定
INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%刹车片磨损%'),
    (SELECT user_id FROM users WHERE username = 'frank'),
    '2025-04-02 11:11:11', 'ACCEPTED'
  ),
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%刹车片磨损%'),
    (SELECT user_id FROM users WHERE username = 'eve'),
    '2025-04-02 11:11:11', 'PENDING'
  );

  -- henry 的工单派给 jack（接单）和 kate（待定）
INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%车灯%'),
   (SELECT user_id FROM users WHERE username = 'jack'), '2025-04-06 11:11:11', 'ACCEPTED'),
  ((SELECT order_id FROM repair_order WHERE description LIKE '%车灯%'),
   (SELECT user_id FROM users WHERE username = 'kate'), '2025-04-06 11:11:11', 'PENDING');

INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%车灯%'),
   (SELECT user_id FROM users WHERE username = 'grace'), '2025-06-06 11:11:11', 'ACCEPTED');

INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%常规保养苏C444DD%'),
   (SELECT user_id FROM users WHERE username = 'eve'), '2025-06-06 11:11:11', 'ACCEPTED');

INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%常规保养苏C555EE%'),
   (SELECT user_id FROM users WHERE username = 'eve'), '2025-06-06 11:11:11', 'PENDING');

-- irene 的工单派给 kate（接单）
INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%划痕%'),
   (SELECT user_id FROM users WHERE username = 'kate'), '2025-06-05 12:12:12', 'ACCEPTED');

INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%仪表盘无响应%'),
   (SELECT user_id FROM users WHERE username = 'mike'), NOW(), 'ACCEPTED'),
  ((SELECT order_id FROM repair_order WHERE description LIKE '%仪表盘无响应%'),
   (SELECT user_id FROM users WHERE username = 'nancy'), NOW(), 'PENDING');

-- 第一轮派单失败
INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  ((SELECT order_id FROM repair_order WHERE description = '电池电量掉得快'),
   (SELECT user_id FROM users WHERE username = 'frank'),
   '2025-05-08 11:15:07', 'REJECTED'),
  ((SELECT order_id FROM repair_order WHERE description = '电池电量掉得快'),
   (SELECT user_id FROM users WHERE username = 'jack'),
   '2025-05-08 11:15:07', 'REJECTED');

-- 第二轮派单成功
INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '电池电量掉得快'),
  (SELECT user_id FROM users WHERE username = 'nancy'),
  '2025-05-08 21:15:07', 'ACCEPTED'
);

-- 第一轮派单失败
INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  ((SELECT order_id FROM repair_order WHERE description = '轮胎鼓包'),
   (SELECT user_id FROM users WHERE username = 'nancy'),
   '2025-05-26 11:15:07', 'REJECTED'),
  ((SELECT order_id FROM repair_order WHERE description = '轮胎鼓包'),
   (SELECT user_id FROM users WHERE username = 'kate'),
   '2025-05-26 11:15:07', 'REJECTED');

-- 第二轮派单成功
INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '轮胎鼓包'),
  (SELECT user_id FROM users WHERE username = 'frank'),
  '2025-05-26 21:15:07', 'ACCEPTED'
);

-- 第一轮派单失败
INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  ((SELECT order_id FROM repair_order WHERE description = '导航系统崩溃'),
   (SELECT user_id FROM users WHERE username = 'frank'),
   '2025-05-07 11:15:07', 'REJECTED'),
  ((SELECT order_id FROM repair_order WHERE description = '导航系统崩溃'),
   (SELECT user_id FROM users WHERE username = 'eve'),
   '2025-05-07 11:15:07', 'REJECTED');

-- 第二轮派单成功
INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '导航系统崩溃'),
  (SELECT user_id FROM users WHERE username = 'mike'),
  '2025-05-07 21:15:07', 'ACCEPTED'
);

-- 第一轮派单失败
INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  ((SELECT order_id FROM repair_order WHERE description = '车窗升降失灵'),
   (SELECT user_id FROM users WHERE username = 'grace'),
   '2025-05-11 11:15:07', 'REJECTED'),
  ((SELECT order_id FROM repair_order WHERE description = '车窗升降失灵'),
   (SELECT user_id FROM users WHERE username = 'frank'),
   '2025-05-11 11:15:07', 'REJECTED');

-- 第二轮派单成功
INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '车窗升降失灵'),
  (SELECT user_id FROM users WHERE username = 'nancy'),
  '2025-05-11 21:15:07', 'ACCEPTED'
);

-- 第一轮派单失败
INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  ((SELECT order_id FROM repair_order WHERE description = '车漆开裂，需要喷涂'),
   (SELECT user_id FROM users WHERE username = 'kate'),
   '2025-05-27 11:15:07', 'REJECTED'),
  ((SELECT order_id FROM repair_order WHERE description = '车漆开裂，需要喷涂'),
   (SELECT user_id FROM users WHERE username = 'nancy'),
   '2025-05-27 11:15:07', 'REJECTED');

-- 第二轮派单成功
INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '车漆开裂，需要喷涂'),
  (SELECT user_id FROM users WHERE username = 'eve'),
  '2025-05-27 21:15:07', 'ACCEPTED'
);

-- === 6. 维修记录（repair_record）示例 ===
-- 假设 eve 拒绝了 charlie 的工单并未插入记录；frank 拒绝，grace 接单并完成一次维修
INSERT INTO `repair_record` (
  order_id, repairman_id, fault_description, repair_result, order_status, actual_work_hours, completion_time
) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%发动机异响%'),
    (SELECT user_id FROM users WHERE username = 'grace'),
    '初步检查发现皮带松动，重新张紧并更换张紧轮',
    '更换完成，试车无异响',
    'COMPLETED',
    2.50,
    '2025-05-09 12:15:07'
  );

-- diana 的工单由 frank 已接单并完成第一步维修
INSERT INTO `repair_record` (
  order_id, repairman_id, fault_description, repair_result, order_status, actual_work_hours, completion_time
) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%刹车片磨损%'),
    (SELECT user_id FROM users WHERE username = 'frank'),
    '更换前刹车片，发现刹车油不足，已补充',
    '刹车片安装并测试正常',
    'PROCESSING',
    1.00,
    '2025-04-03 11:11:11'
  );

-- 同一工单第二次检查并完成，状态改为 COMPLETED
INSERT INTO `repair_record` (
  order_id, repairman_id, fault_description, repair_result, order_status, actual_work_hours, completion_time
) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%刹车片磨损%'),
    (SELECT user_id FROM users WHERE username = 'frank'),
    '复检后刹车性能正常',
    '维修完成并试车合格',
    'COMPLETED',
    0.50,
    '2025-04-04 11:11:11'
  );

-- jack 完成 henry 的电路维修
INSERT INTO `repair_record` (
  order_id, repairman_id, fault_description, repair_result, order_status, actual_work_hours, completion_time
) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%车灯%'),
   (SELECT user_id FROM users WHERE username = 'jack'),
   '检查发现保险丝熔断，更换保险丝后恢复正常',
   '测试完成，车灯工作正常',
   'COMPLETED',
   1.5,
   '2025-04-07 11:11:11');

-- kate 完成 irene 的钣金修复
INSERT INTO `repair_record` (
  order_id, repairman_id, fault_description, repair_result, order_status, actual_work_hours, completion_time
) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%划痕%'),
   (SELECT user_id FROM users WHERE username = 'kate'),
   '打磨划痕区域，喷漆修复，抛光',
   '车门表面恢复光亮如新',
   'COMPLETED',
   3.0,
   '2025-06-06 10:00:00');

INSERT INTO `repair_record` (
  order_id, repairman_id, fault_description, repair_result, order_status, actual_work_hours, completion_time
) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%仪表盘无响应%'),
   (SELECT user_id FROM users WHERE username = 'mike'),
   '初步检查发现主电源模块断电，尝试更换保险丝',
   '问题未完全解决，仍有部分模块无响应',
   'PROCESSING', 1.5, NOW());

INSERT INTO `repair_record` (
  order_id, repairman_id, fault_description, repair_result, order_status, actual_work_hours, completion_time
) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%仪表盘无响应%'),
   (SELECT user_id FROM users WHERE username = 'mike'),
   '更换控制单元，更新固件，全部模块恢复正常',
   '问题彻底解决',
   'COMPLETED', 2.0, NOW());

INSERT INTO `repair_record` (
  order_id, repairman_id, fault_description, repair_result, order_status, actual_work_hours, completion_time
) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '电池电量掉得快'),
  (SELECT user_id FROM users WHERE username = 'nancy'),
  '电池电量掉得快 检查后完成维修',
  '问题已修复，测试通过',
  'COMPLETED',
  2.5,
  '2025-05-08 23:45:07'
);

INSERT INTO `repair_record` (
  order_id, repairman_id, fault_description, repair_result, order_status, actual_work_hours, completion_time
) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '轮胎鼓包'),
  (SELECT user_id FROM users WHERE username = 'frank'),
  '轮胎鼓包 检查后完成维修',
  '问题已修复，测试通过',
  'COMPLETED',
  2.5,
  '2025-05-26 23:45:07'
);

INSERT INTO `repair_record` (
  order_id, repairman_id, fault_description, repair_result, order_status, actual_work_hours, completion_time
) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '导航系统崩溃'),
  (SELECT user_id FROM users WHERE username = 'mike'),
  '导航系统崩溃 检查后完成维修',
  '问题已修复，测试通过',
  'COMPLETED',
  2.5,
  '2025-05-07 23:45:07'
);

INSERT INTO `repair_record` (
  order_id, repairman_id, fault_description, repair_result, order_status, actual_work_hours, completion_time
) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '车窗升降失灵'),
  (SELECT user_id FROM users WHERE username = 'nancy'),
  '车窗升降失灵 检查后完成维修',
  '问题已修复，测试通过',
  'COMPLETED',
  2.5,
  '2025-05-11 23:45:07'
);

INSERT INTO `repair_record` (
  order_id, repairman_id, fault_description, repair_result, order_status, actual_work_hours, completion_time
) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '车漆开裂，需要喷涂'),
  (SELECT user_id FROM users WHERE username = 'eve'),
  '车漆开裂，需要喷涂 检查后完成维修',
  '问题已修复，测试通过',
  'COMPLETED',
  2.5,
  '2025-05-27 23:45:07'
);

-- 同步更新订单状态
UPDATE repair_order
SET status = 'COMPLETED'
WHERE description IN (
  '发动机异响，需要全面检查',
  '轮胎鼓包',
  '车窗升降失灵',
  '刹车片磨损，应更换',
  '车灯无法点亮，需要电路检查',
  '车门有划痕，需要钣金修复',
  '电池电量掉得快',
  '导航系统崩溃',
  '中控系统失灵，仪表盘无响应',
  '车漆开裂，需要喷涂'
);

UPDATE repair_order
SET status = 'PROCESSING'
WHERE description IN (
  '刹车片再次磨损，仍需更换',
  '常规保养浙A111AA',
  '日常检修浙A333CC'
);
UPDATE repair_order
SET status = 'PENDING'
WHERE description IN (
  '日常检修浙A444DD',
  '常规保养苏C555EE'

);


-- === 7. 材料消耗记录（material_usage）示例 ===
-- charlie 的工单：paint 材料 1 瓶（100.00）
INSERT INTO `material_usage` (order_id, material_name, quantity, unit_price, create_time) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%发动机异响%'),
    '张紧轮', 1, 120.00, '2025-05-09 12:15:07'
  ),
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%发动机异响%'),
    '润滑油', 2, 30.00, '2025-05-09 12:15:07'
  );

-- diana 的工单：刹车片 1 套（200.00）
INSERT INTO `material_usage` (order_id, material_name, quantity, unit_price, create_time) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%刹车片磨损%'),
    '刹车片套装', 1, 200.00, '2025-04-04 11:11:11'
  );

-- henry 的工单：保险丝 1 个（15.00）
INSERT INTO `material_usage` (order_id, material_name, quantity, unit_price, create_time) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%车灯%'),
   '保险丝', 1, 15.00, '2025-04-06 11:11:11');

-- irene 的工单：喷漆材料 1 套（250.00）
INSERT INTO `material_usage` (order_id, material_name, quantity, unit_price, create_time) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%划痕%'),
   '喷漆材料', 1, 250.00, '2025-06-06 12:12:12');

INSERT INTO `material_usage` (order_id, material_name, quantity, unit_price, create_time) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%仪表盘无响应%'),
   '主控制单元', 1, 300.00, NOW()),
  ((SELECT order_id FROM repair_order WHERE description LIKE '%仪表盘无响应%'),
   '保险丝', 2, 15.00, NOW());
INSERT INTO `material_usage` (order_id, material_name, quantity, unit_price, create_time) VALUES
  ((SELECT order_id FROM repair_order WHERE description = '电池电量掉得快'),
   '复合组件0', 1, 160, '2025-05-08 23:45:07'),
  ((SELECT order_id FROM repair_order WHERE description = '电池电量掉得快'),
   '维修材料', 2, 30.00, '2025-05-08 23:45:07');

INSERT INTO `material_usage` (order_id, material_name, quantity, unit_price, create_time) VALUES
  ((SELECT order_id FROM repair_order WHERE description = '轮胎鼓包'),
   '复合组件1', 1, 165, '2025-05-26 23:45:07'),
  ((SELECT order_id FROM repair_order WHERE description = '轮胎鼓包'),
   '维修材料', 2, 30.00, '2025-05-26 23:45:07');

INSERT INTO `material_usage` (order_id, material_name, quantity, unit_price, create_time) VALUES
  ((SELECT order_id FROM repair_order WHERE description = '导航系统崩溃'),
   '复合组件2', 1, 170, '2025-05-07 23:45:07'),
  ((SELECT order_id FROM repair_order WHERE description = '导航系统崩溃'),
   '维修材料', 2, 30.00, '2025-05-07 23:45:07');

INSERT INTO `material_usage` (order_id, material_name, quantity, unit_price, create_time) VALUES
  ((SELECT order_id FROM repair_order WHERE description = '车窗升降失灵'),
   '复合组件3', 1, 175, '2025-05-11 23:45:07'),
  ((SELECT order_id FROM repair_order WHERE description = '车窗升降失灵'),
   '维修材料', 2, 30.00, '2025-05-11 23:45:07');

INSERT INTO `material_usage` (order_id, material_name, quantity, unit_price, create_time) VALUES
  ((SELECT order_id FROM repair_order WHERE description = '车漆开裂，需要喷涂'),
   '复合组件4', 1, 180, '2025-05-27 23:45:07'),
  ((SELECT order_id FROM repair_order WHERE description = '车漆开裂，需要喷涂'),
   '维修材料', 2, 30.00, '2025-05-27 23:45:07');

-- === 8. 工时费日志（labor_fee_log）示例 ===
-- charlie 的工单由 grace 完成，用时 2.50h，时薪 75.00，收入合计 187.50
INSERT INTO `labor_fee_log` (order_id, repairman_id, month, total_hours, total_income, settle_time) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%发动机异响%'),
    (SELECT user_id FROM users WHERE username = 'grace'),
    'JUNE', 2.50, 187.50, '2025-05-09 12:15:07'
  );

-- diana 的工单由 frank 完成，合计工时 1.50h，时薪 80.00，收入合计 120.00
INSERT INTO `labor_fee_log` (order_id, repairman_id, month, total_hours, total_income, settle_time) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%刹车片磨损%'),
    (SELECT user_id FROM users WHERE username = 'frank'),
    'JUNE', 1, 120.00, '2025-04-04 11:11:11'
  );

INSERT INTO `labor_fee_log` (order_id, repairman_id, month, total_hours, total_income, settle_time) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%刹车片磨损%'),
    (SELECT user_id FROM users WHERE username = 'frank'),
    'JUNE', 0.5, 120.00, NOW()
  );

INSERT INTO `labor_fee_log` (order_id, repairman_id, month, total_hours, total_income, settle_time) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%车灯%'),
   (SELECT user_id FROM users WHERE username = 'jack'),
   'JUNE', 1.5, 127.50, NOW());

INSERT INTO `labor_fee_log` (order_id, repairman_id, month, total_hours, total_income, settle_time) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%划痕%'),
   (SELECT user_id FROM users WHERE username = 'kate'),
   'JUNE', 3.0, 234.00, NOW());

INSERT INTO `labor_fee_log` (order_id, repairman_id, month, total_hours, total_income, settle_time) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%仪表盘无响应%'),
   (SELECT user_id FROM users WHERE username = 'mike'),
   'JUNE', 3.5, 308.00, NOW());

INSERT INTO `labor_fee_log` (
  order_id, repairman_id, month, total_hours, total_income, settle_time
) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '电池电量掉得快'),
  (SELECT user_id FROM users WHERE username = 'nancy'),
  'JUNE', 2.5, 187.5, '2025-05-08 23:45:07'
);

INSERT INTO `labor_fee_log` (
  order_id, repairman_id, month, total_hours, total_income, settle_time
) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '轮胎鼓包'),
  (SELECT user_id FROM users WHERE username = 'frank'),
  'JUNE', 2.5, 193.75, '2025-05-26 23:45:07'
);

INSERT INTO `labor_fee_log` (
  order_id, repairman_id, month, total_hours, total_income, settle_time
) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '导航系统崩溃'),
  (SELECT user_id FROM users WHERE username = 'mike'),
  'JUNE', 2.5, 200.0, '2025-05-07 23:45:07'
);

INSERT INTO `labor_fee_log` (
  order_id, repairman_id, month, total_hours, total_income, settle_time
) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '车窗升降失灵'),
  (SELECT user_id FROM users WHERE username = 'nancy'),
  'JUNE', 2.5, 206.25, '2025-05-11 23:45:07'
);

INSERT INTO `labor_fee_log` (
  order_id, repairman_id, month, total_hours, total_income, settle_time
) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '车漆开裂，需要喷涂'),
  (SELECT user_id FROM users WHERE username = 'eve'),
  'JUNE', 2.5, 212.5, '2025-05-27 23:45:07'
);

-- === 9. 反馈（feedback）示例 ===
-- charlie 对 grace 的首次维修进行评分
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%发动机异响%'),
    (SELECT user_id FROM users WHERE username = 'charlie'),
    5, 'RATING', '维修速度很快，专业度很高', '2025-05-09 16:00:00'
  );

-- diana 对 frank 的维修未完成时催单
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%刹车片磨损%'),
    (SELECT user_id FROM users WHERE username = 'diana'),
    3, 'URGENT', '何时可以完成维修？', '2025-04-03 16:00:00'
  );

-- diana 对 frank 完成后又做一次一般反馈
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%刹车片磨损%'),
    (SELECT user_id FROM users WHERE username = 'diana'),
    4, 'GENERAL', '维修效果不错，下次再来', '2025-04-05 11:11:11'
  );

-- henry 对 jack 评价
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%车灯%'),
   (SELECT user_id FROM users WHERE username = 'henry'),
   5, 'RATING', '修得很快，非常专业！', NOW());

-- irene 对 kate 一般反馈
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%划痕%'),
   (SELECT user_id FROM users WHERE username = 'irene'),
   4, 'GENERAL', '效果不错，但稍微有点慢。', '2025-06-06 14:00:00');

INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%仪表盘无响应%'),
   (SELECT user_id FROM users WHERE username = 'lily'), 3, 'URGENT', '什么时候能修好仪表盘？', NOW());

INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES
  ((SELECT order_id FROM repair_order WHERE description LIKE '%仪表盘无响应%'),
   (SELECT user_id FROM users WHERE username = 'lily'), 5, 'RATING', '全部功能恢复，服务很好！', NOW());

-- 催单反馈
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '电池电量掉得快'),
  (SELECT user_id FROM users WHERE username = 'irene'),
  3, 'URGENT', '技师长时间未响应，请尽快处理', '2025-05-08 15:15:07'
);

-- 完成后评分反馈
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '电池电量掉得快'),
  (SELECT user_id FROM users WHERE username = 'irene'),
  4, 'RATING', '过程虽有延误，修好即可', '2025-05-08 23:45:07'
);

-- 催单反馈
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '轮胎鼓包'),
  (SELECT user_id FROM users WHERE username = 'charlie'),
  3, 'URGENT', '技师长时间未响应，请尽快处理', '2025-05-26 15:15:07'
);

-- 完成后评分反馈
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '轮胎鼓包'),
  (SELECT user_id FROM users WHERE username = 'charlie'),
  4, 'RATING', '过程虽有延误，修好即可', '2025-05-26 23:45:07'
);

-- 催单反馈
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '导航系统崩溃'),
  (SELECT user_id FROM users WHERE username = 'lily'),
  3, 'URGENT', '技师长时间未响应，请尽快处理', '2025-05-07 15:15:07'
);

-- 完成后评分反馈
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '导航系统崩溃'),
  (SELECT user_id FROM users WHERE username = 'lily'),
  4, 'RATING', '过程虽有延误，修好即可', '2025-05-07 23:45:07'
);

-- 催单反馈
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '车窗升降失灵'),
  (SELECT user_id FROM users WHERE username = 'charlie'),
  3, 'URGENT', '技师长时间未响应，请尽快处理', '2025-05-11 15:15:07'
);

-- 完成后评分反馈
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '车窗升降失灵'),
  (SELECT user_id FROM users WHERE username = 'charlie'),
  4, 'RATING', '过程虽有延误，修好即可', '2025-05-11 23:45:07'
);

-- 催单反馈
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '车漆开裂，需要喷涂'),
  (SELECT user_id FROM users WHERE username = 'henry'),
  3, 'URGENT', '技师长时间未响应，请尽快处理', '2025-05-27 15:15:07'
);

-- 完成后评分反馈
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES (
  (SELECT order_id FROM repair_order WHERE description = '车漆开裂，需要喷涂'),
  (SELECT user_id FROM users WHERE username = 'henry'),
  4, 'RATING', '过程虽有延误，修好即可', '2025-05-27 23:45:07'
);