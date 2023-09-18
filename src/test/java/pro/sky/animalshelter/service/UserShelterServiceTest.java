package pro.sky.animalshelter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.dto.UserShelterDTO;
import pro.sky.animalshelter.exception.UserNotFoundException;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.UserCatShelterRepository;
import pro.sky.animalshelter.repository.UserDogShelterRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static pro.sky.animalshelter.Constants.TextConstants.*;

@ExtendWith(MockitoExtension.class)
class UserShelterServiceTest {
    public static final Integer USER_CAT_SHELTER_PRESENT_ID = 5;
    public static final Integer USER_CAT_SHELTER_NOT_PRESENT_ID = 25;
    public static final String USER_CAT_SHELTER_NAME = "Cat user 1";

    public static final Integer USER_DOG_SHELTER_PRESENT_ID = 15;
    public static final Integer USER_DOG_SHELTER_NOT_PRESENT_ID = 99;
    public static final String USER_DOG_SHELTER_NAME = "Dog user 1";

    public static final Integer CAT_PRESENT_ID = 7;
    public static final String CAT_NAME = "Cat user 1";

    public static final Integer DOG_PRESENT_ID = 17;
    public static final String DOG_NAME = "Dog 1";

    private UserCatShelter userCatShelter;
    private UserDogShelter userDogShelter;

    private Shelter catShelter;
    private Shelter dogShelter;

    @Mock
    private UserDogShelterRepository userDogShelterRepository;

    @Mock
    private  UserCatShelterRepository userCatShelterRepository;

    @Mock
    private ShelterService shelterService;

    @Captor
    private ArgumentCaptor<UserCatShelter> userCatShelterCaptor;

    @Captor
    private ArgumentCaptor<UserDogShelter> userDogShelterCaptor;

    @InjectMocks
    private UserShelterService userShelterService;

    @BeforeEach
    public void init() {
        userCatShelter = new UserCatShelter();
        userCatShelter.setUserId(USER_CAT_SHELTER_PRESENT_ID);
        userCatShelter.setUserName(USER_CAT_SHELTER_NAME);

        userDogShelter = new UserDogShelter();
        userDogShelter.setUserId(USER_DOG_SHELTER_PRESENT_ID);
        userDogShelter.setUserName(USER_DOG_SHELTER_NAME);

        Dog dog = new Dog();
        dog.setDogId(DOG_PRESENT_ID);
        dog.setDogName(DOG_NAME);

        Cat cat = new Cat();
        cat.setCatId(CAT_PRESENT_ID);
        cat.setCatName(CAT_NAME);

        catShelter = new Shelter();
        catShelter.setShelterType(ShelterType.CAT_SHELTER);
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
        dogShelter.setShelterContacts("Dog shelter contacts");
        dogShelter.setSecurityContacts("Dog shelter security contacts");
        dogShelter.setSafetyInfo("Dog safety info");
        dogShelter.setDrivingDirection("Dog shelter driven direction");
    }

    @Test
    public void saveUserContacts_UserDogShelter() {
        UserShelterDTO userShelterDTO = userDogShelter.toDTO();
        Long telegramId = userShelterDTO.getTelegramId();
        when(shelterService.findShelterByShelterType(ShelterType.DOG_SHELTER)).thenReturn(dogShelter);
        when(userDogShelterRepository.findByTelegramId(telegramId)).thenReturn(null);
        when(userDogShelterRepository.save(userDogShelter)).thenReturn(userDogShelter);

        userShelterService.saveUserContacts(userShelterDTO);
        Mockito.verify(shelterService, times(1)).findShelterByShelterType(ShelterType.DOG_SHELTER);
        Mockito.verify(userDogShelterRepository, times(1)).findByTelegramId(telegramId);
        Mockito.verify(userDogShelterRepository, times(1)).save(userDogShelterCaptor.capture());
        assertEquals(userDogShelterCaptor.getValue().getShelter(), dogShelter);
        Mockito.verifyNoMoreInteractions(shelterService, userDogShelterRepository);
    }

    @Test
    public void saveUserContacts_UserCatShelter() {
        UserShelterDTO userShelterDTO = userCatShelter.toDTO();
        Long telegramId = userShelterDTO.getTelegramId();
        when(shelterService.findShelterByShelterType(ShelterType.CAT_SHELTER)).thenReturn(catShelter);
        when(userCatShelterRepository.findByTelegramId(telegramId)).thenReturn(null);
        when(userCatShelterRepository.save(userCatShelter)).thenReturn(userCatShelter);

        userShelterService.saveUserContacts(userShelterDTO);
        Mockito.verify(shelterService, times(1)).findShelterByShelterType(ShelterType.CAT_SHELTER);
        Mockito.verify(userCatShelterRepository, times(1)).findByTelegramId(telegramId);
        Mockito.verify(userCatShelterRepository, times(1)).save(userCatShelterCaptor.capture());
        assertEquals(userCatShelterCaptor.getValue().getShelter(), catShelter);
        Mockito.verifyNoMoreInteractions(shelterService, userCatShelterRepository);
    }

