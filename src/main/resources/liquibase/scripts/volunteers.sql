-- liquibase formatted sql

-- changeset AlexKutin:1
CREATE TABLE volunteers
(
    volunteer_id           SERIAL PRIMARY KEY,
    volunteer_name         VARCHAR(50) NOT NULL,
    volunteer_telegram     VARCHAR(50),
    volunteer_phone        VARCHAR(50)
);
