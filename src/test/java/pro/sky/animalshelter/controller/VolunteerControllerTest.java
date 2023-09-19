package pro.sky.animalshelter.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pro.sky.animalshelter.dto.AnimalAdopterDTO;
import pro.sky.animalshelter.dto.CatDTO;
import pro.sky.animalshelter.dto.DogDTO;
import pro.sky.animalshelter.dto.UserShelterDTO;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.service.AdopterService;
import pro.sky.animalshelter.service.AnimalService;
import pro.sky.animalshelter.service.UserShelterService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static pro.sky.animalshelter.Constants.TextConstants.SHELTER_TYPE_NOT_SUPPORTED_MESSAGE;

@ExtendWith(MockitoExtension.class)
class VolunteerControllerTest {
    public static final Integer USER_CAT_SHELTER_PRESENT_ID = 5;
    public static final String USER_CAT_SHELTER_NAME = "Cat user 1";

    public static final Integer USER_DOG_SHELTER_PRESENT_ID = 15;
    public static final String USER_DOG_SHELTER_NAME = "Dog user 1";

    public static final Integer CAT_ADOPTER_PRESENT_ID = 1;
    public static final Integer DOG_ADOPTER_PRESENT_ID = 2;

    public static final Integer CAT_PRESENT_ID = 7;
    public static final String CAT_NAME = "Cat user 1";

    public static final Integer DOG_PRESENT_ID = 17;
    public static final String DOG_NAME = "Dog 1";

    @Captor
    private ArgumentCaptor<DogDTO> dogDTOCaptor;

    @Captor
    private ArgumentCaptor<CatDTO> catDTOCaptor;

    @Captor
    private ArgumentCaptor<AnimalAdopterDTO> animalAdopterDTOCaptor;

    @Mock
    private UserShelterService userShelterService;

    @Mock
    private AnimalService animalService;

    @Mock
    private AdopterService adopterService;

    @InjectMocks
    private VolunteerController volunteerController;

    private UserDogShelter userDogShelter;
    private CatAdopter catAdopter;
    private DogAdopter dogAdopter;

    private Dog dog;
    private Cat cat;

    @BeforeEach
    public void init() {
        UserCatShelter userCatShelter = new UserCatShelter();
        userCatShelter.setUserId(USER_CAT_SHELTER_PRESENT_ID);
        userCatShelter.setUserName(USER_CAT_SHELTER_NAME);

        userDogShelter = new UserDogShelter();
        userDogShelter.setUserId(USER_DOG_SHELTER_PRESENT_ID);
        userDogShelter.setUserName(USER_DOG_SHELTER_NAME);

        dog = new Dog();
        dog.setDogId(DOG_PRESENT_ID);
        dog.setDogName(DOG_NAME);
        dog.setAge(5);

        cat = new Cat();
        cat.setCatId(CAT_PRESENT_ID);
        cat.setCatName(CAT_NAME);
        cat.setAge(2);

        catAdopter = new CatAdopter();
        catAdopter.setAdopterId(CAT_ADOPTER_PRESENT_ID);
        catAdopter.setChatId(812818905L);
        catAdopter.setAdopterStatus(AdopterStatus.PROBATION_ACTIVE);
        catAdopter.setAdoptionDate(LocalDateTime.of(2023, 9, 1, 12, 0));
        catAdopter.setEndProbationDate(catAdopter.getAdoptionDate().plusMonths(1));
        catAdopter.setUser(userCatShelter);
        catAdopter.setCat(cat);

        dogAdopter = new DogAdopter();
        dogAdopter.setAdopterId(DOG_ADOPTER_PRESENT_ID);
        dogAdopter.setChatId(812818905L);
        dogAdopter.setAdopterStatus(AdopterStatus.PROBATION_ACTIVE);
        dogAdopter.setAdoptionDate(LocalDateTime.of(2023, 9, 1, 12, 0));
        dogAdopter.setEndProbationDate(catAdopter.getAdoptionDate().plusMonths(1));
        dogAdopter.setUser(userDogShelter);
        dogAdopter.setDog(dog);
    }

    @Test
    public void getAllRegisteredUsersByShelter() {
        when(userShelterService.getAllUsersByShelterType(ShelterType.DOG_SHELTER)).
                thenReturn(List.of(userDogShelter.toDTO()));
        ResponseEntity<List<UserShelterDTO>> actualResponse = volunteerController.getAllRegisteredUsersByShelter(ShelterType.DOG_SHELTER);
        Mockito.verify(userShelterService, times(1)).getAllUsersByShelterType(ShelterType.DOG_SHELTER);
        assertEquals(actualResponse.getStatusCode(), HttpStatus.OK);
        Mockito.verifyNoMoreInteractions(userShelterService);
    }

    @Test
    public void getAllRegisteredUsersByShelter_NotFound() {
        ShelterType shelterType = ShelterType.NOT_SUPPORTED;
        when(userShelterService.getAllUsersByShelterType(shelterType)).
                thenThrow(new ShelterNotFoundException(String.format(SHELTER_TYPE_NOT_SUPPORTED_MESSAGE, shelterType)));
        ResponseEntity<List<UserShelterDTO>> actualResponse = volunteerController.getAllRegisteredUsersByShelter(shelterType);
        Mockito.verify(userShelterService, times(1)).getAllUsersByShelterType(shelterType);
        assertEquals(actualResponse.getStatusCode(), HttpStatus.NOT_FOUND);
        Mockito.verifyNoMoreInteractions(userShelterService);
    }

