DROP TABLE IF EXISTS sections;

CREATE TABLE sections (
  id int unsigned AUTO_INCREMENT,
  section_name VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);
INSERT INTO sections (section_name) VALUES ("大道具");
INSERT INTO sections (section_name) VALUES ("音響");
INSERT INTO sections (section_name) VALUES ("照明");
INSERT INTO sections (section_name) VALUES ("特効");
