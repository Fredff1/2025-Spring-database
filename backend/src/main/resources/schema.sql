DROP TABLE IF EXISTS `labor_fee_log`;

DROP TABLE IF EXISTS `repairman_profile`;

DROP TABLE IF EXISTS `repair_record`;

DROP TABLE IF EXISTS `material_usage`;

DROP TABLE IF EXISTS `feedback`;

DROP TABLE IF EXISTS `assignment`;

DROP TABLE IF EXISTS `repair_order`;

DROP TABLE IF EXISTS `vehicle`;

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  user_id    BIGINT      NOT NULL AUTO_INCREMENT,
  username   VARCHAR(50) NOT NULL,
  role       VARCHAR(20) NOT NULL,
  password   VARCHAR(100) NOT NULL,       
  email      VARCHAR(100) NOT NULL,
  phone      VARCHAR(20),
  status     VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
  created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
                                ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_user_username` (`username`),
  UNIQUE KEY `uk_user_email`    (`email`),
  INDEX idx_username(username),
  CHECK (status IN ('ACTIVE','DISABLED','LOCKED')),
  CHECK (role IN ('CUSTOMER','REPAIRMAN','ADMIN'))
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4;


CREATE TABLE `repairman_profile` (
  user_id           BIGINT PRIMARY KEY,
  specialty         VARCHAR(30),
  hourly_money_rate DECIMAL(10,2),
  repairman_number  VARCHAR(50) NOT NULL, 
  CHECK (specialty IN ('MAINTENANCE','REPAIR','PAINT','TIRE','ELECTRICAL','BODYWORK','ENGINE','OTHER')),
  CONSTRAINT fk_rm_user FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
);


CREATE TABLE `vehicle` (
  vehicle_id    BIGINT    NOT NULL AUTO_INCREMENT,
  owner_id      BIGINT      NOT NULL,
  brand         VARCHAR(100) NOT NULL,
  model         VARCHAR(100) NOT NULL,
  license_plate VARCHAR(20),
  register_date DATE         NOT NULL,
  PRIMARY KEY (`vehicle_id`),
  INDEX idx_vehicle_owner(owner_id),
  CONSTRAINT fk_vehicle_owner FOREIGN KEY(owner_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `repair_order` (
  order_id    BIGINT      NOT NULL AUTO_INCREMENT,
  user_id     BIGINT      NOT NULL,
  vehicle_id  BIGINT    NOT NULL,
  description TEXT ,
  total_fee    DECIMAL(10,2),
  submit_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  status      VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  fault_type  VARCHAR(20) NOT NULL DEFAULT 'OTHER',
  is_paid     BOOLEAN     NOT NULL DEFAULT FALSE,
  PRIMARY KEY(order_id),
  INDEX idx_order_user(user_id),
  INDEX idx_order_vehicle(vehicle_id),
  CHECK (fault_type IN ('MAINTENANCE','REPAIR','PAINT','TIRE','ELECTRICAL','BODYWORK','ENGINE','OTHER')),
  CONSTRAINT fk_order_user    FOREIGN KEY(user_id)    REFERENCES users(user_id) ON DELETE CASCADE,
  CONSTRAINT fk_order_vehicle FOREIGN KEY(vehicle_id) REFERENCES vehicle(vehicle_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;








CREATE TABLE `repair_record` (
  repair_record_id BIGINT      NOT NULL AUTO_INCREMENT,
  order_id         BIGINT      NOT NULL,
  repairman_id     BIGINT      NOT NULL,
  fault_description TEXT       NOT NULL,
  repair_result     TEXT,
  order_status      VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  actual_work_hours DECIMAL(5,2),
  completion_time   DATETIME,
  PRIMARY KEY(repair_record_id),
  INDEX idx_rr_order(order_id),
  CONSTRAINT fk_rr_tech FOREIGN KEY(repairman_id) REFERENCES users(user_id) ON DELETE CASCADE,
  CONSTRAINT fk_rr_order FOREIGN KEY(order_id) REFERENCES repair_order(order_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `material_usage` (
  material_usage_id BIGINT      NOT NULL AUTO_INCREMENT,
  order_id          BIGINT      NOT NULL,
  material_name     VARCHAR(100) NOT NULL,
  quantity          INT         NOT NULL,
  unit_price        DECIMAL(10,2) NOT NULL,
  create_time        DATETIME      NOT NULL,
  PRIMARY KEY(material_usage_id),
  INDEX idx_mu_order(order_id),
  CONSTRAINT fk_mu_order FOREIGN KEY(order_id) REFERENCES repair_order(order_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `feedback` (
  feedback_id   BIGINT      NOT NULL AUTO_INCREMENT,
  order_id      BIGINT      NOT NULL,
  user_id       BIGINT      NOT NULL,
  rating        INT         ,
  feed_back_type VARCHAR(20) NOT NULL DEFAULT 'GENERAL',
  description   TEXT,
  feedback_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  admin_response TEXT, 
  PRIMARY KEY(feedback_id),
  INDEX idx_fb_order(order_id),
  CONSTRAINT fk_fb_order FOREIGN KEY(order_id) REFERENCES repair_order(order_id) ON DELETE CASCADE,
  CONSTRAINT fk_fb_user  FOREIGN KEY(user_id)  REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `assignment` (
  assignment_id        BIGINT      NOT NULL AUTO_INCREMENT,
  order_id         BIGINT      NOT NULL,
  repairman_id    BIGINT      NOT NULL,
  assignment_time    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  assignment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

  PRIMARY KEY(assignment_id),
  INDEX idx_asg_order(order_id),
  INDEX idx_asg_tech(repairman_id),
  CONSTRAINT fk_asg_order FOREIGN KEY(order_id)      REFERENCES repair_order(order_id) ON DELETE CASCADE,
  CONSTRAINT fk_asg_tech  FOREIGN KEY(repairman_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `labor_fee_log` (
  labor_fee_log_id BIGINT      NOT NULL AUTO_INCREMENT,
  order_id         BIGINT      NOT NULL,
  repairman_id    BIGINT      NOT NULL,
  month            VARCHAR(7)  NOT NULL,
  total_hours      DECIMAL(7,2) NOT NULL,
  total_income     DECIMAL(10,2) NOT NULL,
  settle_time      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(labor_fee_log_id),
  INDEX idx_lf_tech(repairman_id),
  CONSTRAINT fk_lf_tech FOREIGN KEY(repairman_id) REFERENCES users(user_id) ON DELETE CASCADE,
  CONSTRAINT fk_lf_order FOREIGN KEY(order_id)      REFERENCES repair_order(order_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


