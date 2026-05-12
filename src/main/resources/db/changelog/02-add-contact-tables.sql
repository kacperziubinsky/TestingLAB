CREATE SEQUENCE contact_id START WITH 1 INCREMENT BY 1;

CREATE TABLE contact (
  id BIGINT NOT NULL PRIMARY KEY,
  email VARCHAR(255),
  address VARCHAR(255),
  phone VARCHAR(255),
  person_id BIGINT NOT NULL,
  CONSTRAINT fk_contact_person FOREIGN KEY (person_id) REFERENCES person (id)
);

ALTER SEQUENCE contact_id OWNED BY contact.id;

CREATE TABLE person_contact (
  person_id BIGINT NOT NULL,
  contact_id BIGINT NOT NULL,
  PRIMARY KEY (person_id, contact_id),
  CONSTRAINT fk_person_contact_person FOREIGN KEY (person_id) REFERENCES person (id),
  CONSTRAINT fk_person_contact_contact FOREIGN KEY (contact_id) REFERENCES contact (id)
);
