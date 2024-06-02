DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
  id INTEGER unsigned AUTO_INCREMENT,
  car_type_id int unsigned NOT NULL,
  order_status_id int unsigned NOT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at datetime ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY(id)
);
