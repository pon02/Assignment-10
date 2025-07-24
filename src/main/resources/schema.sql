DROP TABLE IF EXISTS fields;

CREATE TABLE fields (
  id int unsigned AUTO_INCREMENT,
  field_name VARCHAR(100) NOT NULL,
  date_of_use DATE NOT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(id)
);

DROP TABLE IF EXISTS fields;

CREATE TABLE fields (
  id int unsigned AUTO_INCREMENT,
  field_name VARCHAR(100) NOT NULL,
  date_of_use DATE NOT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(id)
);

DROP TABLE IF EXISTS fields;

CREATE TABLE fields (
  id int unsigned AUTO_INCREMENT,
  field_name VARCHAR(100) NOT NULL,
  date_of_use DATE NOT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(id)
);

DROP TABLE IF EXISTS car_types;

CREATE TABLE car_types (
  id INTEGER unsigned AUTO_INCREMENT,
  car_type VARCHAR(100) NOT NULL,
  capacity INTEGER unsigned NOT NULL,
  PRIMARY KEY(id)
);

DROP TABLE IF EXISTS sections;

CREATE TABLE sections (
  id int unsigned AUTO_INCREMENT,
  section_name VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);

DROP TABLE IF EXISTS order_statuses;

CREATE TABLE order_statuses (
  id int unsigned AUTO_INCREMENT,
  order_status VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);

DROP TABLE IF EXISTS staff_statuses;

CREATE TABLE staff_statuses (
  id int unsigned AUTO_INCREMENT,
  staff_status VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);
