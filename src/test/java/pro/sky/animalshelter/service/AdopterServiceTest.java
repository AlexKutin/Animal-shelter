package pro.sky.animalshelter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.dto.AnimalAdopterDTO;
import pro.sky.animalshelter.exception.AdopterNotFoundException;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.CatAdopterRepository;
import pro.sky.animalshelter.repository.DogAdopterRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static pro.sky.animalshelter.Constants.TextConstants.*;
import static pro.sky.animalshelter.timer.checkSkippedReports.SKIPPED_DAYS_SIGNAL_VOLUNTEER;
import static pro.sky.animalshelter.timer.checkSkippedReports.SKIPPED_DAYS_WARNING;

@ExtendWith(MockitoExtension.class)
class AdopterServiceTest {
    public static final Integer CAT_ADOPTER_PRESENT_ID = 1;
    public static final Integer DOG_ADOPTER_PRESENT_ID = 2;
    public static final Integer CAT_ADOPTER_NOT_PRESENT_ID = 10;
    public static final Integer DOG_ADOPTER_NOT_PRESENT_ID = 15;

    public static final Integer USER_CAT_SHELTER_PRESENT_ID = 5;
    public static final String USER_CAT_SHELTER_NAME = "Cat user 1";

    public static final Integer USER_DOG_SHELTER_PRESENT_ID = 15;
    public static final String USER_DOG_SHELTER_NAME = "Dog user 1";

    public static final Integer CAT_PRESENT_ID = 7;
    public static final String CAT_NAME = "Cat user 1";

    public static final Integer DOG_PRESENT_ID = 17;
    public static final String DOG_NAME = "Dog 1";

    public static final Long DOG_ADOPTER_PRESENT_CHAT_ID = 812818905L;
    public static final Long DOG_ADOPTER_NOT_PRESENT_CHAT_ID = 123456789L;
    public static final Long CAT_ADOPTER_PRESENT_CHAT_ID = 812818900L;
    public static final Long CAT_ADOPTER_NOT_PRESENT_CHAT_ID = 123456780L;

    @Mock
    CatAdopterRepository catAdopterRepository;

    @Mock
    DogAdopterRepository dogAdopterRepository;

    @Mock
    NotificationTaskService notificationTaskService;

    @Mock
    VolunteerService volunteerService;

    @Captor
    ArgumentCaptor<Long> valueCaptor;

    @InjectMocks
    AdopterService adopterService;

    private CatAdopter catAdopter;
    private DogAdopter dogAdopter;
    private List<Volunteer> volunteers;

    @BeforeEach
    public void init() {
        UserCatShelter userCatShelter = new UserCatShelter();
        userCatShelter.setUserId(USER_CAT_SHELTER_PRESENT_ID);
        userCatShelter.setUserName(USER_CAT_SHELTER_NAME);

        UserDogShelter userDogShelter = new UserDogShelter();
        userDogShelter.setUserId(USER_DOG_SHELTER_PRESENT_ID);
        userDogShelter.setUserName(USER_DOG_SHELTER_NAME);

        Dog dog = new Dog();
        dog.setDogId(DOG_PRESENT_ID);
        dog.setDogName(DOG_NAME);

        Cat cat = new Cat();
        cat.setCatId(CAT_PRESENT_ID);
        cat.setCatName(CAT_NAME);

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

        volunteers = new ArrayList<>();
        volunteers.add(new Volunteer(100, "Mike", true, 123456789L));
        volunteers.add(new Volunteer(200, "Alex", true, 923456789L));
    }

    @Test
    public void findCatAdopterById() {
        when(catAdopterRepository.findById(CAT_ADOPTER_PRESENT_ID)).thenReturn(Optional.of(catAdopter));
        CatAdopter result = adopterService.findCatAdopterById(CAT_ADOPTER_PRESENT_ID);
        assertEquals(result, catAdopter);
        Mockito.verify(catAdopterRepository, times(1)).findById(CAT_ADOPTER_PRESENT_ID);

        when(catAdopterRepository.findById(CAT_ADOPTER_NOT_PRESENT_ID)).thenReturn(Optional.empty());
        AdopterNotFoundException thrown = Assertions.assertThrows(
                AdopterNotFoundException.class, () -> adopterService.findCatAdopterById(CAT_ADOPTER_NOT_PRESENT_ID));
        assertEquals(thrown.getMessage(), String.format(CAT_ADOPTER_BY_ID_NOT_FOUND, CAT_ADOPTER_NOT_PRESENT_ID));
    }

