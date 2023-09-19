package pro.sky.animalshelter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.NotificationTaskRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static pro.sky.animalshelter.service.NotificationTaskService.*;
import static pro.sky.animalshelter.timer.checkSkippedReports.SKIPPED_DAYS_SIGNAL_VOLUNTEER;

@ExtendWith(MockitoExtension.class)
class NotificationTaskServiceTest {
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

    public static final Long DOG_ADOPTER_PRESENT_CHAT_ID = 812818905L;
    public static final Long CAT_ADOPTER_PRESENT_CHAT_ID = 812818900L;

    private static final Long DOG_VOLUNTEER_CHAT_ID = 123460089L;

    @Mock
    private NotificationTaskRepository notificationTaskRepository;

    @Captor
    ArgumentCaptor<NotificationTask> notificationCaptor;

    @InjectMocks
    private NotificationTaskService notificationTaskService;

    private CatAdopter catAdopter;
    private DogAdopter dogAdopter;

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
        catAdopter.setChatId(CAT_ADOPTER_PRESENT_CHAT_ID);
        catAdopter.setAdopterStatus(AdopterStatus.PROBATION_ACTIVE);
        catAdopter.setAdoptionDate(LocalDateTime.of(2023, 9, 1, 12, 0));
        catAdopter.setEndProbationDate(catAdopter.getAdoptionDate().plusMonths(1));
        catAdopter.setUser(userCatShelter);
        catAdopter.setCat(cat);

