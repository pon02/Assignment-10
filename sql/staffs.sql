DROP TABLE IF EXISTS staffs;

CREATE TABLE staffs (
  id int unsigned AUTO_INCREMENT,
  field_id int unsigned NOT NULL,
  section_id int unsigned NOT NULL,
  staff_status_id int unsigned NOT NULL,
  created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at datetime ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY(id)
);