    @Test
    public void findDogAdopterById() {
        when(dogAdopterRepository.findById(DOG_ADOPTER_PRESENT_ID)).thenReturn(Optional.of(dogAdopter));
        DogAdopter result = adopterService.findDogAdopterById(DOG_ADOPTER_PRESENT_ID);
        assertEquals(result, dogAdopter);
        Mockito.verify(dogAdopterRepository, times(1)).findById(DOG_ADOPTER_PRESENT_ID);

        when(dogAdopterRepository.findById(DOG_ADOPTER_NOT_PRESENT_ID)).thenReturn(Optional.empty());
        AdopterNotFoundException thrown = Assertions.assertThrows(
                AdopterNotFoundException.class, () -> adopterService.findDogAdopterById(DOG_ADOPTER_NOT_PRESENT_ID));
        Mockito.verify(dogAdopterRepository, times(1)).findById(DOG_ADOPTER_NOT_PRESENT_ID);
        assertEquals(thrown.getMessage(), String.format(DOG_ADOPTER_BY_ID_NOT_FOUND, DOG_ADOPTER_NOT_PRESENT_ID));
    }

    @Test
    public void findDogAdopterByChatIdAndStatus() {
        List<AdopterStatus> oneStatusProbationActive = Collections.singletonList(AdopterStatus.PROBATION_ACTIVE);
        List<AdopterStatus> oneStatusProbationPassed = Collections.singletonList(AdopterStatus.PROBATION_PASSED);

        // Проверяем вызов для усыновителя с существующим chatId и корректным статусом
        when(dogAdopterRepository.findByChatIdAndAdopterStatusIn(
                DOG_ADOPTER_PRESENT_CHAT_ID, oneStatusProbationActive)).thenReturn(dogAdopter);
        DogAdopter result = adopterService.findDogAdopterByChatIdAndStatuses(
                DOG_ADOPTER_PRESENT_CHAT_ID, oneStatusProbationActive);
        assertEquals(result, dogAdopter);
        Mockito.verify(dogAdopterRepository, times(1)).
                findByChatIdAndAdopterStatusIn(DOG_ADOPTER_PRESENT_CHAT_ID, oneStatusProbationActive);

        // Проверяем вызов для усыновителя с несуществующим chatId и корректным статусом
        when(dogAdopterRepository.findByChatIdAndAdopterStatusIn(
                DOG_ADOPTER_NOT_PRESENT_CHAT_ID, oneStatusProbationActive)).thenReturn(null);
        AdopterNotFoundException thrown = Assertions.assertThrows(
                AdopterNotFoundException.class, () -> adopterService.findDogAdopterByChatIdAndStatuses(
                        DOG_ADOPTER_NOT_PRESENT_CHAT_ID, oneStatusProbationActive));
        assertEquals(thrown.getMessage(),
                String.format(DOG_ADOPTER_BY_CHAT_ID_AND_STATUS_NOT_FOUND, DOG_ADOPTER_NOT_PRESENT_CHAT_ID, oneStatusProbationActive));
        Mockito.verify(dogAdopterRepository, times(1)).
                findByChatIdAndAdopterStatusIn(DOG_ADOPTER_NOT_PRESENT_CHAT_ID, oneStatusProbationActive);

        // Проверяем вызов для усыновителя с существующим chatId и некорректным статусом
        when(dogAdopterRepository.findByChatIdAndAdopterStatusIn(
                DOG_ADOPTER_PRESENT_CHAT_ID, oneStatusProbationPassed)).thenReturn(null);
        thrown = Assertions.assertThrows(
                AdopterNotFoundException.class, () -> adopterService.findDogAdopterByChatIdAndStatuses(
                        DOG_ADOPTER_PRESENT_CHAT_ID, oneStatusProbationPassed));
        assertEquals(thrown.getMessage(),
                String.format(DOG_ADOPTER_BY_CHAT_ID_AND_STATUS_NOT_FOUND, DOG_ADOPTER_PRESENT_CHAT_ID, oneStatusProbationPassed));
        Mockito.verify(dogAdopterRepository, times(1)).
                findByChatIdAndAdopterStatusIn(DOG_ADOPTER_PRESENT_CHAT_ID, oneStatusProbationPassed);
    }

