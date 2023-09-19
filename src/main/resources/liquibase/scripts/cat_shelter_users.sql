-- liquibase formatted sql

-- changeset AlexKutin:1
CREATE TABLE cat_shelter_users
(
    user_id                SERIAL PRIMARY KEY,
    telegram_id            BIGINT NOT NULL UNIQUE,
    user_contacts          VARCHAR(100),
    shelter_id             INT NOT NULL
);

-- changeset AlexKutin:2
ALTER TABLE cat_shelter_users ADD COLUMN first_name VARCHAR(50), ADD COLUMN last_name VARCHAR(50);

-- changeset AlexKutin:3
ALTER TABLE cat_shelter_users ADD COLUMN user_name VARCHAR(50);

-- changeset AlexKutin:4
ALTER TABLE cat_shelter_users ADD CONSTRAINT cat_shelter_users_shelter_id_fkey FOREIGN KEY (shelter_id) REFERENCES shelters(shelter_id);

-- changeset AlexKutin:5
ALTER TABLE cat_shelter_users ADD COLUMN chat_id BIGINT UNIQUE;