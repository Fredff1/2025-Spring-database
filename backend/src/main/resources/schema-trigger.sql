DROP TRIGGER IF EXISTS tr_repair_order_after_insert$$
DROP TRIGGER IF EXISTS tr_repair_order_after_update$$
DROP TRIGGER IF EXISTS tr_repair_order_after_delete$$

DROP TABLE IF EXISTS `repair_order_history`$$

CREATE TABLE `repair_order_history` (
  history_id    BIGINT       NOT NULL AUTO_INCREMENT,
  order_id      BIGINT       NOT NULL,                         
  operation     ENUM('INSERT','UPDATE','DELETE') NOT NULL,    
  changed_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,  
  user_id       BIGINT       NULL,
  vehicle_id    BIGINT       NULL,
  description   TEXT         NULL,
  total_fee     DECIMAL(10,2) NULL,
  submit_time   DATETIME     NULL,
  update_time   DATETIME     NULL,
  status        VARCHAR(20)  NULL,
  fault_type    VARCHAR(20)  NULL,
  is_paid       BOOLEAN      NULL,
  PRIMARY KEY (`history_id`),
  INDEX idx_ro_his_order(order_id),
  INDEX idx_ro_his_time(changed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4$$

CREATE TRIGGER tr_repair_order_after_insert
AFTER INSERT ON `repair_order`
FOR EACH ROW
BEGIN
  INSERT INTO `repair_order_history` (
    order_id, operation, changed_at,
    user_id, vehicle_id, description,
    total_fee, submit_time, update_time,
    status, fault_type, is_paid
  ) VALUES (
    NEW.order_id,
    'INSERT',
    NOW(),
    NEW.user_id,
    NEW.vehicle_id,
    NEW.description,
    NEW.total_fee,
    NEW.submit_time,
    NEW.update_time,
    NEW.status,
    NEW.fault_type,
    NEW.is_paid
  );
END
$$

CREATE TRIGGER tr_repair_order_after_update
AFTER UPDATE ON `repair_order`
FOR EACH ROW
BEGIN
  INSERT INTO `repair_order_history` (
    order_id, operation, changed_at,
    user_id, vehicle_id, description,
    total_fee, submit_time, update_time,
    status, fault_type, is_paid
  ) VALUES (
    NEW.order_id,
    'UPDATE',
    NOW(),
    NEW.user_id,
    NEW.vehicle_id,
    NEW.description,
    NEW.total_fee,
    NEW.submit_time,
    NEW.update_time,
    NEW.status,
    NEW.fault_type,
    NEW.is_paid
  );
END
$$

CREATE TRIGGER tr_repair_order_after_delete
AFTER DELETE ON `repair_order`
FOR EACH ROW
BEGIN
  INSERT INTO `repair_order_history` (
    order_id, operation, changed_at,
    user_id, vehicle_id, description,
    total_fee, submit_time, update_time,
    status, fault_type, is_paid
  ) VALUES (
    OLD.order_id,
    'DELETE',
    NOW(),
    OLD.user_id,
    OLD.vehicle_id,
    OLD.description,
    OLD.total_fee,
    OLD.submit_time,
    OLD.update_time,
    OLD.status,
    OLD.fault_type,
    OLD.is_paid
  );
END
$$


CREATE TABLE IF NOT EXISTS `users_history` (
  history_id   BIGINT      NOT NULL AUTO_INCREMENT,
  user_id      BIGINT      NOT NULL,
  operation    ENUM('INSERT','UPDATE','DELETE') NOT NULL,
  changed_at   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  username     VARCHAR(50)  NULL,
  role         VARCHAR(20)  NULL,
  password     VARCHAR(100) NULL,
  email        VARCHAR(100) NULL,
  phone        VARCHAR(20)  NULL,
  status       VARCHAR(20)  NULL,
  created_at   DATETIME     NULL,
  updated_at   DATETIME     NULL,
  PRIMARY KEY (`history_id`),
  INDEX idx_users_his_user(user_id),
  INDEX idx_users_his_time(changed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4$$



CREATE TABLE IF NOT EXISTS `repairman_profile_history` (
  history_id         BIGINT      NOT NULL AUTO_INCREMENT,
  user_id            BIGINT      NOT NULL,
  operation          ENUM('INSERT','UPDATE','DELETE') NOT NULL,
  changed_at         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  specialty          VARCHAR(30)   NULL,
  hourly_money_rate  DECIMAL(10,2) NULL,
  repairman_number   VARCHAR(50)   NULL,
  PRIMARY KEY (`history_id`),
  INDEX idx_rp_his_user(user_id),
  INDEX idx_rp_his_time(changed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4$$


DROP TRIGGER IF EXISTS tr_users_after_insert$$
DROP TRIGGER IF EXISTS tr_users_after_update$$
DROP TRIGGER IF EXISTS tr_users_after_delete$$

DROP TRIGGER IF EXISTS tr_rp_after_insert$$
DROP TRIGGER IF EXISTS tr_rp_after_update$$
DROP TRIGGER IF EXISTS tr_rp_after_delete$$


CREATE TRIGGER tr_users_after_insert
AFTER INSERT ON `users`
FOR EACH ROW
BEGIN
  INSERT INTO `users_history` (
    user_id,
    operation,
    changed_at,
    username,
    role,
    password,
    email,
    phone,
    status,
    created_at,
    updated_at
  ) VALUES (
    NEW.user_id,
    'INSERT',
    NOW(),
    NEW.username,
    NEW.role,
    NEW.password,
    NEW.email,
    NEW.phone,
    NEW.status,
    NEW.created_at,
    NEW.updated_at
  );
END
$$


CREATE TRIGGER tr_users_after_update
AFTER UPDATE ON `users`
FOR EACH ROW
BEGIN
  INSERT INTO `users_history` (
    user_id,
    operation,
    changed_at,
    username,
    role,
    password,
    email,
    phone,
    status,
    created_at,
    updated_at
  ) VALUES (
    NEW.user_id,
    'UPDATE',
    NOW(),
    NEW.username,
    NEW.role,
    NEW.password,
    NEW.email,
    NEW.phone,
    NEW.status,
    NEW.created_at,
    NEW.updated_at
  );
END
$$


CREATE TRIGGER tr_users_after_delete
AFTER DELETE ON `users`
FOR EACH ROW
BEGIN
  INSERT INTO `users_history` (
    user_id,
    operation,
    changed_at,
    username,
    role,
    password,
    email,
    phone,
    status,
    created_at,
    updated_at
  ) VALUES (
    OLD.user_id,
    'DELETE',
    NOW(),
    OLD.username,
    OLD.role,
    OLD.password,
    OLD.email,
    OLD.phone,
    OLD.status,
    OLD.created_at,
    OLD.updated_at
  );
END
$$


CREATE TRIGGER tr_rp_after_insert
AFTER INSERT ON `repairman_profile`
FOR EACH ROW
BEGIN
  INSERT INTO `repairman_profile_history` (
    user_id,
    operation,
    changed_at,
    specialty,
    hourly_money_rate,
    repairman_number
  ) VALUES (
    NEW.user_id,
    'INSERT',
    NOW(),
    NEW.specialty,
    NEW.hourly_money_rate,
    NEW.repairman_number
  );
END
$$


CREATE TRIGGER tr_rp_after_update
AFTER UPDATE ON `repairman_profile`
FOR EACH ROW
BEGIN
  INSERT INTO `repairman_profile_history` (
    user_id,
    operation,
    changed_at,
    specialty,
    hourly_money_rate,
    repairman_number
  ) VALUES (
    NEW.user_id,
    'UPDATE',
    NOW(),
    NEW.specialty,
    NEW.hourly_money_rate,
    NEW.repairman_number
  );
END
$$


CREATE TRIGGER tr_rp_after_delete
AFTER DELETE ON `repairman_profile`
FOR EACH ROW
BEGIN
  INSERT INTO `repairman_profile_history` (
    user_id,
    operation,
    changed_at,
    specialty,
    hourly_money_rate,
    repairman_number
  ) VALUES (
    OLD.user_id,
    'DELETE',
    NOW(),
    OLD.specialty,
    OLD.hourly_money_rate,
    OLD.repairman_number
  );
END
$$

CREATE TABLE IF NOT EXISTS `vehicle_history` (
  history_id    BIGINT     NOT NULL AUTO_INCREMENT,
  vehicle_id    BIGINT     NOT NULL,
  operation     ENUM('INSERT','UPDATE','DELETE') NOT NULL,
  changed_at    DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  owner_id      BIGINT     NULL,
  brand         VARCHAR(100) NULL,
  model         VARCHAR(100) NULL,
  license_plate VARCHAR(20)  NULL,
  register_date DATE        NULL,
  PRIMARY KEY (`history_id`),
  INDEX idx_vehicle_his_vehicle(vehicle_id),
  INDEX idx_vehicle_his_time(changed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4$$


CREATE TABLE IF NOT EXISTS `repair_record_history` (
  history_id       BIGINT      NOT NULL AUTO_INCREMENT,
  repair_record_id BIGINT      NOT NULL,
  operation        ENUM('INSERT','UPDATE','DELETE') NOT NULL,
  changed_at       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  order_id         BIGINT      NULL,
  repairman_id     BIGINT      NULL,
  fault_description TEXT       NULL,
  repair_result     TEXT       NULL,
  order_status      VARCHAR(20) NULL,
  actual_work_hours DECIMAL(5,2) NULL,
  completion_time   DATETIME    NULL,
  PRIMARY KEY (`history_id`),
  INDEX idx_rr_his_record(repair_record_id),
  INDEX idx_rr_his_time(changed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4$$


DROP TRIGGER IF EXISTS tr_vehicle_after_insert$$
DROP TRIGGER IF EXISTS tr_vehicle_after_update$$
DROP TRIGGER IF EXISTS tr_vehicle_after_delete$$

DROP TRIGGER IF EXISTS tr_rr_after_insert$$
DROP TRIGGER IF EXISTS tr_rr_after_update$$
DROP TRIGGER IF EXISTS tr_rr_after_delete$$


CREATE TRIGGER tr_vehicle_after_insert
AFTER INSERT ON `vehicle`
FOR EACH ROW
BEGIN
  INSERT INTO `vehicle_history` (
    vehicle_id,
    operation,
    changed_at,
    owner_id,
    brand,
    model,
    license_plate,
    register_date
  ) VALUES (
    NEW.vehicle_id,
    'INSERT',
    NOW(),
    NEW.owner_id,
    NEW.brand,
    NEW.model,
    NEW.license_plate,
    NEW.register_date
  );
END
$$


CREATE TRIGGER tr_vehicle_after_update
AFTER UPDATE ON `vehicle`
FOR EACH ROW
BEGIN
  INSERT INTO `vehicle_history` (
    vehicle_id,
    operation,
    changed_at,
    owner_id,
    brand,
    model,
    license_plate,
    register_date
  ) VALUES (
    NEW.vehicle_id,
    'UPDATE',
    NOW(),
    NEW.owner_id,
    NEW.brand,
    NEW.model,
    NEW.license_plate,
    NEW.register_date
  );
END
$$


CREATE TRIGGER tr_vehicle_after_delete
AFTER DELETE ON `vehicle`
FOR EACH ROW
BEGIN
  INSERT INTO `vehicle_history` (
    vehicle_id,
    operation,
    changed_at,
    owner_id,
    brand,
    model,
    license_plate,
    register_date
  ) VALUES (
    OLD.vehicle_id,
    'DELETE',
    NOW(),
    OLD.owner_id,
    OLD.brand,
    OLD.model,
    OLD.license_plate,
    OLD.register_date
  );
END
$$


CREATE TRIGGER tr_rr_after_insert
AFTER INSERT ON `repair_record`
FOR EACH ROW
BEGIN
  INSERT INTO `repair_record_history` (
    repair_record_id,
    operation,
    changed_at,
    order_id,
    repairman_id,
    fault_description,
    repair_result,
    order_status,
    actual_work_hours,
    completion_time
  ) VALUES (
    NEW.repair_record_id,
    'INSERT',
    NOW(),
    NEW.order_id,
    NEW.repairman_id,
    NEW.fault_description,
    NEW.repair_result,
    NEW.order_status,
    NEW.actual_work_hours,
    NEW.completion_time
  );
END
$$


CREATE TRIGGER tr_rr_after_update
AFTER UPDATE ON `repair_record`
FOR EACH ROW
BEGIN
  INSERT INTO `repair_record_history` (
    repair_record_id,
    operation,
    changed_at,
    order_id,
    repairman_id,
    fault_description,
    repair_result,
    order_status,
    actual_work_hours,
    completion_time
  ) VALUES (
    NEW.repair_record_id,
    'UPDATE',
    NOW(),
    NEW.order_id,
    NEW.repairman_id,
    NEW.fault_description,
    NEW.repair_result,
    NEW.order_status,
    NEW.actual_work_hours,
    NEW.completion_time
  );
END
$$


CREATE TRIGGER tr_rr_after_delete
AFTER DELETE ON `repair_record`
FOR EACH ROW
BEGIN
  INSERT INTO `repair_record_history` (
    repair_record_id,
    operation,
    changed_at,
    order_id,
    repairman_id,
    fault_description,
    repair_result,
    order_status,
    actual_work_hours,
    completion_time
  ) VALUES (
    OLD.repair_record_id,
    'DELETE',
    NOW(),
    OLD.order_id,
    OLD.repairman_id,
    OLD.fault_description,
    OLD.repair_result,
    OLD.order_status,
    OLD.actual_work_hours,
    OLD.completion_time
  );
END
$$


CREATE TABLE IF NOT EXISTS `material_usage_history` (
  history_id         BIGINT      NOT NULL AUTO_INCREMENT,
  material_usage_id  BIGINT      NOT NULL,
  operation          ENUM('INSERT','UPDATE','DELETE') NOT NULL,
  changed_at         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  order_id           BIGINT      NULL,
  material_name      VARCHAR(100) NULL,
  quantity           INT         NULL,
  unit_price         DECIMAL(10,2) NULL,
  create_time        DATETIME    NULL,
  PRIMARY KEY (`history_id`),
  INDEX idx_mu_his_usage(material_usage_id),
  INDEX idx_mu_his_time(changed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4$$

CREATE TABLE IF NOT EXISTS `feedback_history` (
  history_id      BIGINT      NOT NULL AUTO_INCREMENT,
  feedback_id     BIGINT      NOT NULL,
  operation       ENUM('INSERT','UPDATE','DELETE') NOT NULL,
  changed_at      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  order_id        BIGINT      NULL,
  user_id         BIGINT      NULL,
  rating          INT         NULL,
  feed_back_type  VARCHAR(20) NULL,
  description     TEXT        NULL,
  feedback_time   DATETIME    NULL,
  PRIMARY KEY (`history_id`),
  INDEX idx_fb_his_feedback(feedback_id),
  INDEX idx_fb_his_time(changed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4$$

CREATE TABLE IF NOT EXISTS `assignment_history` (
  history_id         BIGINT      NOT NULL AUTO_INCREMENT,
  assignment_id      BIGINT      NOT NULL,
  operation          ENUM('INSERT','UPDATE','DELETE') NOT NULL,
  changed_at         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  order_id           BIGINT      NULL,
  repairman_id       BIGINT      NULL,
  assignment_time    DATETIME    NULL,
  assignment_status  VARCHAR(20) NULL,
  PRIMARY KEY (`history_id`),
  INDEX idx_asg_his_assignment(assignment_id),
  INDEX idx_asg_his_time(changed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4$$

CREATE TABLE IF NOT EXISTS `labor_fee_log_history` (
  history_id        BIGINT      NOT NULL AUTO_INCREMENT,
  labor_fee_log_id  BIGINT      NOT NULL,
  operation         ENUM('INSERT','UPDATE','DELETE') NOT NULL,
  changed_at        DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  order_id          BIGINT      NULL,
  repairman_id      BIGINT      NULL,
  month             VARCHAR(7)  NULL,
  total_hours       DECIMAL(7,2) NULL,
  total_income      DECIMAL(10,2) NULL,
  settle_time       DATETIME    NULL,
  PRIMARY KEY (`history_id`),
  INDEX idx_lf_his_log(labor_fee_log_id),
  INDEX idx_lf_his_time(changed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4$$




DROP TRIGGER IF EXISTS tr_mu_after_insert$$
DROP TRIGGER IF EXISTS tr_mu_after_update$$
DROP TRIGGER IF EXISTS tr_mu_after_delete$$

DROP TRIGGER IF EXISTS tr_fb_after_insert$$
DROP TRIGGER IF EXISTS tr_fb_after_update$$
DROP TRIGGER IF EXISTS tr_fb_after_delete$$

DROP TRIGGER IF EXISTS tr_asg_after_insert$$
DROP TRIGGER IF EXISTS tr_asg_after_update$$
DROP TRIGGER IF EXISTS tr_asg_after_delete$$

DROP TRIGGER IF EXISTS tr_lf_after_insert$$
DROP TRIGGER IF EXISTS tr_lf_after_update$$
DROP TRIGGER IF EXISTS tr_lf_after_delete$$



CREATE TRIGGER tr_mu_after_insert
AFTER INSERT ON `material_usage`
FOR EACH ROW
BEGIN
  INSERT INTO `material_usage_history` (
    material_usage_id,
    operation,
    changed_at,
    order_id,
    material_name,
    quantity,
    unit_price,
    create_time
  ) VALUES (
    NEW.material_usage_id,
    'INSERT',
    NOW(),
    NEW.order_id,
    NEW.material_name,
    NEW.quantity,
    NEW.unit_price,
    NEW.create_time
  );
END
$$

CREATE TRIGGER tr_mu_after_update
AFTER UPDATE ON `material_usage`
FOR EACH ROW
BEGIN
  INSERT INTO `material_usage_history` (
    material_usage_id,
    operation,
    changed_at,
    order_id,
    material_name,
    quantity,
    unit_price,
    create_time
  ) VALUES (
    NEW.material_usage_id,
    'UPDATE',
    NOW(),
    NEW.order_id,
    NEW.material_name,
    NEW.quantity,
    NEW.unit_price,
    NEW.create_time
  );
END
$$

CREATE TRIGGER tr_mu_after_delete
AFTER DELETE ON `material_usage`
FOR EACH ROW
BEGIN
  INSERT INTO `material_usage_history` (
    material_usage_id,
    operation,
    changed_at,
    order_id,
    material_name,
    quantity,
    unit_price,
    create_time
  ) VALUES (
    OLD.material_usage_id,
    'DELETE',
    NOW(),
    OLD.order_id,
    OLD.material_name,
    OLD.quantity,
    OLD.unit_price,
    OLD.create_time
  );
END
$$


CREATE TRIGGER tr_fb_after_insert
AFTER INSERT ON `feedback`
FOR EACH ROW
BEGIN
  INSERT INTO `feedback_history` (
    feedback_id,
    operation,
    changed_at,
    order_id,
    user_id,
    rating,
    feed_back_type,
    description,
    feedback_time
  ) VALUES (
    NEW.feedback_id,
    'INSERT',
    NOW(),
    NEW.order_id,
    NEW.user_id,
    NEW.rating,
    NEW.feed_back_type,
    NEW.description,
    NEW.feedback_time
  );
END
$$


CREATE TRIGGER tr_fb_after_update
AFTER UPDATE ON `feedback`
FOR EACH ROW
BEGIN
  INSERT INTO `feedback_history` (
    feedback_id,
    operation,
    changed_at,
    order_id,
    user_id,
    rating,
    feed_back_type,
    description,
    feedback_time
  ) VALUES (
    NEW.feedback_id,
    'UPDATE',
    NOW(),
    NEW.order_id,
    NEW.user_id,
    NEW.rating,
    NEW.feed_back_type,
    NEW.description,
    NEW.feedback_time
  );
END
$$

CREATE TRIGGER tr_fb_after_delete
AFTER DELETE ON `feedback`
FOR EACH ROW
BEGIN
  INSERT INTO `feedback_history` (
    feedback_id,
    operation,
    changed_at,
    order_id,
    user_id,
    rating,
    feed_back_type,
    description,
    feedback_time
  ) VALUES (
    OLD.feedback_id,
    'DELETE',
    NOW(),
    OLD.order_id,
    OLD.user_id,
    OLD.rating,
    OLD.feed_back_type,
    OLD.description,
    OLD.feedback_time
  );
END
$$



CREATE TRIGGER tr_asg_after_insert
AFTER INSERT ON `assignment`
FOR EACH ROW
BEGIN
  INSERT INTO `assignment_history` (
    assignment_id,
    operation,
    changed_at,
    order_id,
    repairman_id,
    assignment_time,
    assignment_status
  ) VALUES (
    NEW.assignment_id,
    'INSERT',
    NOW(),
    NEW.order_id,
    NEW.repairman_id,
    NEW.assignment_time,
    NEW.assignment_status
  );
END
$$

CREATE TRIGGER tr_asg_after_update
AFTER UPDATE ON `assignment`
FOR EACH ROW
BEGIN
  INSERT INTO `assignment_history` (
    assignment_id,
    operation,
    changed_at,
    order_id,
    repairman_id,
    assignment_time,
    assignment_status
  ) VALUES (
    NEW.assignment_id,
    'UPDATE',
    NOW(),
    NEW.order_id,
    NEW.repairman_id,
    NEW.assignment_time,
    NEW.assignment_status
  );
END
$$

CREATE TRIGGER tr_asg_after_delete
AFTER DELETE ON `assignment`
FOR EACH ROW
BEGIN
  INSERT INTO `assignment_history` (
    assignment_id,
    operation,
    changed_at,
    order_id,
    repairman_id,
    assignment_time,
    assignment_status
  ) VALUES (
    OLD.assignment_id,
    'DELETE',
    NOW(),
    OLD.order_id,
    OLD.repairman_id,
    OLD.assignment_time,
    OLD.assignment_status
  );
END
$$



CREATE TRIGGER tr_lf_after_insert
AFTER INSERT ON `labor_fee_log`
FOR EACH ROW
BEGIN
  INSERT INTO `labor_fee_log_history` (
    labor_fee_log_id,
    operation,
    changed_at,
    order_id,
    repairman_id,
    month,
    total_hours,
    total_income,
    settle_time
  ) VALUES (
    NEW.labor_fee_log_id,
    'INSERT',
    NOW(),
    NEW.order_id,
    NEW.repairman_id,
    NEW.month,
    NEW.total_hours,
    NEW.total_income,
    NEW.settle_time
  );
END
$$

CREATE TRIGGER tr_lf_after_update
AFTER UPDATE ON `labor_fee_log`
FOR EACH ROW
BEGIN
  INSERT INTO `labor_fee_log_history` (
    labor_fee_log_id,
    operation,
    changed_at,
    order_id,
    repairman_id,
    month,
    total_hours,
    total_income,
    settle_time
  ) VALUES (
    NEW.labor_fee_log_id,
    'UPDATE',
    NOW(),
    NEW.order_id,
    NEW.repairman_id,
    NEW.month,
    NEW.total_hours,
    NEW.total_income,
    NEW.settle_time
  );
END
$$

CREATE TRIGGER tr_lf_after_delete
AFTER DELETE ON `labor_fee_log`
FOR EACH ROW
BEGIN
  INSERT INTO `labor_fee_log_history` (
    labor_fee_log_id,
    operation,
    changed_at,
    order_id,
    repairman_id,
    month,
    total_hours,
    total_income,
    settle_time
  ) VALUES (
    OLD.labor_fee_log_id,
    'DELETE',
    NOW(),
    OLD.order_id,
    OLD.repairman_id,
    OLD.month,
    OLD.total_hours,
    OLD.total_income,
    OLD.settle_time
  );
END
$$
