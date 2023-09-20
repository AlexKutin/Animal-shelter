package pro.sky.animalshelter.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pro.sky.animalshelter.dto.ShelterDTO;
import pro.sky.animalshelter.dto.VolunteerDTO;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.exception.VolunteerNotFoundException;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.ShelterType;
import pro.sky.animalshelter.service.ShelterService;
import pro.sky.animalshelter.service.VolunteerService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static pro.sky.animalshelter.Constants.TextConstants.SHELTER_TYPE_NOT_FOUND_MESSAGE;
import static pro.sky.animalshelter.Constants.TextConstants.VOLUNTEER_BY_ID_NOT_FOUND_MESSAGE;

@ExtendWith(MockitoExtension.class)
class ShelterControllerTest {
    public static final Integer CAT_VOLUNTEER_PRESENT_ID = 61;
    public static final Integer CAT_VOLUNTEER_NOT_PRESENT_ID = 12;

    private static final String CAT_VOLUNTEER_NAME = "Cat Volunteer";
    private static final String CAT_VOLUNTEER_PHONE = "123";
    private static final String CAT_VOLUNTEER_TG_ADDR = "@Cat_Keeper";

    private Shelter dogShelter;
    private VolunteerDTO catVolunteerDTO;

    @Mock
    private ShelterService shelterService;

    @Mock
    private VolunteerService volunteerService;

    @InjectMocks
    private ShelterController shelterController;

    @BeforeEach
    public void init() {
        dogShelter = new Shelter();
        dogShelter.setShelterType(ShelterType.DOG_SHELTER);
        dogShelter.setId(2);
        dogShelter.setShelterName("Dog shelter");
        dogShelter.setShelterDescription("Dog shelter description");
        dogShelter.setShelterAddress("Dog address");

        catVolunteerDTO = new VolunteerDTO();
        catVolunteerDTO.setShelterType(ShelterType.CAT_SHELTER);
        catVolunteerDTO.setName(CAT_VOLUNTEER_NAME);
        catVolunteerDTO.setPhone(CAT_VOLUNTEER_PHONE);
        catVolunteerDTO.setTelegramAddress(CAT_VOLUNTEER_TG_ADDR);
        catVolunteerDTO.setActive(true);
    }

    @Test
    void editShelterByExistingShelterType() {
        ShelterType shelterType = ShelterType.CAT_SHELTER;
        ShelterDTO shelterDTO = new ShelterDTO();
        when(shelterService.editShelter(shelterType, shelterDTO)).thenReturn(shelterDTO);
        ResponseEntity<ShelterDTO> actual = shelterController.editShelterByShelterType(shelterType, shelterDTO);

        Mockito.verify(shelterService, times(1)).editShelter(shelterType, shelterDTO);
        Assertions.assertThat(actual).isEqualTo(ResponseEntity.ok(shelterDTO));
    }

    @Test
    void editShelterByNullShelterType() {
        ShelterDTO shelterDTO = new ShelterDTO();
        when(shelterService.editShelter(null, shelterDTO)).thenThrow(ShelterNotFoundException.class);

        ResponseEntity<ShelterDTO> actual = shelterController.editShelterByShelterType(null, shelterDTO);

        Mockito.verify(shelterService, times(1)).editShelter(null, shelterDTO);
        Assertions.assertThat(actual).isEqualTo(ResponseEntity.notFound().build());
    }

    @Test
    void getShelterInfoByShelterType() {
        ShelterType shelterType = ShelterType.DOG_SHELTER;
        ShelterDTO shelterDTO = ShelterDTO.fromShelter(dogShelter);
        when(shelterService.getShelterByShelterType(shelterType)).thenReturn(shelterDTO);
        ResponseEntity<ShelterDTO> actual = shelterController.getShelterInfoByShelterType(shelterType);
        Mockito.verify(shelterService, times(1)).getShelterByShelterType(shelterType);
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        assertEquals(actual.getBody(), shelterDTO);
    }

