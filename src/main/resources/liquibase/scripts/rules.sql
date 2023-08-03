-- liquibase formatted sql

-- changeset InnaSerebriakova:1

CREATE TABLE rules
(
    id_rules                 SERIAL PRIMARY KEY,
    riles_meeting            VARCHAR(1024) NOT NULL,
    list_doc_for_take_pet    VARCHAR(1024) NOT NULL,
    rules_transportation     VARCHAR(1024) NOT NULL,
    rules_gh_for_adult_pet   VARCHAR(1024) NOT NULL,
    rules_gh_for_child_pet   VARCHAR(1024) NOT NULL,
    rules_gh_for_special_pet VARCHAR(1024) NOT NULL,
    reasons_refusal          VARCHAR(1024) NOT NULL,
);
INSERT INTO rules (riles_meeting, list_doc_for_take_pet, rules_transportation, rules_gh_for_adult_pet, rules_gh_for_child_pet, rules_gh_for_special_pet, reasons_refusal) VALUES (
'1. Присядьте на уровень роста животного и назовите его по имени.
2. Дайте животному обнюхать Вас.
3. Угостите животное лакомством.
4. Поиграйте или поговорите с ним.',
'1. Удостоверение личности с регистрацией проживания.
2. Согласие членов семьи на проживание с животным.
3. Справка с ветклиники об отсутствии обращений по поводу инфекционных заболеваний,
опасных для животного за последний год',
'1. Здоровое животное может быть транспортировано в салоне машины, с поводком и намордником.
2. Кроме водителя в машине должен быть кто-нибудь, рядом с животным.
3. Если водитель один, желательно использовать специальную клетку или переноску.',
'1. У животного должно быть свое место.
2. Рядом с домом должно быть место для прогулок с животным.
3. Территория дома(квартиры) должна быть огорожена и закрыта, чтоб животное случайно не выбежало на улицу.',
'1. У животного должно быть свое место.
2. Рядом с домом должно быть место для прогулок с животным.
3. Территория дома(квартиры) должна быть огорожена и закрыта, чтоб животное случайно не выбежало на улицу.
4. В доме должны быть спрятаны все потенциально опасные для животного предметы.',
'1. У животного должно быть свое место.
2. Рядом с домом должно быть место для прогулок с животным.
3. Территория дома(квартиры) должна быть огорожена и закрыта, чтоб животное случайно не выбежало на улицу.
4. В доме должны быть спрятаны все потенциально опасные для животного предметы.
5. В доме не должно быть открытых люков, лестниц, острых углов.',
'1. Случаи жестокого обращения с животными в прошлом
2. На усмотрение волонтера'),
('1. Присядьте на уровень роста животного и назовите его по имени.
 2. Дайте животному обнюхать Вас.
 3. Угостите животное лакомством.
 4. Поиграйте или поговорите с ним.',
 '1. Удостоверение личности с регистрацией проживания.
 2. Согласие членов семьи на проживание с животным.
 3. Справка с ветклиники об отсутствии обращений по поводу инфекционных заболеваний,
 опасных для животного за последний год',
 '1. Использовать специальную клетку или переноску.',
 '1. У животного должно быть свое место.
 2. Территория дома(квартиры) должна быть огорожена и закрыта, чтоб животное случайно не выбежало на улицу.',
 '1. У животного должно быть свое место.
 2. Территория дома(квартиры) должна быть огорожена и закрыта, чтоб животное случайно не выбежало на улицу.
 3. В доме должны быть спрятаны все потенциально опасные для животного предметы.',
 '1. У животного должно быть свое место.
 2. Территория дома(квартиры) должна быть огорожена и закрыта, чтоб животное случайно не выбежало на улицу.
 3. В доме должны быть спрятаны все потенциально опасные для животного предметы.
 4. В доме не должно быть открытых люков, лестниц, острых углов.',
 '1. Случаи жестокого обращения с животными в прошлом
 2. На усмотрение волонтера')