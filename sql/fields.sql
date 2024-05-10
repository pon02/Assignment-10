DROP TABLE IF EXISTS fields;

CREATE TABLE fields (
  id int unsigned AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  date_of_use DATE NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(id)
);
