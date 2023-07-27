-- liquibase formatted sql

-- changeset AlexKutin:1
CREATE TABLE dog_shelter_users
(
    user_id                SERIAL PRIMARY KEY,
    telegram_id            BIGINT NOT NULL UNIQUE,
    user_contacts          VARCHAR(100),
    shelter_id             INT NOT NULL
);
