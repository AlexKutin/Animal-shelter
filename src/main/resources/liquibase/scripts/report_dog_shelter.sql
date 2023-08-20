-- liquibase formatted sql

-- changeset InnaSerebriakova:1
CREATE TABLE report_dog_shelter
(
    id_report   SERIAL PRIMARY KEY,
    user_id     INT           NOT NULL,
    description VARCHAR(1024) NOT NULL,
    photo       VARCHAR(50),
    date_report DATE
);

ALTER TABLE report_dog_shelter ADD CONSTRAINT report_dog_shelter_user_id_fkey FOREIGN KEY (user_id) REFERENCES dog_shelter_users(user_id);

-- changeset AlexKutin:2
ALTER TABLE report_dog_shelter DROP CONSTRAINT IF EXISTS report_dog_shelter_user_id_fkey;

ALTER TABLE report_dog_shelter RENAME COLUMN user_id TO adopter_id;

ALTER TABLE report_dog_shelter ADD CONSTRAINT report_dog_shelter_adopter_id_fkey FOREIGN KEY (adopter_id)
    REFERENCES dog_adopters(adopter_id);

-- changeset AlexKutin:3
ALTER TABLE report_dog_shelter ADD COLUMN report_status VARCHAR(25);
