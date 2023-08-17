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

-- changeset AlexKutin:3
ALTER TABLE shelters ADD CONSTRAINT shelters_volunteer_id_fkey FOREIGN KEY (volunteer_id) REFERENCES volunteers(volunteer_id);

-- changeset InnaSerebriakova:4
ALTER TABLE shelters ADD COLUMN id_rules INT;
UPDATE shelters SET id_rules =  shelter_id;
ALTER TABLE shelters ALTER COLUMN id_rules SET NOT NULL;
ALTER TABLE shelters ADD CONSTRAINT shelters_id_rules_fkey FOREIGN KEY (id_rules) REFERENCES rules(id_rules);

-- changeset AlexKutin:5
ALTER TABLE shelters ADD COLUMN safety_info VARCHAR(1024);

UPDATE shelters SET safety_info = 'Находясь на территории приюта, пожалуйста, соблюдайте наши правила и технику безопасности!
Запрещается:
Самостоятельно открывать выгулы и вольеры без разрешения работника приюта.
1. Кормить животных. Этим Вы можете спровоцировать драку. Угощения разрешены только постоянным опекунам и волонтерам, во время прогулок с животными на поводке.
2. Оставлять после себя мусор на территории приюта и прилегающей территории.
3. Подходить близко к вольерам и гладить собак через сетку на выгулах. Животные могут быть агрессивны!
4. Кричать, размахивать руками, бегать между будками или вольерами, пугать и дразнить животных.
5. Посещение приюта для детей дошкольного и младшего школьного возраста без сопровождения взрослых.'
WHERE shelter_type = 'DOG_SHELTER';

UPDATE shelters SET safety_info = '❌ В приют не допускаются:
- дети до 14 лет без сопровождения взрослых;
- лица в состоянии алкогольного или наркотического опьянения;
- лица в агрессивном или неадекватном состоянии.

❌ Для всех посетителей Приюта запрещается:
- кормить животных кормами и продуктами;
- посещать блок карантина и изолятор;
- давать животным самостоятельно какие-либо ветеринарные или медицинские препараты;
- осуществлять любые ветеринарные манипуляции с животными;
- находится без сопровождения сотрудника на территории приюта;
- посещать приют со своими животными.'
WHERE shelter_type = 'CAT_SHELTER';

-- changeset AlexKutin:6
UPDATE shelters SET security_contacts = '+7-955-000-9922, +7-987-065-4416' WHERE shelter_type = 'DOG_SHELTER';
UPDATE shelters SET security_contacts = '+7-925-711-5948, +7-995-611-0617' WHERE shelter_type = 'CAT_SHELTER';

-- changeset DrogolovaNadezhda: 7
ALTER TABLE shelters ADD COLUMN driving_directions VARCHAR(1024);

-- changeset DrogolovaNadezhda: 8
UPDATE shelters SET driving_directions =  'src/main/resources/dogshelter.png' WHERE shelter_id = 1;
UPDATE shelters SET driving_directions =  'src/main/resources/catshelter.png' WHERE shelter_id = 2;

-- changeset DrogolovaNadezhda: 9
UPDATE shelters SET driving_directions =  null WHERE shelter_id = 1;
UPDATE shelters SET driving_directions =  null WHERE shelter_id = 2;

-- changeset BegaliMashrapov:10
UPDATE shelters SET shelter_name = 'Happy Dog' WHERE shelter_type = 'DOG_SHELTER';
UPDATE shelters SET shelter_name = 'Nice Cat' WHERE shelter_type = 'CAT_SHELTER';
