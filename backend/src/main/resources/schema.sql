DROP TABLE IF EXISTS `labor_fee_log`;

DROP TABLE IF EXISTS `repairman_profile`;

DROP TABLE IF EXISTS `repair_record`;

DROP TABLE IF EXISTS `material_usage`;

DROP TABLE IF EXISTS `feedback`;

DROP TABLE IF EXISTS `assignment`;

DROP TABLE IF EXISTS `repair_order`;

DROP TABLE IF EXISTS `vehicle`;

DROP TABLE IF EXISTS `users`;

-- ============================
-- 表：users（用户信息表）
-- 说明：系统所有用户的基本资料，含账号、角色、状态等
-- ============================
CREATE TABLE `users` (
  user_id    BIGINT      NOT NULL AUTO_INCREMENT,                     --主键，自增
  username   VARCHAR(50) NOT NULL,                                    --用户名，唯一
  role       VARCHAR(20) NOT NULL,                                    --用户角色，分为CUSTOMER, REPAIRMAN, ADMIN
  password   VARCHAR(100) NOT NULL,                                   --密码，存储加密后的值
  email      VARCHAR(100) NOT NULL,                                   --电子邮箱，唯一                    
  phone      VARCHAR(20),                                             --手机号码，唯一
  status     VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',                  --用户状态，分为ACTIVE, DISABLED, LOCKED    
  created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,         --创建时间，默认当前时间
  updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,         --更新时间，默认当前时间
  PRIMARY KEY (`user_id`),                                            
  UNIQUE KEY `uk_user_username` (`username`),                         -- 唯一索引：用户名
  UNIQUE KEY `uk_user_email`    (`email`),                            -- 唯一索引：电子邮箱 
  UNIQUE KEY `uk_user_phone`    (`phone`),                            -- 唯一索引：手机号码
  CHECK (status IN ('ACTIVE','DISABLED','LOCKED')),                   -- 状态约束
  CHECK (role IN ('CUSTOMER','REPAIRMAN','ADMIN'))                    -- 角色约束
) DEFAULT CHARSET = utf8mb4;

-- 创建索引：用于加速通过用户名（username）字段查询用户记录
CREATE INDEX idx_users_username ON users(username);                   

-- ============================
-- 表：repairman_profile（维修工人资料表）
-- 说明：维修工人详细资料，包括专业、收费标准等
-- ============================
CREATE TABLE `repairman_profile` (
  user_id           BIGINT PRIMARY KEY,                               -- 外键，关联users表
  specialty         VARCHAR(30),                                      -- 专业领域，维修工人擅长的领域
  hourly_money_rate DECIMAL(10,2),                                    -- 每小时收费标准
  repairman_number  VARCHAR(50) NOT NULL,                             -- 维修工人编号，唯一 
  UNIQUE KEY `uk_rp_number`    (`repairman_number`),                  -- 唯一索引：维修工人编号
  CHECK (specialty IN ('MAINTENANCE','REPAIR','PAINT','TIRE','ELECTRICAL','BODYWORK','ENGINE','OTHER')),      -- 专业领域约束
  -- 外键约束：user_id 引用 users 表的主键，用户删除时自动删除其维修工资料（级联删除）
  CONSTRAINT fk_rm_user FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE        
);

