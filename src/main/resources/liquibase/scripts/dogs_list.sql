-- liquibase formatted sql

-- changeset DrogolovaNadezhda:3
CREATE TABLE dogs_list
(
dog_id SERIAL PRIMARY KEY,
shelter_id INT NOT NULL REFERENCES shelters(shelter_id),
age INT NOT NULL,
gender VARCHAR NOT NULL,
breed VARCHAR NOT NULL
);

-- changeset DrogolovaNadezhda:4
alter table dogs_list add column dog_name varchar;
