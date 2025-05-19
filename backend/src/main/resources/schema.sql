DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  user_id    BIGINT      NOT NULL AUTO_INCREMENT,
  username   VARCHAR(50) NOT NULL,
  role       VARCHAR(20) NOT NULL,
  password   VARCHAR(100) NOT NULL,        -- 存哈希值
  email      VARCHAR(100) NOT NULL,
  phone      VARCHAR(20),
  status     VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
  created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
                                ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_user_username` (`username`),
  UNIQUE KEY `uk_user_email`    (`email`),
  CHECK (status IN ('ACTIVE','DISABLED','LOCKED'))
  CHECK (Role IN ('CUSTOMER','STAFF','ADMIN'))
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4;
