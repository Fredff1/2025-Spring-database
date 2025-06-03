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
  ('grace',     'REPAIRMAN','$2a$10$bD3ySXDVhoz/HBCGztXgq.dSr3WLWEGlz0LOcl8RmWZ4tsZOsCSvq',   'grace@repairhub.com', '100-200-3004', 'ACTIVE', NOW(), NOW());

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

-- === 3. 车辆（vehicle）示例 ===
INSERT INTO `vehicle` (owner_id, brand, model, license_plate, register_date) VALUES
  (
    (SELECT user_id FROM users WHERE username = 'charlie'),
    'Toyota', 'Corolla', '浙A111AA', '2023-08-15'
  ),
  (
    (SELECT user_id FROM users WHERE username = 'charlie'),
    'Honda',  'Civic',   '浙A222BB', '2022-06-30'
  ),
  (
    (SELECT user_id FROM users WHERE username = 'diana'),
    'Ford',   'Focus',   '沪B333CC', '2021-12-01'
  );

-- === 4. 报修工单（repair_order）示例 ===
-- charlie 针对第一辆车提交了一条 MAINTENANCE 故障工单
INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES
  (
    (SELECT user_id FROM users WHERE username = 'charlie'),
    (SELECT vehicle_id FROM vehicle WHERE license_plate = '浙A111AA'),
    '发动机异响，需要全面检查', 0.00, NOW(), NOW(), 'PENDING', 'MAINTENANCE', FALSE
  );

-- diana 针对自己车辆提交了一条 REPAIR 故障工单
INSERT INTO `repair_order` (
  user_id, vehicle_id, description, total_fee, submit_time, update_time, status, fault_type, is_paid
) VALUES
  (
    (SELECT user_id FROM users WHERE username = 'diana'),
    (SELECT vehicle_id FROM vehicle WHERE license_plate = '沪B333CC'),
    '刹车片磨损，应更换', 0.00, NOW(), NOW(), 'PENDING', 'REPAIR', FALSE
  );

-- === 5. 派单（assignment）示例 ===
-- 为 charlie 的第一个工单派发给 eve、frank、grace 三位技师
-- (assignment_status: PENDING/ACCEPTED/REJECTED)
INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%发动机异响%'),
    (SELECT user_id FROM users WHERE username = 'eve'),
    NOW(), 'PENDING'
  ),
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%发动机异响%'),
    (SELECT user_id FROM users WHERE username = 'frank'),
    NOW(), 'PENDING'
  ),
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%发动机异响%'),
    (SELECT user_id FROM users WHERE username = 'grace'),
    NOW(), 'PENDING'
  );

-- 为 diana 的工单派发给 frank 与 eve 两位技师，其中 frank 已接单，eve 待定
INSERT INTO `assignment` (order_id, repairman_id, assignment_time, assignment_status) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%刹车片磨损%'),
    (SELECT user_id FROM users WHERE username = 'frank'),
    NOW(), 'ACCEPTED'
  ),
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%刹车片磨损%'),
    (SELECT user_id FROM users WHERE username = 'eve'),
    NOW(), 'PENDING'
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
    NOW()
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
    NOW()
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
    NOW()
  );

-- === 7. 材料消耗记录（material_usage）示例 ===
-- charlie 的工单：paint 材料 1 瓶（100.00）
INSERT INTO `material_usage` (order_id, material_name, quantity, unit_price, create_time) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%发动机异响%'),
    '张紧轮', 1, 120.00, NOW()
  ),
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%发动机异响%'),
    '润滑油', 2, 30.00, NOW()
  );

-- diana 的工单：刹车片 1 套（200.00）
INSERT INTO `material_usage` (order_id, material_name, quantity, unit_price, create_time) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%刹车片磨损%'),
    '刹车片套装', 1, 200.00, NOW()
  );

-- === 8. 工时费日志（labor_fee_log）示例 ===
-- charlie 的工单由 grace 完成，用时 2.50h，时薪 75.00，收入合计 187.50
INSERT INTO `labor_fee_log` (order_id, repairman_id, month, total_hours, total_income, settle_time) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%发动机异响%'),
    (SELECT user_id FROM users WHERE username = 'grace'),
    'JUNE', 2.50, 187.50, NOW()
  );

-- diana 的工单由 frank 完成，合计工时 1.50h，时薪 80.00，收入合计 120.00
INSERT INTO `labor_fee_log` (order_id, repairman_id, month, total_hours, total_income, settle_time) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%刹车片磨损%'),
    (SELECT user_id FROM users WHERE username = 'frank'),
    'JUNE', 1, 120.00, NOW()
  );

INSERT INTO `labor_fee_log` (order_id, repairman_id, month, total_hours, total_income, settle_time) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%刹车片磨损%'),
    (SELECT user_id FROM users WHERE username = 'frank'),
    'JUNE', 0.5, 120.00, NOW()
  );

-- === 9. 反馈（feedback）示例 ===
-- charlie 对 grace 的首次维修进行评分
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%发动机异响%'),
    (SELECT user_id FROM users WHERE username = 'charlie'),
    5, 'RATING', '维修速度很快，专业度很高', NOW()
  );

-- diana 对 frank 的维修未完成时催单
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%刹车片磨损%'),
    (SELECT user_id FROM users WHERE username = 'diana'),
    NULL, 'URGENT', '何时可以完成维修？', NOW()
  );

-- diana 对 frank 完成后又做一次一般反馈
INSERT INTO `feedback` (order_id, user_id, rating, feed_back_type, description, feedback_time) VALUES
  (
    (SELECT order_id FROM repair_order WHERE description LIKE '%刹车片磨损%'),
    (SELECT user_id FROM users WHERE username = 'diana'),
    4, 'GENERAL', '维修效果不错，下次再来', NOW()
  );