    @Test
    public void findCatAdopterByChatIdAndStatuses() {
        List<AdopterStatus> oneStatusProbationActive = Collections.singletonList(AdopterStatus.PROBATION_ACTIVE);
        List<AdopterStatus> oneStatusProbationPassed = Collections.singletonList(AdopterStatus.PROBATION_PASSED);

        // Проверяем вызов для усыновителя с существующим chatId и корректным статусом
        when(catAdopterRepository.findByChatIdAndAdopterStatusIn(
                CAT_ADOPTER_PRESENT_CHAT_ID, oneStatusProbationActive)).thenReturn(catAdopter);
        CatAdopter result = adopterService.findCatAdopterByChatIdAndStatuses(
                CAT_ADOPTER_PRESENT_CHAT_ID, oneStatusProbationActive);
        assertEquals(result, catAdopter);
        Mockito.verify(catAdopterRepository, times(1)).
                findByChatIdAndAdopterStatusIn(CAT_ADOPTER_PRESENT_CHAT_ID, oneStatusProbationActive);

        // Проверяем вызов для усыновителя с несуществующим chatId и корректным статусом
        when(catAdopterRepository.findByChatIdAndAdopterStatusIn(
                CAT_ADOPTER_NOT_PRESENT_CHAT_ID, oneStatusProbationActive)).thenReturn(null);
        AdopterNotFoundException thrown = Assertions.assertThrows(
                AdopterNotFoundException.class, () -> adopterService.findCatAdopterByChatIdAndStatuses(
                        CAT_ADOPTER_NOT_PRESENT_CHAT_ID, oneStatusProbationActive));
        assertEquals(thrown.getMessage(),
                String.format(CAT_ADOPTER_BY_CHAT_ID_AND_STATUS_NOT_FOUND, CAT_ADOPTER_NOT_PRESENT_CHAT_ID, oneStatusProbationActive));
        Mockito.verify(catAdopterRepository, times(1)).
                findByChatIdAndAdopterStatusIn(CAT_ADOPTER_NOT_PRESENT_CHAT_ID, oneStatusProbationActive);

        // Проверяем вызов для усыновителя с существующим chatId и некорректным статусом
        when(catAdopterRepository.findByChatIdAndAdopterStatusIn(
                CAT_ADOPTER_PRESENT_CHAT_ID, oneStatusProbationPassed)).thenReturn(null);
        thrown = Assertions.assertThrows(
                AdopterNotFoundException.class, () -> adopterService.findCatAdopterByChatIdAndStatuses(
                        CAT_ADOPTER_PRESENT_CHAT_ID, oneStatusProbationPassed));
        assertEquals(thrown.getMessage(),
                String.format(CAT_ADOPTER_BY_CHAT_ID_AND_STATUS_NOT_FOUND, CAT_ADOPTER_PRESENT_CHAT_ID, oneStatusProbationPassed));
        Mockito.verify(catAdopterRepository, times(1)).
                findByChatIdAndAdopterStatusIn(CAT_ADOPTER_PRESENT_CHAT_ID, oneStatusProbationPassed);
    }

