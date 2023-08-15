-- liquibase formatted sql

-- changeset DrogolovaNadezhda:1
CREATE TABLE cat_adopter_list
(
adoption_id SERIAL PRIMARY KEY,
adopter_id INT NOT NULL REFERENCES cat_shelter_users(user_id),
cat_id INT NOT NULL REFERENCES cats_list(cat_id),
adoption_date TIMESTAMP NOT NULL
);