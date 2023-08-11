-- liquibase formatted sql

-- changeset InnaSerebriakova:1
CREATE TABLE report
(
    id_report   SERIAL PRIMARY KEY,
    user_id     INT           NOT NULL,
    description VARCHAR(1024) NOT NULL,
    photo       VARCHAR(50),
    date_report DATE
);

-- changeset InnaSerebriakova:2
DROP TABLE report;