    @Test
    public void getAdopterIdByChatId_forDogShelter() {
        // Проверяем вызов метода для усыновителя приюта для собак с существующим в базе chatId
        when(dogAdopterRepository.findAdopterIdByChatId(DOG_ADOPTER_PRESENT_CHAT_ID)).thenReturn(dogAdopter);
        Integer adopterId = adopterService.getAdopterIdByChatId(DOG_ADOPTER_PRESENT_CHAT_ID, ShelterType.DOG_SHELTER);
        assertEquals(adopterId, dogAdopter.getAdopterId());
        Mockito.verify(dogAdopterRepository, times(1))
                .findAdopterIdByChatId(DOG_ADOPTER_PRESENT_CHAT_ID);
        Mockito.verifyNoInteractions(catAdopterRepository);

        // Проверяем вызов метода для усыновителя приюта для собак с несуществующим в базе chatId
        when(dogAdopterRepository.findAdopterIdByChatId(DOG_ADOPTER_NOT_PRESENT_CHAT_ID)).thenReturn(null);
        AdopterNotFoundException thrown = Assertions.assertThrows(
                AdopterNotFoundException.class, () -> adopterService.getAdopterIdByChatId(
                        DOG_ADOPTER_NOT_PRESENT_CHAT_ID, ShelterType.DOG_SHELTER));
        Mockito.verify(dogAdopterRepository, times(1))
                .findAdopterIdByChatId(DOG_ADOPTER_NOT_PRESENT_CHAT_ID);
        assertEquals(thrown.getMessage(),
                String.format(DOG_ADOPTER_BY_CHAT_ID_NOT_FOUND, DOG_ADOPTER_NOT_PRESENT_CHAT_ID));
        Mockito.verifyNoInteractions(catAdopterRepository);
    }

    @Test
    public void getAdopterIdByChatId_forCatShelter() {
        // Проверяем вызов метода для усыновителя приюта для кошек с существующим в базе chatId
        when(catAdopterRepository.findAdopterIdByChatId(CAT_ADOPTER_PRESENT_CHAT_ID)).thenReturn(catAdopter);
        Integer adopterId = adopterService.getAdopterIdByChatId(CAT_ADOPTER_PRESENT_CHAT_ID, ShelterType.CAT_SHELTER);
        assertEquals(adopterId, catAdopter.getAdopterId());
        Mockito.verify(catAdopterRepository, times(1))
                .findAdopterIdByChatId(CAT_ADOPTER_PRESENT_CHAT_ID);
        Mockito.verifyNoInteractions(dogAdopterRepository);

        // Проверяем вызов метода для усыновителя приюта для кошек с несуществующим в базе chatId
        when(catAdopterRepository.findAdopterIdByChatId(CAT_ADOPTER_NOT_PRESENT_CHAT_ID)).thenReturn(null);
        AdopterNotFoundException thrown = Assertions.assertThrows(
                AdopterNotFoundException.class, () -> adopterService.getAdopterIdByChatId(
                        CAT_ADOPTER_NOT_PRESENT_CHAT_ID, ShelterType.CAT_SHELTER));
        Mockito.verify(catAdopterRepository, times(1))
                .findAdopterIdByChatId(CAT_ADOPTER_NOT_PRESENT_CHAT_ID);
        assertEquals(thrown.getMessage(),
                String.format(CAT_ADOPTER_BY_CHAT_ID_NOT_FOUND, CAT_ADOPTER_NOT_PRESENT_CHAT_ID));
        Mockito.verifyNoInteractions(dogAdopterRepository);
    }

    @Test
    void processProbationStatusForDogAdopter_Active() {
        ProbationStatus probationStatus = ProbationStatus.PROBATION_ACTIVE;
        LocalDateTime endProbationDate0 = dogAdopter.getEndProbationDate();
        when(dogAdopterRepository.findById(DOG_ADOPTER_PRESENT_ID)).thenReturn(Optional.of(dogAdopter));
        when(dogAdopterRepository.save(dogAdopter)).thenReturn(dogAdopter);

        AnimalAdopterDTO result = adopterService.processProbationStatusForDogAdopter(DOG_ADOPTER_PRESENT_ID, probationStatus);

        Mockito.verify(dogAdopterRepository, times(1))
                .findById(DOG_ADOPTER_PRESENT_ID);
        Mockito.verify(dogAdopterRepository, times(1))
                .save(dogAdopter);
        assertEquals(result.getAdopterStatus(), AdopterStatus.PROBATION_ACTIVE);
        assertEquals(dogAdopter.getEndProbationDate(), endProbationDate0);
        Mockito.verify(notificationTaskService, times(1)).
                probationActiveNotification(dogAdopter, ShelterType.DOG_SHELTER);
    }

