SET GLOBAL sql_mode=STRICT_TRANS_TABLES;

DROP TABLE IF EXISTS student_info;

DROP TABLE IF EXISTS room_info;
DROP TABLE IF EXISTS student_info;

CREATE TABLE student_info (
  registno INT NOT NULL,
  name VARCHAR(50) NOT NULL,
  kdno INT,
  kcno INT,
  ccno INT,
  seat INT,
  PRIMARY KEY (registno)
);




CREATE TABLE room_info(
    kdno INT NOT NULL,
    kcno INT NOT NULL,
    ccno INT NOT NULL,
    kdname VARCHAR(40),
    exptime VARCHAR(40),
    papername VARCHAR(40),
    PRIMARY KEY (kdno, kcno, ccno)
)


