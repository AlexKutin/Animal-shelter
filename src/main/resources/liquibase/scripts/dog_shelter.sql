-- liquibase formatted sql

-- changeset AlexKutin:1
CREATE TABLE dog_shelter
(
    shelter_id             SERIAL PRIMARY KEY,
    shelter_name           VARCHAR(100) NOT NULL UNIQUE,
    shelter_description    VARCHAR(1024) NOT NULL,
    shelter_address        VARCHAR(255) NOT NULL,
    shelter_contacts       VARCHAR(100) NOT NULL,
    security_contacts      VARCHAR(50),
    volunteer_id           INT NOT NULL
);
