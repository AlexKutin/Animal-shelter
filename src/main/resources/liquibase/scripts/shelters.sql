-- liquibase formatted sql

-- changeset InnaSerebriakova:1
CREATE TABLE shelters
(
    shelter_id             SERIAL PRIMARY KEY,
    shelter_type           VARCHAR(20) NOT NULL,
    shelter_name           VARCHAR(100) NOT NULL UNIQUE,
    shelter_description    VARCHAR(1024) NOT NULL,
    shelter_address        VARCHAR(255) NOT NULL,
    shelter_contacts       VARCHAR(100) NOT NULL,
    security_contacts      VARCHAR(50),
    volunteer_id           INT NOT NULL
);
-- changeset InnaSerebriakova:2
INSERT INTO shelters
    (shelter_type, shelter_name, shelter_description, shelter_address, shelter_contacts, security_contacts, volunteer_id)
VALUES ('DOG_SHELTER', 'Happy dog', 'Наш приют существует с 2020г. Приют был создан усилиями энтузиастов и за их средства.
Собаки попадают сюда по большей части с улицы, но есть и "отказники". Вы можете взять из нашего приюта друга в свой дом, после соблюдения некоторых формальностей', 'Часы посещений приюта:

вт - с 9:00 до 17:00
ср - с 9:00 до 17:00
чт - с 9:00 до 17:00
пт - с 9:00 до 17:00
сб - с 9:00 до 17:00
вс - с 9:00 до 17:00
пн - выходной

Адрес: Стрелецкая, 25', '+75550009977', ' ', 1), ('CAT_SHELTER', 'Nice cat', 'Наш приют существует с 2020г. Приют был создан усилиями энтузиастов и за их средства.
Кошки попадают сюда по большей части с улицы, но есть и "отказники". Вы можете взять из нашего приюта друга в свой дом, после соблюдения некоторых формальностей', 'Часы посещений приюта:

вт - с 9:00 до 17:00
ср - с 9:00 до 17:00
чт - с 9:00 до 17:00
пт - с 9:00 до 17:00
сб - с 9:00 до 17:00
вс - с 9:00 до 17:00
пн - выходной

Адрес: Стрелецкая, 27', '+75550009977', ' ', 2);

DROP TABLE cat_shelter;

DROP TABLE dog_shelter;

-- changeset AlexKutin:3
ALTER TABLE shelters ADD CONSTRAINT shelters_volunteer_id_fkey FOREIGN KEY (volunteer_id) REFERENCES volunteers(volunteer_id);

