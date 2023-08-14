-- liquibase formatted sql

-- changeset InnaSerebriakova:1
CREATE TABLE report_cat_shelter
(
    id_report   SERIAL PRIMARY KEY,
    user_id     INT           NOT NULL,
    description VARCHAR(1024) NOT NULL,
    photo       VARCHAR(50),
    date_report DATE
);

ALTER TABLE report_cat_shelter ADD CONSTRAINT report_cat_shelter_user_id_fkey FOREIGN KEY (user_id)
    REFERENCES cat_shelter_users(user_id);

-- changeset InnaSerebriakova:2
alter table report_cat_shelter
    alter column date_report type timestamp using date_report::timestamp;