    @Test
    void getShelterInfoByShelterType_NotFound() {
        ShelterType shelterType = ShelterType.NOT_SUPPORTED;
        when(shelterService.getShelterByShelterType(shelterType)).
                thenThrow(new ShelterNotFoundException(String.format(SHELTER_TYPE_NOT_FOUND_MESSAGE, shelterType)));
        ResponseEntity<ShelterDTO> actual = shelterController.getShelterInfoByShelterType(shelterType);
        Mockito.verify(shelterService, times(1)).getShelterByShelterType(shelterType);
        Mockito.verifyNoMoreInteractions(shelterService);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    void getVolunteersByShelterType() {
        ShelterType shelterType = ShelterType.CAT_SHELTER;
        ResponseEntity<List<VolunteerDTO>> actual = shelterController.getVolunteersByShelterType(shelterType);
        Mockito.verify(shelterService, times(1)).getAllVolunteersByShelterType(shelterType);
        Mockito.verifyNoMoreInteractions(shelterService);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getVolunteersByShelterType_NotFound() {
        ShelterType shelterType = ShelterType.NOT_SUPPORTED;
        when(shelterService.getAllVolunteersByShelterType(shelterType)).
                thenThrow(new ShelterNotFoundException(String.format(SHELTER_TYPE_NOT_FOUND_MESSAGE, shelterType)));
        ResponseEntity<List<VolunteerDTO>> actual = shelterController.getVolunteersByShelterType(shelterType);
        Mockito.verify(shelterService, times(1)).getAllVolunteersByShelterType(shelterType);
        Mockito.verifyNoMoreInteractions(shelterService);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    void addVolunteerToShelter() {
        ResponseEntity<VolunteerDTO> actual = shelterController.addVolunteerToShelter(catVolunteerDTO);
        Mockito.verify(shelterService, times(1)).addVolunteerToShelter(catVolunteerDTO);
        Mockito.verifyNoMoreInteractions(shelterService);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void addVolunteerToShelter_NotFound() {
        catVolunteerDTO.setShelterType(ShelterType.NOT_SUPPORTED);
        when(shelterService.addVolunteerToShelter(catVolunteerDTO)).
                thenThrow(new ShelterNotFoundException(String.format(SHELTER_TYPE_NOT_FOUND_MESSAGE, catVolunteerDTO.getShelterType())));
        ResponseEntity<VolunteerDTO> actual = shelterController.addVolunteerToShelter(catVolunteerDTO);
        Mockito.verify(shelterService, times(1)).addVolunteerToShelter(catVolunteerDTO);
        Mockito.verifyNoMoreInteractions(shelterService);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    void blockVolunteerById() {
        ResponseEntity<VolunteerDTO> actual = shelterController.blockVolunteerById(CAT_VOLUNTEER_PRESENT_ID);
        Mockito.verify(volunteerService, times(1)).blockVolunteerById(CAT_VOLUNTEER_PRESENT_ID);
        Mockito.verifyNoMoreInteractions(volunteerService, shelterService);
        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void blockVolunteerById_NotFound() {
        Integer volunteerId = CAT_VOLUNTEER_NOT_PRESENT_ID;
        when(volunteerService.blockVolunteerById(volunteerId)).
                thenThrow(new VolunteerNotFoundException(String.format(VOLUNTEER_BY_ID_NOT_FOUND_MESSAGE, volunteerId)));

        ResponseEntity<VolunteerDTO> actual = shelterController.blockVolunteerById(volunteerId);
        Mockito.verify(volunteerService, times(1)).blockVolunteerById(volunteerId);
        Mockito.verifyNoMoreInteractions(volunteerService, shelterService);
        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void unlockVolunteerById() {
        ResponseEntity<VolunteerDTO> actual = shelterController.unlockVolunteerById(CAT_VOLUNTEER_PRESENT_ID);
        Mockito.verify(volunteerService, times(1)).unlockVolunteerById(CAT_VOLUNTEER_PRESENT_ID);
        Mockito.verifyNoMoreInteractions(volunteerService, shelterService);
        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void unlockVolunteerById_NotFound() {
        Integer volunteerId = CAT_VOLUNTEER_NOT_PRESENT_ID;
        when(volunteerService.unlockVolunteerById(volunteerId)).
                thenThrow(new VolunteerNotFoundException(String.format(VOLUNTEER_BY_ID_NOT_FOUND_MESSAGE, volunteerId)));

        ResponseEntity<VolunteerDTO> actual = shelterController.unlockVolunteerById(volunteerId);
        Mockito.verify(volunteerService, times(1)).unlockVolunteerById(volunteerId);
        Mockito.verifyNoMoreInteractions(volunteerService, shelterService);
        Assertions.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}