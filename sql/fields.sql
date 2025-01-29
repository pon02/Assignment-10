DROP TABLE IF EXISTS fields;

CREATE TABLE fields (
  id int unsigned AUTO_INCREMENT,
  field_name VARCHAR(100) NOT NULL,
  date_of_use DATE NOT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(id)
);
