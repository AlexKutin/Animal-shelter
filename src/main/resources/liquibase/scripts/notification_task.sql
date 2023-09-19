-- liquibase formatted sql

-- changeset AlexKutin:1
CREATE TABLE notification_tasks
(
    id                     BIGSERIAL PRIMARY KEY,
    message                TEXT      NOT NULL,
    chat_id                BIGINT    NOT NULL,
    notification_date_time TIMESTAMP NOT NULL,
    processed              BOOLEAN NOT NULL DEFAULT false
);

-- changeset AlexKutin:2
ALTER TABLE notification_tasks ADD COLUMN shelter_type VARCHAR(20);

ALTER TABLE notification_tasks ALTER COLUMN shelter_type SET NOT NULL;