    @Test
    void processProbationStatusForDogAdopter_Success() {
        ProbationStatus probationStatus = ProbationStatus.PROBATION_SUCCESS;
        LocalDateTime endProbationDate0 = dogAdopter.getEndProbationDate();
        when(dogAdopterRepository.findById(DOG_ADOPTER_PRESENT_ID)).thenReturn(Optional.of(dogAdopter));
        when(dogAdopterRepository.save(dogAdopter)).thenReturn(dogAdopter);

        AnimalAdopterDTO result = adopterService.processProbationStatusForDogAdopter(DOG_ADOPTER_PRESENT_ID, probationStatus);

        Mockito.verify(dogAdopterRepository, times(1))
                .findById(DOG_ADOPTER_PRESENT_ID);
        Mockito.verify(dogAdopterRepository, times(1))
                .save(dogAdopter);
        assertEquals(result.getAdopterStatus(), AdopterStatus.PROBATION_PASSED);
        assertEquals(dogAdopter.getEndProbationDate(), endProbationDate0);
        Mockito.verify(notificationTaskService, times(1)).
                probationSuccessNotification(dogAdopter, ShelterType.DOG_SHELTER);
    }

    @Test
    void processProbationStatusForDogAdopter_Add14() {
        ProbationStatus probationStatus = ProbationStatus.PROBATION_ADD_14;
        LocalDateTime endProbationDate0 = dogAdopter.getEndProbationDate();
        when(dogAdopterRepository.findById(DOG_ADOPTER_PRESENT_ID)).thenReturn(Optional.of(dogAdopter));
        when(dogAdopterRepository.save(dogAdopter)).thenReturn(dogAdopter);

        AnimalAdopterDTO result = adopterService.processProbationStatusForDogAdopter(DOG_ADOPTER_PRESENT_ID, probationStatus);

        Mockito.verify(dogAdopterRepository, times(1))
                .findById(DOG_ADOPTER_PRESENT_ID);
        Mockito.verify(dogAdopterRepository, times(1))
                .save(dogAdopter);
        assertEquals(result.getAdopterStatus(), AdopterStatus.PROBATION_ACTIVE);
        assertEquals(dogAdopter.getEndProbationDate(), endProbationDate0.plusDays(14));
        Mockito.verify(notificationTaskService, times(1)).
                probationAdd14DaysNotification(dogAdopter, ShelterType.DOG_SHELTER);
    }

    @Test
    void processProbationStatusForDogAdopter_Add30() {
        ProbationStatus probationStatus = ProbationStatus.PROBATION_ADD_30;
        LocalDateTime endProbationDate0 = dogAdopter.getEndProbationDate();
        when(dogAdopterRepository.findById(DOG_ADOPTER_PRESENT_ID)).thenReturn(Optional.of(dogAdopter));
        when(dogAdopterRepository.save(dogAdopter)).thenReturn(dogAdopter);

        AnimalAdopterDTO result = adopterService.processProbationStatusForDogAdopter(DOG_ADOPTER_PRESENT_ID, probationStatus);

        Mockito.verify(dogAdopterRepository, times(1))
                .findById(DOG_ADOPTER_PRESENT_ID);
        Mockito.verify(dogAdopterRepository, times(1))
                .save(dogAdopter);
        assertEquals(result.getAdopterStatus(), AdopterStatus.PROBATION_ACTIVE);
        assertEquals(dogAdopter.getEndProbationDate(), endProbationDate0.plusDays(30));
        Mockito.verify(notificationTaskService, times(1)).
                probationAdd30DaysNotification(dogAdopter, ShelterType.DOG_SHELTER);
    }

