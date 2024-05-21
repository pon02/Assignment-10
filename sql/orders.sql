DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
  id int unsigned AUTO_INCREMENT,
  car_type_id int unsigned NOT NULL,
  order_status_id int unsigned NOT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at datetime NOT NULL DEFAULT 0000-00-00 00:00:00 ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY(id)
);
