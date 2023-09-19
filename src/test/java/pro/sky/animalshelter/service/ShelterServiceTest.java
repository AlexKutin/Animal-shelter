package pro.sky.animalshelter.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.dto.ShelterDTO;
import pro.sky.animalshelter.dto.VolunteerDTO;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.model.Volunteer;
import pro.sky.animalshelter.repository.ShelterRepository;
import pro.sky.animalshelter.repository.VolunteerRepository;

import java.util.Collections;
import java.util.List;

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
    private Shelter dogShelter;
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
        catShelter.setShelterAddress("Cat shelter address");
        catShelter.setShelterContacts("Cat shelter contacts");
        catShelter.setSecurityContacts("Cat shelter security contacts");
        catShelter.setSafetyInfo("Cat safety info");
        catShelter.setDrivingDirection("Cat shelter driven direction");

        dogShelter = new Shelter();
        dogShelter.setShelterType(ShelterType.DOG_SHELTER);
        dogShelter.setId(2);
        dogShelter.setShelterName("Dog shelter");
        dogShelter.setShelterDescription("Dog shelter description");
        dogShelter.setShelterAddress("Dog address");

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

    @Test
    void getAllVolunteersByShelterType() {
        Volunteer volunteer = catVolunteerDTO.toVolunteer(catShelter);
        catShelter.getVolunteers().add(volunteer);
        when(shelterRepository.findSheltersByShelterType(ShelterType.CAT_SHELTER)).thenReturn(Collections.singletonList(catShelter));
        List<VolunteerDTO> volunteers = shelterService.getAllVolunteersByShelterType(ShelterType.CAT_SHELTER);
        Mockito.verify(shelterRepository, Mockito.times(1)).findSheltersByShelterType(CAT_SHELTER_TYPE);
        Assertions.assertThat(volunteers.size()).isEqualTo(1);
    }

    @Test
    public void getDogShelter() {
        when(shelterRepository.findSheltersByShelterType(ShelterType.DOG_SHELTER)).thenReturn(Collections.singletonList(dogShelter));
        ShelterDTO shelterDTO = shelterService.getDogShelter();
        Mockito.verify(shelterRepository, Mockito.times(1)).findSheltersByShelterType(ShelterType.DOG_SHELTER);

        Assertions.assertThat(shelterDTO.getId()).isEqualTo(dogShelter.getId());
        Assertions.assertThat(shelterDTO.getShelterName()).isEqualTo(dogShelter.getShelterName());
        Assertions.assertThat(shelterDTO.getShelterDescription()).isEqualTo(dogShelter.getShelterDescription());
    }

    @Test
    public void getCatShelter() {
        when(shelterRepository.findSheltersByShelterType(ShelterType.CAT_SHELTER)).thenReturn(Collections.singletonList(catShelter));
        ShelterDTO shelterDTO = shelterService.getCatShelter();
        Mockito.verify(shelterRepository, Mockito.times(1)).findSheltersByShelterType(CAT_SHELTER_TYPE);

        Assertions.assertThat(shelterDTO.getId()).isEqualTo(catShelter.getId());
        Assertions.assertThat(shelterDTO.getShelterName()).isEqualTo(catShelter.getShelterName());
        Assertions.assertThat(shelterDTO.getShelterDescription()).isEqualTo(catShelter.getShelterDescription());
    }

    @Test
    void editShelter() {
        String newContacts = "8-900-100-0000";
        String newDescription = "New Cat shelter";
        when(shelterRepository.findSheltersByShelterType(ShelterType.CAT_SHELTER)).thenReturn(Collections.singletonList(catShelter));
        ShelterDTO shelterDTO = ShelterDTO.fromShelter(catShelter);
        shelterDTO.setShelterContacts(newContacts);
        shelterDTO.setShelterDescription(newDescription);

        catShelter.setShelterContacts(newContacts);
        catShelter.setShelterDescription(newDescription);
        when(shelterRepository.save(catShelter)).thenReturn(catShelter);

        shelterDTO = shelterService.editShelter(ShelterType.CAT_SHELTER, shelterDTO);
        Mockito.verify(shelterRepository, Mockito.times(1)).save(catShelter);
        Assertions.assertThat(shelterDTO).isEqualTo(ShelterDTO.fromShelter(catShelter));
    }

    @Test
    void getShelterByShelterType() {
        when(shelterRepository.findSheltersByShelterType(ShelterType.CAT_SHELTER)).thenReturn(Collections.singletonList(catShelter));
        shelterService.getShelterByShelterType(ShelterType.CAT_SHELTER);
        Mockito.verify(shelterRepository, Mockito.times(1)).findSheltersByShelterType(ShelterType.CAT_SHELTER);
    }

}