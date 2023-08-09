package pro.sky.animalshelter.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pro.sky.animalshelter.controller.ShelterController;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.repository.ShelterRepository;

import static org.junit.jupiter.api.Assertions.*;

class ShelterServiceTest {

    private static final ShelterType CAT_SHELTER_TYPE = ShelterType.CAT_SHELTER;

    @Mock
    ShelterRepository shelterRepository;

    @InjectMocks
    ShelterService shelterService;

    @Test
    public void addVolunteerToShelter() {

    }
}