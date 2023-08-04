-- liquibase formatted sql

-- changeset InnaSerebriakova:1
CREATE TABLE report
(
    id_report   SERIAL PRIMARY KEY,
    user_id     INT           NOT NULL,
    description VARCHAR(1024) NOT NULL,
    photo       VARBINARY( MAX),
    date_report DATE
);