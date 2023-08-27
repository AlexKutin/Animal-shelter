-- liquibase formatted sql

-- changeset DrogolovaNadezhda:1
CREATE TABLE cat_adopter_list
(
adoption_id SERIAL PRIMARY KEY,
adopter_id INT NOT NULL REFERENCES cat_shelter_users(user_id),
cat_id INT NOT NULL REFERENCES cats_list(cat_id),
adoption_date TIMESTAMP NOT NULL
);

-- changeset AlexKutin:2
ALTER TABLE cat_adopter_list ADD COLUMN adopter_status VARCHAR(25),
    ADD COLUMN end_probation_date TIMESTAMP;

-- changeset AlexKutin:3
ALTER TABLE cat_adopter_list RENAME COLUMN adopter_id TO user_id;

ALTER TABLE cat_adopter_list RENAME COLUMN adoption_id TO adopter_id;

-- changeset AlexKutin:4
ALTER TABLE cat_adopter_list RENAME TO cat_adopters
-- changeset BM:5
ALTER TABLE cat_adopters ADD COLUMN chat_id BIGINT UNIQUE;

-- changeset AlexKutin:6
ALTER TABLE cat_adopters DROP CONSTRAINT cat_adopters_chat_id_key;