        dogAdopter = new DogAdopter();
        dogAdopter.setAdopterId(DOG_ADOPTER_PRESENT_ID);
        dogAdopter.setChatId(DOG_ADOPTER_PRESENT_CHAT_ID);
        dogAdopter.setAdopterStatus(AdopterStatus.PROBATION_ACTIVE);
        dogAdopter.setAdoptionDate(LocalDateTime.of(2023, 10, 17, 12, 0));
        dogAdopter.setEndProbationDate(catAdopter.getAdoptionDate().plusMonths(1));
        dogAdopter.setUser(userDogShelter);
        dogAdopter.setDog(dog);
    }

    @Test
    public void probationActiveNotification() {
        notificationTaskService.probationActiveNotification(dogAdopter, ShelterType.DOG_SHELTER);

        Mockito.verify(notificationTaskRepository, times(1)).save(notificationCaptor.capture());
        NotificationTask notificationTask = notificationCaptor.getValue();
        assertEquals(notificationTask.getChatId(), dogAdopter.getChatId());
        assertFalse(notificationTask.getProcessed());
        assertEquals(notificationTask.getShelterType(), ShelterType.DOG_SHELTER);
        assertEquals(notificationTask.getNotificationDateTime().toLocalDate(), LocalDateTime.now().toLocalDate());

        String textMessage = String.format(
                PROBATION_ACTIVE_MESSAGE, dogAdopter.getAnimal().getName(),
                dogAdopter.getEndProbationDate().format(notificationFormatter));
        assertEquals(notificationTask.getMessage(), textMessage);
    }

    @Test
    public void probationSuccessNotification() {
        notificationTaskService.probationSuccessNotification(catAdopter, ShelterType.CAT_SHELTER);

        Mockito.verify(notificationTaskRepository, times(1)).save(notificationCaptor.capture());
        NotificationTask notificationTask = notificationCaptor.getValue();
        assertEquals(notificationTask.getChatId(), catAdopter.getChatId());
        assertFalse(notificationTask.getProcessed());
        assertEquals(notificationTask.getShelterType(), ShelterType.CAT_SHELTER);
        assertEquals(notificationTask.getNotificationDateTime().toLocalDate(), LocalDateTime.now().toLocalDate());
        assertEquals(notificationTask.getMessage(), PROBATION_SUCCESS_MESSAGE);
    }

    @Test
    public void probationAdd14DaysNotification() {
        notificationTaskService.probationAdd14DaysNotification(catAdopter, ShelterType.CAT_SHELTER);

        Mockito.verify(notificationTaskRepository, times(1)).save(notificationCaptor.capture());
        NotificationTask notificationTask = notificationCaptor.getValue();
        assertEquals(notificationTask.getChatId(), catAdopter.getChatId());
        assertFalse(notificationTask.getProcessed());
        assertEquals(notificationTask.getShelterType(), ShelterType.CAT_SHELTER);
        assertEquals(notificationTask.getNotificationDateTime().toLocalDate(), LocalDateTime.now().toLocalDate());

        String textMessage = String.format(
                PROBATION_ADD_14_DAYS_MESSAGE, catAdopter.getEndProbationDate().format(notificationFormatter));
        assertEquals(notificationTask.getMessage(), textMessage);
    }

    @Test
    public void probationAdd30DaysNotification() {
        notificationTaskService.probationAdd30DaysNotification(dogAdopter, ShelterType.DOG_SHELTER);

        Mockito.verify(notificationTaskRepository, times(1)).save(notificationCaptor.capture());
        NotificationTask notificationTask = notificationCaptor.getValue();
        assertEquals(notificationTask.getChatId(), dogAdopter.getChatId());
        assertFalse(notificationTask.getProcessed());
        assertEquals(notificationTask.getShelterType(), ShelterType.DOG_SHELTER);
        assertEquals(notificationTask.getNotificationDateTime().toLocalDate(), LocalDateTime.now().toLocalDate());

        String textMessage = String.format(
                PROBATION_ADD_30_DAYS_MESSAGE, dogAdopter.getEndProbationDate().format(notificationFormatter));
        assertEquals(notificationTask.getMessage(), textMessage);
    }

    @Test
    public void probationRejectNotification() {
        notificationTaskService.probationRejectNotification(catAdopter, ShelterType.CAT_SHELTER);

        Mockito.verify(notificationTaskRepository, times(1)).save(notificationCaptor.capture());
        NotificationTask notificationTask = notificationCaptor.getValue();
        assertEquals(notificationTask.getChatId(), catAdopter.getChatId());
        assertFalse(notificationTask.getProcessed());
        assertEquals(notificationTask.getShelterType(), ShelterType.CAT_SHELTER);
        assertEquals(notificationTask.getNotificationDateTime().toLocalDate(), LocalDateTime.now().toLocalDate());
        assertEquals(notificationTask.getMessage(), PROBATION_REJECT_MESSAGE);
    }

    @Test
    public void reportWarningNotification() {
        notificationTaskService.reportWarningNotification(dogAdopter, ShelterType.DOG_SHELTER);

        Mockito.verify(notificationTaskRepository, times(1)).save(notificationCaptor.capture());
        NotificationTask notificationTask = notificationCaptor.getValue();
        assertEquals(notificationTask.getChatId(), dogAdopter.getChatId());
        assertFalse(notificationTask.getProcessed());
        assertEquals(notificationTask.getShelterType(), ShelterType.DOG_SHELTER);
        assertEquals(notificationTask.getNotificationDateTime().toLocalDate(), LocalDateTime.now().toLocalDate());
        assertEquals(notificationTask.getMessage(), REPORT_WARNING_MESSAGE);
    }

    @Test
    void reportSkippedWarningAdopterNotification() {
        notificationTaskService.reportSkippedWarningAdopterNotification(catAdopter, ShelterType.CAT_SHELTER);

        Mockito.verify(notificationTaskRepository, times(1)).save(notificationCaptor.capture());
        NotificationTask notificationTask = notificationCaptor.getValue();
        assertEquals(notificationTask.getChatId(), catAdopter.getChatId());
        assertFalse(notificationTask.getProcessed());
        assertEquals(notificationTask.getShelterType(), ShelterType.CAT_SHELTER);
        assertEquals(notificationTask.getNotificationDateTime().toLocalDate(), LocalDateTime.now().toLocalDate());
        assertEquals(notificationTask.getMessage(), REPORT_SKIPPED_WARNING_MESSAGE);
    }

    @Test
    void reportSkippedWarningVolunteerNotification() {
        notificationTaskService.reportSkippedWarningVolunteerNotification(DOG_VOLUNTEER_CHAT_ID, dogAdopter, SKIPPED_DAYS_SIGNAL_VOLUNTEER, ShelterType.DOG_SHELTER);

        Mockito.verify(notificationTaskRepository, times(1)).save(notificationCaptor.capture());
        NotificationTask notificationTask = notificationCaptor.getValue();
        assertEquals(notificationTask.getChatId(), DOG_VOLUNTEER_CHAT_ID);
        assertFalse(notificationTask.getProcessed());
        assertEquals(notificationTask.getShelterType(), ShelterType.DOG_SHELTER);
        assertEquals(notificationTask.getNotificationDateTime().toLocalDate(), LocalDateTime.now().toLocalDate());

        String textMessage = String.format(
                REPORT_SKIPPED_VOLUNTEER_MESSAGE, dogAdopter.getNotNullUserName(), dogAdopter.getAnimal().getName(), SKIPPED_DAYS_SIGNAL_VOLUNTEER);
        assertEquals(notificationTask.getMessage(), textMessage);
    }
}