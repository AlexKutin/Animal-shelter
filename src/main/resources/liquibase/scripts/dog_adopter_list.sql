-- liquibase formatted sql

-- changeset DrogolovaNadezhda:1
CREATE TABLE dog_adopter_list
(
adoption_id SERIAL PRIMARY KEY,
adopter_id INT NOT NULL REFERENCES dog_shelter_users(user_id),
dog_id INT NOT NULL REFERENCES dogs_list(dog_id),
adoption_date TIMESTAMP NOT NULL
);