package pro.sky.animalshelter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.dto.AnimalAdopterDTO;
import pro.sky.animalshelter.dto.CatDTO;
import pro.sky.animalshelter.dto.DogDTO;
import pro.sky.animalshelter.exception.DuplicateAdopterException;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.CatAdopterRepository;
import pro.sky.animalshelter.repository.CatsRepository;
import pro.sky.animalshelter.repository.DogAdopterRepository;
import pro.sky.animalshelter.repository.DogsRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static pro.sky.animalshelter.Constants.TextConstants.CAT_ADOPTER_DUPLICATION_MESSAGE;
import static pro.sky.animalshelter.Constants.TextConstants.DOG_ADOPTER_DUPLICATION_MESSAGE;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {
    public static final Integer CAT_ADOPTER_PRESENT_ID = 1;
    public static final Integer DOG_ADOPTER_PRESENT_ID = 2;

    public static final Integer USER_CAT_SHELTER_PRESENT_ID = 5;
    public static final String USER_CAT_SHELTER_NAME = "Cat user 1";

    public static final Integer USER_DOG_SHELTER_PRESENT_ID = 15;
    public static final String USER_DOG_SHELTER_NAME = "Dog user 1";

    public static final Integer CAT_PRESENT_ID = 7;
    public static final String CAT_NAME = "Cat user 1";

    public static final Integer DOG_PRESENT_ID = 17;
    public static final String DOG_NAME = "Dog 1";

    private Shelter catShelter;
    private Shelter dogShelter;
    private Dog dog;
    private Cat cat;
    private UserDogShelter userDogShelter;
    private UserCatShelter userCatShelter;
    private CatAdopter catAdopter;
    private DogAdopter dogAdopter;

    @Captor
    private ArgumentCaptor<Dog> dogCaptor;

    @Captor
    private ArgumentCaptor<Cat> catCaptor;

    @Captor
    private ArgumentCaptor<DogAdopter> dogAdopterCaptor;

    @Captor
    private ArgumentCaptor<CatAdopter> catAdopterCaptor;

    @Mock
    private ShelterService shelterService;

    @Mock
    private DogsRepository dogsRepository;

    @Mock
    private CatsRepository catsRepository;

    @Mock
    private DogAdopterRepository dogAdopterRepository;

    @Mock
    private CatAdopterRepository catAdopterRepository;

    @Mock
    private NotificationTaskService notificationTaskService;

    @Mock
    private UserShelterService userShelterService;

    @InjectMocks
    private AnimalService animalService;

    @BeforeEach
    public void init() {
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

        userCatShelter = new UserCatShelter();
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
    public void saveDogToDb() {
        DogDTO dogDTO = dog.toDTO();
        when(shelterService.findShelterByShelterType(ShelterType.DOG_SHELTER)).thenReturn(dogShelter);
        when(dogsRepository.save(any(Dog.class))).thenReturn(dog);
        animalService.saveDogToDb(dogDTO);
        Mockito.verify(shelterService, times(1)).findShelterByShelterType(ShelterType.DOG_SHELTER);
        Mockito.verify(dogsRepository, times(1)).save(dogCaptor.capture());
        Dog dogSaved = dogCaptor.getValue();
        assertEquals(dogSaved.getShelter(), dogShelter);
        Mockito.verifyNoMoreInteractions(shelterService, dogsRepository);
    }

    @Test
    public void getAllDogs() {
        animalService.getAllDogs();
        Mockito.verify(dogsRepository, times(1)).findAll();
        Mockito.verifyNoMoreInteractions(shelterService, dogsRepository);
    }

    @Test
    public void getAllCats() {
        animalService.getAllCats();
        Mockito.verify(catsRepository, times(1)).findAll();
        Mockito.verifyNoMoreInteractions(shelterService, catsRepository);
    }

    @Test
    public void saveCatToDb() {
        CatDTO catDTO = cat.toDTO();
        when(shelterService.findShelterByShelterType(ShelterType.CAT_SHELTER)).thenReturn(catShelter);
        when(catsRepository.save(any(Cat.class))).thenReturn(cat);
        animalService.saveCatToDb(catDTO);
        Mockito.verify(shelterService, times(1)).findShelterByShelterType(ShelterType.CAT_SHELTER);
        Mockito.verify(catsRepository, times(1)).save(catCaptor.capture());
        Cat catSaved = catCaptor.getValue();
        assertEquals(catSaved.getShelter(), catShelter);
        Mockito.verifyNoMoreInteractions(shelterService, catsRepository);
    }

    @Test
    public void saveDogAdopter_alreadyPresent() {
        AnimalAdopterDTO dogAdopterDTO = dogAdopter.toDTO();
        when(dogAdopterRepository.isPresentDogAdopterByUserAndDog(
                dogAdopterDTO.getUserId(), dogAdopterDTO.getAnimalId())).thenReturn(true);

        DuplicateAdopterException thrown = Assertions.assertThrows(
                DuplicateAdopterException.class, () -> animalService.saveDogAdopter(dogAdopterDTO));
        String message = String.format(DOG_ADOPTER_DUPLICATION_MESSAGE,  dogAdopterDTO.getUserId(), dogAdopterDTO.getAnimalId());
        assertEquals(thrown.getMessage(), message);
    }

    @Test
    public void saveDogAdopter_new() {
        AnimalAdopterDTO dogAdopterDTO = dogAdopter.toDTO();
        Integer userId = dogAdopterDTO.getUserId();
        Integer animalId = dogAdopterDTO.getAnimalId();
        when(dogAdopterRepository.isPresentDogAdopterByUserAndDog(userId, animalId)).thenReturn(false);
        when(userShelterService.findUserDogShelterById(userId)).thenReturn(userDogShelter);
        when(dogsRepository.findById(dog.getDogId())).thenReturn(Optional.of(dog));
        when(dogAdopterRepository.save(any())).thenReturn(dogAdopter);

        animalService.saveDogAdopter(dogAdopterDTO);
        Mockito.verify(dogAdopterRepository).
                isPresentDogAdopterByUserAndDog(userId, animalId);
        Mockito.verify(userShelterService, times(1)).findUserDogShelterById(userId);
        Mockito.verify(dogsRepository, times(1)).findById(animalId);
        Mockito.verify(dogAdopterRepository, times(1)).save(dogAdopterCaptor.capture());
        Mockito.verify(notificationTaskService, times(1)).
                probationActiveNotification(dogAdopter, ShelterType.DOG_SHELTER);
        Mockito.verifyNoMoreInteractions(dogAdopterRepository, userShelterService, dogsRepository, notificationTaskService);
        DogAdopter dogAdopterArg = dogAdopterCaptor.getValue();
        assertEquals(dogAdopterArg.getAdoptionDate().toLocalDate(), LocalDate.now());
    }

    @Test
    public void saveCatAdopter_alreadyPresent() {
        AnimalAdopterDTO catAdopterDTO = catAdopter.toDTO();
        when(catAdopterRepository.isPresentCatAdopterByUserAndCat(
                catAdopterDTO.getUserId(), catAdopterDTO.getAnimalId())).thenReturn(true);

        DuplicateAdopterException thrown = Assertions.assertThrows(
                DuplicateAdopterException.class, () -> animalService.saveCatAdopter(catAdopterDTO));
        String message = String.format(CAT_ADOPTER_DUPLICATION_MESSAGE,  catAdopterDTO.getUserId(), catAdopterDTO.getAnimalId());
        assertEquals(thrown.getMessage(), message);
    }

    @Test
    public void saveCatAdopter_new() {
        AnimalAdopterDTO catAdopterDTO = catAdopter.toDTO();
        Integer userId = catAdopterDTO.getUserId();
        Integer catId = catAdopterDTO.getAnimalId();
        when(catAdopterRepository.isPresentCatAdopterByUserAndCat(userId, catId)).thenReturn(false);
        when(userShelterService.findUserCatShelterById(userId)).thenReturn(userCatShelter);
        when(catsRepository.findById(cat.getCatId())).thenReturn(Optional.of(cat));
        when(catAdopterRepository.save(any())).thenReturn(catAdopter);

        animalService.saveCatAdopter(catAdopterDTO);
        Mockito.verify(catAdopterRepository).isPresentCatAdopterByUserAndCat(userId, catId);
        Mockito.verify(userShelterService, times(1)).findUserCatShelterById(userId);
        Mockito.verify(catsRepository, times(1)).findById(catId);
        Mockito.verify(catAdopterRepository, times(1)).save(catAdopterCaptor.capture());
        Mockito.verify(notificationTaskService, times(1)).
                probationActiveNotification(catAdopter, ShelterType.CAT_SHELTER);
        Mockito.verifyNoMoreInteractions(catAdopterRepository, userShelterService, catsRepository, notificationTaskService);
        CatAdopter catAdopterArg = catAdopterCaptor.getValue();
        assertEquals(catAdopterArg.getAdoptionDate().toLocalDate(), LocalDate.now());
    }

    @Test
    public void getDogAdopters_withoutAdopterStatus() {
        animalService.getDogAdopters(null);
        Mockito.verify(dogAdopterRepository, times(1)).findAll();
        Mockito.verifyNoMoreInteractions(dogAdopterRepository);
    }

    @Test
    public void getDogAdopters_withAdopterStatus() {
        AdopterStatus adopterStatus = AdopterStatus.WAITING_REPORT;
        animalService.getDogAdopters(adopterStatus);
        Mockito.verify(dogAdopterRepository, times(1)).findAllByAdopterStatusOrderByUser(adopterStatus);
        Mockito.verifyNoMoreInteractions(dogAdopterRepository);
    }

    @Test
    public void getCatAdopters_withoutAdopterStatus() {
        animalService.getCatAdopters(null);
        Mockito.verify(catAdopterRepository, times(1)).findAll();
        Mockito.verifyNoMoreInteractions(catAdopterRepository);
    }

    @Test
    public void getCatAdopters_withAdopterStatus() {
        AdopterStatus adopterStatus = AdopterStatus.PROBATION_REJECT;
        animalService.getCatAdopters(adopterStatus);
        Mockito.verify(catAdopterRepository, times(1)).findAllByAdopterStatusOrderByUser(adopterStatus);
        Mockito.verifyNoMoreInteractions(catAdopterRepository);
    }
}