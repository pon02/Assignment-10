CREATE TABLE staff_statuses (
  id int unsigned AUTO_INCREMENT,
  staff_status VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);
INSERT INTO staff_statuses (staff_status) VALUES ("待機中");
INSERT INTO staff_statuses (staff_status) VALUES ("出発済");
INSERT INTO staff_statuses (staff_status) VALUES ("取消");