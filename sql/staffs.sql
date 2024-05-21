DROP TABLE IF EXISTS staffs;

CREATE TABLE staffs (
  id int unsigned AUTO_INCREMENT,
  section_id int unsigned NOT NULL,
  staff_status_id int unsigned NOT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at datetime NOT NULL DEFAULT 0000-00-00 00:00:00 ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY(id)
);