    @Test
    void processProbationStatusForDogAdopter_Reject() {
        ProbationStatus probationStatus = ProbationStatus.PROBATION_REJECT;
        LocalDateTime endProbationDate0 = dogAdopter.getEndProbationDate();
        when(dogAdopterRepository.findById(DOG_ADOPTER_PRESENT_ID)).thenReturn(Optional.of(dogAdopter));
        when(dogAdopterRepository.save(dogAdopter)).thenReturn(dogAdopter);

        AnimalAdopterDTO result = adopterService.processProbationStatusForDogAdopter(DOG_ADOPTER_PRESENT_ID, probationStatus);

        Mockito.verify(dogAdopterRepository, times(1))
                .findById(DOG_ADOPTER_PRESENT_ID);
        Mockito.verify(dogAdopterRepository, times(1))
                .save(dogAdopter);
        assertEquals(result.getAdopterStatus(), AdopterStatus.PROBATION_REJECT);
        assertEquals(dogAdopter.getEndProbationDate(), endProbationDate0);
        Mockito.verify(notificationTaskService, times(1)).
                probationRejectNotification(dogAdopter, ShelterType.DOG_SHELTER);
    }

    @Test
    void processProbationStatusForCatAdopter() {
        ProbationStatus probationStatus = ProbationStatus.PROBATION_ADD_14;
        LocalDateTime endProbationDate0 = catAdopter.getEndProbationDate();
        when(catAdopterRepository.findById(CAT_ADOPTER_PRESENT_ID)).thenReturn(Optional.of(catAdopter));
        when(catAdopterRepository.save(catAdopter)).thenReturn(catAdopter);

        AnimalAdopterDTO result = adopterService.processProbationStatusForCatAdopter(CAT_ADOPTER_PRESENT_ID, probationStatus);

        Mockito.verify(catAdopterRepository, times(1))
                .findById(CAT_ADOPTER_PRESENT_ID);
        Mockito.verify(catAdopterRepository, times(1))
                .save(catAdopter);
        assertEquals(result.getAdopterStatus(), AdopterStatus.PROBATION_ACTIVE);
        assertEquals(catAdopter.getEndProbationDate(), endProbationDate0.plusDays(14));
        Mockito.verify(notificationTaskService, times(1)).
                probationAdd14DaysNotification(catAdopter, ShelterType.CAT_SHELTER);
    }

    @Test
    void checkSkipReportAndCreateWarning_DogAdopters() {
        // Проверяем обработку несвоевременно отправленных отчетов усыновителей приюта для собак и их уведомление
        dogAdopter.setAdopterStatus(AdopterStatus.PROBATION_ACTIVE);
        when(dogAdopterRepository.findById(DOG_ADOPTER_PRESENT_ID)).thenReturn(Optional.of(dogAdopter));
        when(dogAdopterRepository.getAdoptersChatIdWhoNotSendReport(
                "PROBATION_ACTIVE", "WAITING_REPORT", SKIPPED_DAYS_WARNING)).
                thenReturn(Collections.singletonList(dogAdopter.getAdopterId()));
        adopterService.checkSkipReportAndCreateWarning(ShelterType.DOG_SHELTER, SKIPPED_DAYS_WARNING, TypeWarning.WARNING_ADOPTER);
        assertEquals(dogAdopter.getAdopterStatus(), AdopterStatus.WAITING_REPORT);
        Mockito.verify(dogAdopterRepository, times(1)).save(dogAdopter);
        Mockito.verify(notificationTaskService, times(1)).
                reportSkippedWarningAdopterNotification(dogAdopter, ShelterType.DOG_SHELTER);
    }

