-- liquibase formatted sql

-- changeset AlexKutin:1
CREATE TABLE volunteers
(
    volunteer_id           SERIAL PRIMARY KEY,
    volunteer_name         VARCHAR(50) NOT NULL,
    volunteer_telegram     VARCHAR(50),
    volunteer_phone        VARCHAR(50)
);
-- changeset InnaSerebriakova:2
INSERT INTO volunteers (volunteer_name, volunteer_telegram, volunteer_phone) VALUES ('Evgeniy', null, null), ('Lisa', null, null);

-- changeset AlexKutin:3
ALTER TABLE volunteers ADD COLUMN shelter_id INT;

ALTER TABLE volunteers ADD CONSTRAINT volunteers_shelter_id_fkey FOREIGN KEY (shelter_id) REFERENCES shelters(shelter_id);

UPDATE volunteers
SET volunteer_telegram = '@VolunteerEvgeniy', volunteer_phone = '+7-864-091-4532',
    shelter_id = (SELECT shelter_id FROM shelters WHERE shelter_type = 'DOG_SHELTER')
WHERE volunteer_name = 'Evgeniy';

UPDATE volunteers
SET volunteer_telegram = '@VolunteerLisa', volunteer_phone = '+7-954-790-3457',
    shelter_id = (SELECT shelter_id FROM shelters WHERE shelter_type = 'CAT_SHELTER')
WHERE volunteer_name = 'Lisa';

ALTER TABLE volunteers ALTER COLUMN shelter_id SET NOT NULL;

-- changeset AlexKutin:4
ALTER TABLE volunteers ADD COLUMN volunteer_active BOOLEAN default true;

UPDATE volunteers SET volunteer_active = true WHERE true;

ALTER TABLE volunteers ALTER COLUMN volunteer_active SET NOT NULL;
