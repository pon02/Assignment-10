DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
  id int unsigned AUTO_INCREMENT,
  car_type_id int unsigned NOT NULL,
  car_status_id int unsigned NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(id)
);