    @Test
    public void getAllDogs() {
        when(animalService.getAllDogs()).thenReturn(List.of(dog.toDTO()));
        ResponseEntity<List<DogDTO>> actual = volunteerController.getAllDogs();
        Mockito.verify(animalService, times(1)).getAllDogs();
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        Mockito.verifyNoMoreInteractions(animalService);
    }

    @Test
    public void getAllCats() {
        when(animalService.getAllCats()).thenReturn(List.of(cat.toDTO()));
        ResponseEntity<List<CatDTO>> actual = volunteerController.getAllCats();
        Mockito.verify(animalService, times(1)).getAllCats();
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        Mockito.verifyNoMoreInteractions(animalService);
    }

    @Test
    public void saveDogToDb() {
        DogDTO dogDTO = dog.toDTO();
        when(animalService.saveDogToDb(any(DogDTO.class))).thenReturn(dogDTO);
        ResponseEntity<DogDTO> actual = volunteerController.saveDogToDb(dogDTO);
        Mockito.verify(animalService, times(1)).saveDogToDb(dogDTOCaptor.capture());
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        assertEquals(dogDTOCaptor.getValue(), dogDTO);
        Mockito.verifyNoMoreInteractions(animalService);
    }

    @Test
    public void saveCatToDb() {
        CatDTO catDTO = cat.toDTO();
        when(animalService.saveCatToDb(any(CatDTO.class))).thenReturn(catDTO);
        ResponseEntity<CatDTO> actual = volunteerController.saveCatToDb(catDTO);
        Mockito.verify(animalService, times(1)).saveCatToDb(catDTOCaptor.capture());
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        assertEquals(catDTOCaptor.getValue(), catDTO);
        Mockito.verifyNoMoreInteractions(animalService);
    }

    @Test
    public void saveDogAdopter() {
        AnimalAdopterDTO dogAdopterDTO = dogAdopter.toDTO();
        ResponseEntity<AnimalAdopterDTO> actual = volunteerController.saveDogAdopter(dogAdopterDTO);
        Mockito.verify(animalService, times(1)).saveDogAdopter(animalAdopterDTOCaptor.capture());
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        assertEquals(animalAdopterDTOCaptor.getValue().getShelterType(), ShelterType.DOG_SHELTER);
    }

    @Test
    public void getDogAdopters() {
        AdopterStatus adopterStatus = AdopterStatus.PROBATION_ACTIVE;
        when(animalService.getDogAdopters(adopterStatus)).thenReturn(List.of(dogAdopter.toDTO()));
        ResponseEntity<List<AnimalAdopterDTO>> actual = volunteerController.getDogAdopters(adopterStatus);
        Mockito.verify(animalService, times(1)).getDogAdopters(adopterStatus);
        Mockito.verifyNoMoreInteractions(animalService);
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(actual.getBody()).contains(dogAdopter.toDTO()));
    }

    @Test
    public void saveProbationStatusDogAdopter() {
        ProbationStatus probationStatus = ProbationStatus.PROBATION_REJECT;
        AnimalAdopterDTO animalAdopterDTO = dogAdopter.toDTO();
        when(adopterService.processProbationStatusForDogAdopter(DOG_ADOPTER_PRESENT_ID, probationStatus)).
                thenReturn(animalAdopterDTO);
        ResponseEntity<AnimalAdopterDTO> actual = volunteerController.saveProbationStatusDogAdopter(
                DOG_ADOPTER_PRESENT_ID, probationStatus);
        Mockito.verify(adopterService, times(1)).
                processProbationStatusForDogAdopter(DOG_ADOPTER_PRESENT_ID, probationStatus);
        Mockito.verifyNoMoreInteractions(adopterService);
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void saveCatAdopter() {
        AnimalAdopterDTO catAdopterDTO = catAdopter.toDTO();
        ResponseEntity<AnimalAdopterDTO> actual = volunteerController.saveCatAdopter(catAdopterDTO);
        Mockito.verify(animalService, times(1)).saveCatAdopter(animalAdopterDTOCaptor.capture());
        Mockito.verifyNoMoreInteractions(animalService);
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        assertEquals(animalAdopterDTOCaptor.getValue().getShelterType(), ShelterType.CAT_SHELTER);
    }

    @Test
    public void getCatAdopters() {
        AdopterStatus adopterStatus = AdopterStatus.PROBATION_PASSED;
        when(animalService.getCatAdopters(adopterStatus)).thenReturn(List.of(catAdopter.toDTO()));
        ResponseEntity<List<AnimalAdopterDTO>> actual = volunteerController.getCatAdopters(adopterStatus);
        Mockito.verify(animalService, times(1)).getCatAdopters(adopterStatus);
        Mockito.verifyNoMoreInteractions(animalService);
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.requireNonNull(actual.getBody()).contains(catAdopter.toDTO()));
    }

    @Test
    public void saveProbationStatusCatAdopter() {
        ProbationStatus probationStatus = ProbationStatus.PROBATION_ADD_30;
        AnimalAdopterDTO animalAdopterDTO = catAdopter.toDTO();
        when(adopterService.processProbationStatusForCatAdopter(CAT_ADOPTER_PRESENT_ID, probationStatus)).
                thenReturn(animalAdopterDTO);
        ResponseEntity<AnimalAdopterDTO> actual = volunteerController.saveProbationStatusCatAdopter(
                CAT_ADOPTER_PRESENT_ID, probationStatus);
        Mockito.verify(adopterService, times(1)).
                processProbationStatusForCatAdopter(CAT_ADOPTER_PRESENT_ID, probationStatus);
        Mockito.verifyNoMoreInteractions(adopterService);
        assertEquals(actual.getStatusCode(), HttpStatus.OK);
    }

}