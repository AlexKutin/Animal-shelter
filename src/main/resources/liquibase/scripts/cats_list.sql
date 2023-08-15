-- liquibase formatted sql

-- changeset DrogolovaNadezhda:1
CREATE TABLE cats_list
(
cat_id SERIAL PRIMARY KEY,
shelter_id INT NOT NULL REFERENCES shelters(shelter_id),
age INT NOT NULL,
gender VARCHAR NOT NULL,
breed VARCHAR NOT NULL,
cat_name VARCHAR NOT NULL
);