-- ============================
-- 表：vehicle（车辆信息表）
-- 说明：车辆基本信息，包括车主、品牌、型号等
-- ============================
CREATE TABLE `vehicle` (
  vehicle_id    BIGINT    NOT NULL AUTO_INCREMENT,                -- 主键，自增
  owner_id      BIGINT      NOT NULL,                             -- 外键，关联用户表                   
  brand         VARCHAR(100) NOT NULL,                            -- 车辆品牌
  model         VARCHAR(100) NOT NULL,                            -- 车辆型号                 
  license_plate VARCHAR(20),                                      -- 车牌号，唯一
  register_date DATE         NOT NULL,                            -- 注册日期
  PRIMARY KEY (`vehicle_id`),                                 
  UNIQUE KEY `uk_vehicle_plate`    (`license_plate`),             -- 唯一索引：车牌号
  -- 外键约束：owner_id 引用 users 表的主键，用户删除时自动删除其车辆记录（级联删除）
  CONSTRAINT fk_vehicle_owner FOREIGN KEY(owner_id) REFERENCES users(user_id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4;

-- 创建索引：用于加速通过车主ID（owner_id）查询车辆记录
CREATE INDEX idx_vehicle_owner ON vehicle(owner_id);

-- ============================
-- 表：repair_order（维修订单表）
-- 说明：维修订单的详细信息，包括用户、车辆、描述、费用等
-- ============================
CREATE TABLE `repair_order` (
  order_id    BIGINT      NOT NULL AUTO_INCREMENT,                            -- 主键，自增
  user_id     BIGINT      NOT NULL,                                           -- 外键，关联用户表
  vehicle_id  BIGINT    NOT NULL,                                             -- 外键，关联车辆表
  description TEXT ,                                                          -- 维修描述
  total_fee    DECIMAL(10,2),                                                 -- 订单总费用
  submit_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,                 -- 提交时间，默认当前时间
  update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,                 -- 更新时间，默认当前时间                  
  status      VARCHAR(20) NOT NULL DEFAULT 'PENDING',                         -- 订单状态，分为PENDING, IN_PROGRESS, COMPLETED, CANCELLED
  fault_type  VARCHAR(20) NOT NULL DEFAULT 'OTHER',                           -- 故障类型
  is_paid     BOOLEAN     NOT NULL DEFAULT FALSE,                             -- 是否已支付，默认未支付
  PRIMARY KEY(order_id),
  CHECK (fault_type IN ('MAINTENANCE','REPAIR','PAINT','TIRE','ELECTRICAL','BODYWORK','ENGINE','OTHER')),   -- 故障类型约束
  -- 外键约束：user_id 引用 users 表的主键，用户删除时自动删除其维修订单（级联删除）
  CONSTRAINT fk_order_user    FOREIGN KEY(user_id)    REFERENCES users(user_id) ON DELETE CASCADE,
  -- 外键约束：vehicle_id 引用 vehicle 表的主键，车辆删除时自动删除其维修订单（级联删除）
  CONSTRAINT fk_order_vehicle FOREIGN KEY(vehicle_id) REFERENCES vehicle(vehicle_id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4;

-- 创建索引：用于加速通过用户ID（user_id）和车辆ID（vehicle_id）查询维修订单记录
CREATE INDEX idx_repair_order_user ON repair_order(user_id);
CREATE INDEX idx_repair_order_vehicle ON repair_order(vehicle_id);

-- ============================
-- 表：repair_record（维修记录表）
-- 说明：维修工人对订单的具体维修记录，包括故障描述、维修结果、工时等
-- ============================
CREATE TABLE `repair_record` (
  repair_record_id BIGINT      NOT NULL AUTO_INCREMENT,           -- 主键，自增
  order_id         BIGINT      NOT NULL,                          -- 外键，关联维修订单表
  repairman_id     BIGINT      NOT NULL,                          -- 外键，关联维修工人表
  fault_description TEXT       NOT NULL,                          -- 故障描述
  repair_result     TEXT,                                         -- 维修结果
  order_status      VARCHAR(20) NOT NULL DEFAULT 'PENDING',       -- 订单状态
  actual_work_hours DECIMAL(5,2),                                 -- 实际工时                      
  completion_time   DATETIME,                                     -- 完成时间
  PRIMARY KEY(repair_record_id),
  -- 外键约束：repairman_id 引用 users 表的主键，维修工人删除时自动删除其维修记录（级联删除）
  CONSTRAINT fk_rr_tech FOREIGN KEY(repairman_id) REFERENCES users(user_id) ON DELETE CASCADE,
  -- 外键约束：order_id 引用 repair_order 表的主键，订单删除时自动删除其维修记录（级联删除）
  CONSTRAINT fk_rr_order FOREIGN KEY(order_id) REFERENCES repair_order(order_id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4;

-- 创建索引：用于加速通过订单ID（order_id）和维修工人ID（repairman_id）查询维修记录
CREATE INDEX idx_repair_record_order ON repair_record(order_id);
CREATE INDEX idx_repair_record_repairman ON repair_record(repairman_id);

-- ============================
-- 表：material_usage（材料使用记录表）
-- 说明：维修订单中使用的材料记录，包括材料名称、数量、单价等
-- ============================
CREATE TABLE `material_usage` (
  material_usage_id BIGINT      NOT NULL AUTO_INCREMENT,            -- 主键，自增                  
  submitter_id      BIGINT      NOT NULL,                           -- 外键，关联用户表
  order_id          BIGINT      NOT NULL,                           -- 外键，关联维修订单表
  material_name     VARCHAR(100) NOT NULL,                          -- 材料名称
  quantity          INT         NOT NULL,                           -- 使用数量
  unit_price        DECIMAL(10,2) NOT NULL,                         -- 单价                
  create_time        DATETIME      NOT NULL,                        -- 创建时间，默认当前时间
  PRIMARY KEY(material_usage_id),
  -- 外键约束：order_id 引用 repair_order 表的主键，订单删除时自动删除其材料使用记录（级联删除）
  CONSTRAINT fk_mu_order FOREIGN KEY(order_id) REFERENCES repair_order(order_id) ON DELETE CASCADE,
  -- 外键约束：submitter_id 引用 users 表的主键，用户删除时自动删除其材料使用记录（级联删除）
  CONSTRAINT fk_mu_submitter FOREIGN KEY(submitter_id) REFERENCES users(user_id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4;

-- 创建索引：用于加速通过订单ID（order_id）查询材料使用记录
CREATE INDEX idx_material_usage_order ON material_usage(order_id);

-- ============================
-- 表：feedback（用户反馈表）
-- 说明：用户对维修服务的反馈，包括评分、类型、描述等
-- ============================
CREATE TABLE `feedback` (
  feedback_id   BIGINT      NOT NULL AUTO_INCREMENT,                          -- 主键，自增
  order_id      BIGINT      NOT NULL,                                         -- 外键，关联维修订单表
  user_id       BIGINT      NOT NULL,                                         -- 外键，关联用户表
  rating        INT         ,                                                 -- 评分，1-5分
  feed_back_type VARCHAR(20) NOT NULL DEFAULT 'GENERAL',                      -- 反馈类型，分为GENERAL, COMPLAINT, SUGGESTION
  description   TEXT,                                                         -- 反馈描述
  feedback_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,               -- 反馈时间，默认当前时间
  response TEXT,                                                              -- 维修工人或管理员的回复
  PRIMARY KEY(feedback_id),
  -- 外键约束：order_id 引用 repair_order 表的主键，订单删除时自动删除其反馈记录（级联删除）
  CONSTRAINT fk_fb_order FOREIGN KEY(order_id) REFERENCES repair_order(order_id) ON DELETE CASCADE,
  -- 外键约束：user_id 引用 users 表的主键，用户删除时自动删除其反馈记录（级联删除）
  CONSTRAINT fk_fb_user  FOREIGN KEY(user_id)  REFERENCES users(user_id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4;

-- 创建索引：用于加速通过订单ID（order_id）和用户ID（user_id）查询反馈记录
CREATE INDEX idx_feedback_order ON feedback(order_id);
CREATE INDEX idx_feedback_user ON feedback(user_id);

-- ============================   
-- 中间表：assignment（维修工人分配表）
-- 说明：维修工人分配到订单的记录，包含分配时间、状态等
-- ============================
CREATE TABLE `assignment` (
  assignment_id        BIGINT      NOT NULL AUTO_INCREMENT,                 -- 主键，自增
  order_id         BIGINT      NOT NULL,                                    -- 外键，关联维修订单表
  repairman_id    BIGINT      NOT NULL,                                     -- 外键，关联维修工人表
  assignment_time    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,        -- 分配时间，默认当前时间
  assignment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',                 -- 分配状态

  PRIMARY KEY(assignment_id),
  -- 外键约束：order_id 引用 repair_order 表的主键，订单删除时自动删除其分配记录（级联删除）
  CONSTRAINT fk_asg_order FOREIGN KEY(order_id)      REFERENCES repair_order(order_id) ON DELETE CASCADE,
  -- 外键约束：repairman_id 引用 users 表的主键，维修工人删除时自动删除其分配记录（级联删除）
  CONSTRAINT fk_asg_tech  FOREIGN KEY(repairman_id) REFERENCES users(user_id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4;

-- 创建索引：用于加速通过订单ID（order_id）和维修工人ID（repairman_id）查询分配记录
CREATE INDEX idx_assignment_order ON assignment(order_id);
CREATE INDEX idx_assignment_repairman ON assignment(repairman_id);

-- ============================   
-- 中间表：labor_fee_log（工时费表）
-- 说明：记录维修工人每月的工时和收入情况
-- ============================
CREATE TABLE `labor_fee_log` (  
  labor_fee_log_id BIGINT      NOT NULL AUTO_INCREMENT,                           -- 主键，自增
  repair_record_id        BIGINT      NOT NULL,                                   -- 外键，关联维修记录表
  order_id         BIGINT      NOT NULL,                                          -- 外键，关联维修订单表
  repairman_id    BIGINT      NOT NULL,                                           -- 外键，关联维修工人表
  month            VARCHAR(7)  NOT NULL,                                          -- 月份，格式为YYYY-MM
  total_hours      DECIMAL(7,2) NOT NULL,                                         -- 总工时
  total_income     DECIMAL(10,2) NOT NULL,                                        -- 总收入                      
  settle_time      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,                -- 结算时间，默认当前时间
  PRIMARY KEY(labor_fee_log_id),
  -- 外键约束：repairman_id 引用 users 表的主键，维修工人删除时自动删除其工时费记录（级联删除）
  CONSTRAINT fk_lf_tech FOREIGN KEY(repairman_id) REFERENCES users(user_id) ON DELETE CASCADE,
  -- 外键约束：order_id 引用 repair_order 表的主键，维修订单删除时自动删除其工时费记录（级联删除）
  CONSTRAINT fk_lf_order FOREIGN KEY(order_id)      REFERENCES repair_order(order_id) ON DELETE CASCADE,
  -- 外键约束：repair_record_id 引用 repair_record 表的主键，维修记录删除时自动删除其工时费记录（级联删除）
  CONSTRAINT fk_lf_record FOREIGN KEY(repair_record_id)      REFERENCES repair_record(repair_record_id) ON DELETE CASCADE
) DEFAULT CHARSET=utf8mb4;

-- 创建索引：用于加速通过维修记录ID（repair_record_id）、订单ID（order_id）和维修工人ID（repairman_id）查询工时费记录
CREATE INDEX idx_labor_fee_log_order ON labor_fee_log(order_id);
CREATE INDEX idx_labor_fee_log_record ON labor_fee_log(repair_record_id);
CREATE INDEX idx_labor_fee_log_repairman ON labor_fee_log(repairman_id);