    @Test
    void getAllUsersByShelterType_DogShelter() {
        when(shelterService.findShelterByShelterType(ShelterType.DOG_SHELTER)).thenReturn(dogShelter);
        userShelterService.getAllUsersByShelterType(ShelterType.DOG_SHELTER);
        Mockito.verify(shelterService, times(1)).findShelterByShelterType(ShelterType.DOG_SHELTER);
        Mockito.verify(userDogShelterRepository, times(1)).findAllByShelter(dogShelter);
        Mockito.verifyNoMoreInteractions(shelterService, userDogShelterRepository);
    }

    @Test
    void getAllUsersByShelterType_CatShelter() {
        when(shelterService.findShelterByShelterType(ShelterType.CAT_SHELTER)).thenReturn(catShelter);
        userShelterService.getAllUsersByShelterType(ShelterType.CAT_SHELTER);
        Mockito.verify(shelterService, times(1)).findShelterByShelterType(ShelterType.CAT_SHELTER);
        Mockito.verify(userCatShelterRepository, times(1)).findAllByShelter(catShelter);
        Mockito.verifyNoMoreInteractions(shelterService, userCatShelterRepository);
    }

    @Test
    void findUserCatShelterById_Present() {
        when(userCatShelterRepository.findById(USER_CAT_SHELTER_PRESENT_ID)).thenReturn(Optional.of(userCatShelter));
        userShelterService.findUserCatShelterById(USER_CAT_SHELTER_PRESENT_ID);
        Mockito.verify(userCatShelterRepository, times(1)).findById(USER_CAT_SHELTER_PRESENT_ID);
        Mockito.verifyNoMoreInteractions(userCatShelterRepository);
    }

    @Test
    void findUserCatShelterById_NotFound() {
        when(userCatShelterRepository.findById(USER_CAT_SHELTER_NOT_PRESENT_ID)).thenThrow(
                new UserNotFoundException(String.format(CAT_SHELTER_USER_BY_ID_NOT_FOUND_MESSAGE, USER_CAT_SHELTER_NOT_PRESENT_ID)));

        UserNotFoundException thrown = Assertions.assertThrows(
                UserNotFoundException.class, () -> userShelterService.findUserCatShelterById(USER_CAT_SHELTER_NOT_PRESENT_ID));
        Mockito.verify(userCatShelterRepository, times(1)).findById(USER_CAT_SHELTER_NOT_PRESENT_ID);
        assertEquals(thrown.getMessage(), String.format(CAT_SHELTER_USER_BY_ID_NOT_FOUND_MESSAGE, USER_CAT_SHELTER_NOT_PRESENT_ID));
        Mockito.verifyNoMoreInteractions(userCatShelterRepository);
    }

    @Test
    void findUserDogShelterById_Present() {
        when(userDogShelterRepository.findById(USER_DOG_SHELTER_PRESENT_ID)).thenReturn(Optional.of(userDogShelter));
        userShelterService.findUserDogShelterById(USER_DOG_SHELTER_PRESENT_ID);
        Mockito.verify(userDogShelterRepository, times(1)).findById(USER_DOG_SHELTER_PRESENT_ID);
        Mockito.verifyNoMoreInteractions(userDogShelterRepository);
    }

    @Test
    void findUserDogShelterById_NotFound() {
        when(userDogShelterRepository.findById(USER_DOG_SHELTER_NOT_PRESENT_ID)).thenThrow(
                new UserNotFoundException(String.format(DOG_SHELTER_USER_BY_ID_NOT_FOUND_MESSAGE, USER_DOG_SHELTER_NOT_PRESENT_ID)));

        UserNotFoundException thrown = Assertions.assertThrows(
                UserNotFoundException.class, () -> userShelterService.findUserDogShelterById(USER_DOG_SHELTER_NOT_PRESENT_ID));
        Mockito.verify(userDogShelterRepository, times(1)).findById(USER_DOG_SHELTER_NOT_PRESENT_ID);
        assertEquals(thrown.getMessage(), String.format(DOG_SHELTER_USER_BY_ID_NOT_FOUND_MESSAGE, USER_DOG_SHELTER_NOT_PRESENT_ID));
        Mockito.verifyNoMoreInteractions(userDogShelterRepository);
    }
}