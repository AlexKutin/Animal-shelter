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

-- changeset BegaliMashrapov:4
-- Удаление столбца photo
ALTER TABLE report_dog_shelter DROP COLUMN IF EXISTS photo;

-- Добавление столбца photo_data с типом BYTEA для хранения изображения
ALTER TABLE report_dog_shelter ADD COLUMN photo_data BYTEA;

-- Добавление столбца photo_filename для хранения имени файла изображения
ALTER TABLE report_dog_shelter ADD COLUMN photo_filename VARCHAR(100);

-- changeset AlexKutin:5
ALTER TABLE report_dog_shelter ALTER COLUMN description DROP NOT NULL;

-- changeset AlexKutin:6
-- Добавление столбца file_path для хранения пути к файлу изображения
ALTER TABLE report_dog_shelter ADD COLUMN IF NOT EXISTS file_path VARCHAR(255);

-- Добавление столбца file_size для хранения размера файла изображения
ALTER TABLE report_dog_shelter ADD COLUMN IF NOT EXISTS file_size int8;

-- Добавление столбца media_type для хранения типа файла изображения
ALTER TABLE report_dog_shelter ADD COLUMN IF NOT EXISTS media_type VARCHAR(25);

-- changeset AlexKutin:7
-- Удаление лишних столбцов
ALTER TABLE report_dog_shelter DROP COLUMN IF EXISTS photo_data;

ALTER TABLE report_dog_shelter DROP COLUMN IF EXISTS photo_filename;