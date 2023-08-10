package pro.sky.animalshelter.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.dto.VolunteerDTO;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.repository.ShelterRepository;
import pro.sky.animalshelter.repository.VolunteerRepository;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShelterServiceTest {

    private static final ShelterType CAT_SHELTER_TYPE = ShelterType.CAT_SHELTER;
    private static final Integer CAT_VOLUNTEER_ID = 11;

    private static final String CAT_VOLUNTEER_NAME = "Cat Volunteer";
    private static final String CAT_VOLUNTEER_PHONE = "123";
    private static final String CAT_VOLUNTEER_TG_ADDR = "@Cat_Keeper";

    private Shelter catShelter;
    private VolunteerDTO catVolunteerDTO;

    @Mock
    ShelterRepository shelterRepository;

    @Mock
    VolunteerRepository volunteerRepository;

    @InjectMocks
    ShelterService shelterService;

    @BeforeEach
    public void init() {
        catShelter = new Shelter();
        catShelter.setShelterType(CAT_SHELTER_TYPE);
        catShelter.setId(1);
        catShelter.setShelterName("Cat shelter");
        catShelter.setShelterDescription("Cat shelter description");
        catShelter.setShelterAddress("Cat address");

        catVolunteerDTO = new VolunteerDTO();
        catVolunteerDTO.setShelterType(CAT_SHELTER_TYPE);
        catVolunteerDTO.setName(CAT_VOLUNTEER_NAME);
        catVolunteerDTO.setPhone(CAT_VOLUNTEER_PHONE);
        catVolunteerDTO.setTelegramAddress(CAT_VOLUNTEER_TG_ADDR);
        catVolunteerDTO.setActive(true);
    }


    @Test
    public void addVolunteerToShelter() throws CloneNotSupportedException {
        when(shelterRepository.findSheltersByShelterType(ShelterType.CAT_SHELTER)).thenReturn(Collections.singletonList(catShelter));
        Volunteer catVolunteer = catVolunteerDTO.toVolunteer(catShelter);
        Volunteer catVolunteerSaved = (Volunteer) catVolunteer.clone();
        catVolunteerSaved.setId(CAT_VOLUNTEER_ID);

        when(volunteerRepository.save(any())).thenReturn(catVolunteerSaved);

        VolunteerDTO catVolunteerDTOSaved = shelterService.addVolunteerToShelter(catVolunteerDTO);

        Mockito.verify(shelterRepository, Mockito.times(1)).findSheltersByShelterType(CAT_SHELTER_TYPE);
        Mockito.verify(volunteerRepository, Mockito.times(1)).save(catVolunteer);
        Assertions.assertThat(catVolunteerDTOSaved.getId()).isEqualTo(CAT_VOLUNTEER_ID);
    }

}