    @Test
    void checkSkipReportAndCreateWarning_CatAdopters() {
        // Проверяем обработку отсутствия отправленных отчетов усыновителей приюта для кошек и их уведомление
        catAdopter.setAdopterStatus(AdopterStatus.PROBATION_ACTIVE);
        when(catAdopterRepository.findById(CAT_ADOPTER_PRESENT_ID)).thenReturn(Optional.of(catAdopter));
        when(catAdopterRepository.getAdoptersChatIdWhoNotSendReport(
                "PROBATION_ACTIVE", "WAITING_REPORT", SKIPPED_DAYS_WARNING)).
                thenReturn(Collections.singletonList(catAdopter.getAdopterId()));
        adopterService.checkSkipReportAndCreateWarning(
                ShelterType.CAT_SHELTER, SKIPPED_DAYS_WARNING, TypeWarning.WARNING_ADOPTER);
        assertEquals(catAdopter.getAdopterStatus(), AdopterStatus.WAITING_REPORT);
        Mockito.verify(catAdopterRepository, times(1)).save(catAdopter);
        Mockito.verify(notificationTaskService, times(1)).
                reportSkippedWarningAdopterNotification(catAdopter, ShelterType.CAT_SHELTER);
    }

    @Test
    void checkSkipReportAndCreateWarning_DogVolunteers() {
        // Проверяем обработку отсутствия отчетов усыновителей приюта для собак и уведомление волонтеров
        when(dogAdopterRepository.findById(DOG_ADOPTER_PRESENT_ID)).thenReturn(Optional.of(dogAdopter));
        when(dogAdopterRepository.getAdoptersChatIdWhoNotSendReport(
                "PROBATION_ACTIVE", "WAITING_REPORT", SKIPPED_DAYS_SIGNAL_VOLUNTEER)).
                thenReturn(Collections.singletonList(dogAdopter.getAdopterId()));
        when(volunteerService.findAvailableVolunteerChatId(ShelterType.DOG_SHELTER)).
                thenReturn(volunteers.stream().map(Volunteer::getChatId).collect(Collectors.toList()));

        adopterService.checkSkipReportAndCreateWarning(
                ShelterType.DOG_SHELTER, SKIPPED_DAYS_SIGNAL_VOLUNTEER, TypeWarning.WARNING_VOLUNTEER);
        Mockito.verify(notificationTaskService, times(volunteers.size())).
                reportSkippedWarningVolunteerNotification(
                        valueCaptor.capture(), eq(dogAdopter), eq(SKIPPED_DAYS_SIGNAL_VOLUNTEER), eq(ShelterType.DOG_SHELTER));
        List<Long> volunteersChatId = valueCaptor.getAllValues();
        assertEquals(volunteersChatId.size(), 2);
        assertTrue(volunteersChatId.contains(volunteers.get(0).getChatId()));
        assertTrue(volunteersChatId.contains(volunteers.get(1).getChatId()));
    }

    @Test
    void checkSkipReportAndCreateWarning_CatVolunteers() {
        // Проверяем обработку отсутствия отчетов усыновителей приюта для кошек и уведомление волонтеров
        when(catAdopterRepository.findById(CAT_ADOPTER_PRESENT_ID)).thenReturn(Optional.of(catAdopter));
        when(catAdopterRepository.getAdoptersChatIdWhoNotSendReport(
                "PROBATION_ACTIVE", "WAITING_REPORT", SKIPPED_DAYS_SIGNAL_VOLUNTEER)).
                thenReturn(Collections.singletonList(catAdopter.getAdopterId()));
        when(volunteerService.findAvailableVolunteerChatId(ShelterType.CAT_SHELTER)).
                thenReturn(volunteers.stream().map(Volunteer::getChatId).collect(Collectors.toList()));

        adopterService.checkSkipReportAndCreateWarning(
                ShelterType.CAT_SHELTER, SKIPPED_DAYS_SIGNAL_VOLUNTEER, TypeWarning.WARNING_VOLUNTEER);
        Mockito.verify(notificationTaskService, times(volunteers.size())).
                reportSkippedWarningVolunteerNotification(
                        valueCaptor.capture(), eq(catAdopter), eq(SKIPPED_DAYS_SIGNAL_VOLUNTEER), eq(ShelterType.CAT_SHELTER));
        List<Long> volunteersChatId = valueCaptor.getAllValues();
        assertEquals(volunteersChatId.size(), 2);
        assertTrue(volunteersChatId.contains(volunteers.get(0).getChatId()));
        assertTrue(volunteersChatId.contains(volunteers.get(1).getChatId()));
    }
}