SET GLOBAL sql_mode=STRICT_TRANS_TABLES;



DROP TABLE IF EXISTS room_info;
DROP TABLE IF EXISTS student_info;

CREATE TABLE student_info (
  registration_number INT NOT NULL,
  name VARCHAR(50) NOT NULL,
  exam_center_id INT,
  exam_room_id INT,
  session_id INT,
  seat INT,
  PRIMARY KEY (registration_number)
);




CREATE TABLE room_info(
    exam_center_id INT NOT NULL,
    exam_room_id INT NOT NULL,
    session_id INT NOT NULL,
    exam_center_name VARCHAR(40),
    expected_time VARCHAR(40),
    paper_name VARCHAR(40),
    PRIMARY KEY (exam_center_id, exam_room_id, session_id)
);


