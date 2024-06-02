DROP TABLE IF EXISTS car_types;

CREATE TABLE car_types (
  id INTEGER unsigned AUTO_INCREMENT,
  car_type VARCHAR(100) NOT NULL,
  capacity int unsigned NOT NULL,
  PRIMARY KEY(id)
);
INSERT INTO car_types (car_type, capacity) VALUES ("セダン4人", 4);
INSERT INTO car_types (car_type, capacity) VALUES ("ハコバン7人", 7);
INSERT INTO car_types (car_type, capacity) VALUES ("ハイエース9人", 9);
INSERT INTO car_types (car_type, capacity) VALUES ("小型バス", 21);
