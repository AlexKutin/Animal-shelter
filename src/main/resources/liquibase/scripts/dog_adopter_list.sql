-- liquibase formatted sql

-- changeset DrogolovaNadezhda:1
CREATE TABLE dog_adopter_list
(
adoption_id SERIAL PRIMARY KEY,
adopter_id INT NOT NULL REFERENCES dog_shelter_users(user_id),
dog_id INT NOT NULL REFERENCES dogs_list(dog_id),
adoption_date TIMESTAMP NOT NULL
);

-- changeset AlexKutin:2
ALTER TABLE dog_adopter_list ADD COLUMN adopter_status VARCHAR(25),
    ADD COLUMN end_probation_date TIMESTAMP;

-- changeset AlexKutin:3
ALTER TABLE dog_adopter_list RENAME COLUMN adopter_id TO user_id;

ALTER TABLE dog_adopter_list RENAME COLUMN adoption_id TO adopter_id;

-- changeset AlexKutin:4
ALTER TABLE dog_adopter_list RENAME TO dog_adopters