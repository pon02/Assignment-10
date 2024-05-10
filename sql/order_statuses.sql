DROP TABLE IF EXISTS order_statuses;

CREATE TABLE order_statuses (
  id int unsigned AUTO_INCREMENT,
  order_status VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);
INSERT INTO order_statuses (order_status) VALUES ("配車中");
INSERT INTO order_statuses (order_status) VALUES ("到着");
INSERT INTO order_statuses (order_status) VALUES ("出発済");
INSERT INTO order_statuses (order_status) VALUES ("取消");
