DROP TABLE IF EXISTS staffs;

CREATE TABLE staffs (
  id int unsigned AUTO_INCREMENT,
  section_id int unsigned NOT NULL,
  staff_status_id int unsigned NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(id)
);
