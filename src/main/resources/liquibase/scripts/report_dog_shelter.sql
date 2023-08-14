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

-- changeset InnaSerebriakova:2
alter table report_dog_shelter
    alter column date_report type timestamp using date_report::timestamp;