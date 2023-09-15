package pro.sky.animalshelter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import pro.sky.animalshelter.dto.ReportAnimalDTO;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.ReportCatShelterRepository;
import pro.sky.animalshelter.repository.ReportDogShelterRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {
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

    public static final Integer DOG_SHELTER_REPORT_ID = 21;
    public static final Integer CAT_SHELTER_REPORT_ID = 84;

   /* public static final Long DOG_ADOPTER_PRESENT_CHAT_ID = 812818905L;
    public static final Long DOG_ADOPTER_NOT_PRESENT_CHAT_ID = 123456789L;
    public static final Long CAT_ADOPTER_PRESENT_CHAT_ID = 812818900L;
    public static final Long CAT_ADOPTER_NOT_PRESENT_CHAT_ID = 123456780L;*/

    @Mock
    private ReportDogShelterRepository reportDogShelterRepository;

    @Mock
    private ReportCatShelterRepository reportCatShelterRepository;

    @Mock
    private AdopterService adopterService;

    @Mock
    private NotificationTaskService notificationTaskService;

    @InjectMocks
    public ReportService reportService;

    private CatAdopter catAdopter;
    private DogAdopter dogAdopter;
    private ReportDogShelter reportDogShelter;
    private ReportCatShelter reportCatShelter;

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

        reportDogShelter = new ReportDogShelter();
        reportDogShelter.setReportId(DOG_SHELTER_REPORT_ID);
        reportDogShelter.setDogAdopter(dogAdopter);
        reportDogShelter.setReportStatus(ReportStatus.REPORT_NEW);
        reportDogShelter.setDateReport(LocalDate.now());
        reportDogShelter.setPhotoFilePath("C:");
        reportDogShelter.setDescription("Report Dog Description");
        reportDogShelter.setPhotoMediaType(MediaType.IMAGE_JPEG_VALUE);
        reportDogShelter.setPhotoFileSize(32768L);

        reportCatShelter = new ReportCatShelter();
        reportCatShelter.setReportId(CAT_SHELTER_REPORT_ID);
        reportCatShelter.setCatAdopter(catAdopter);
        reportCatShelter.setReportStatus(ReportStatus.REPORT_NEW);
        reportCatShelter.setDateReport(LocalDate.now());
        reportCatShelter.setPhotoFilePath("C:");
        reportCatShelter.setDescription("Report Cat Description");
        reportCatShelter.setPhotoMediaType(MediaType.IMAGE_JPEG_VALUE);
        reportCatShelter.setPhotoFileSize(43070L);
    }

    @Test
    public void getReportsDogSheltersBy_NullAdopterAndEmptyStatus() {
        reportService.getReportsDogShelterByAdopterAndStatus(null, Collections.emptyList());
        Mockito.verify(reportDogShelterRepository, times(1)).findAll();
        Mockito.verifyNoMoreInteractions(reportDogShelterRepository, reportCatShelterRepository);
    }

    @Test
    public void getReportsDogSheltersBy_NotNullAdopterAndEmptyStatus() {
        when(adopterService.findDogAdopterById(DOG_ADOPTER_PRESENT_ID)).thenReturn(dogAdopter);
        reportService.getReportsDogShelterByAdopterAndStatus(DOG_ADOPTER_PRESENT_ID, Collections.emptyList());

        Mockito.verify(adopterService, times(1)).findDogAdopterById(DOG_ADOPTER_PRESENT_ID);
        Mockito.verify(reportDogShelterRepository, times(1)).findByDogAdopter(dogAdopter);
        Mockito.verifyNoMoreInteractions(reportDogShelterRepository, reportCatShelterRepository, adopterService);
    }

    @Test
    public void getReportsDogSheltersBy_NotNullAdopterAndFillStatus() {
        when(adopterService.findDogAdopterById(DOG_ADOPTER_PRESENT_ID)).thenReturn(dogAdopter);
        Collection<ReportStatus> statuses = List.of(ReportStatus.REPORT_NEW, ReportStatus.REPORT_WARNING);
        reportService.getReportsDogShelterByAdopterAndStatus(DOG_ADOPTER_PRESENT_ID, statuses);

        Mockito.verify(adopterService, times(1)).findDogAdopterById(DOG_ADOPTER_PRESENT_ID);
        Mockito.verify(reportDogShelterRepository, times(1)).
                findByDogAdopterAndReportStatusIn(dogAdopter, statuses);
        Mockito.verifyNoMoreInteractions(reportDogShelterRepository, reportCatShelterRepository, adopterService);
    }

    @Test
    public void getReportsDogSheltersBy_NullAdopterAndFillStatus() {
        Collection<ReportStatus> statuses = List.of(ReportStatus.REPORT_NEW, ReportStatus.REPORT_WARNING);
        reportService.getReportsDogShelterByAdopterAndStatus(null, statuses);

        Mockito.verify(reportDogShelterRepository, times(1)).findByReportStatusIn(statuses);
        Mockito.verifyNoMoreInteractions(reportDogShelterRepository, reportCatShelterRepository, adopterService);
    }

    @Test
    public void getReportsCatShelterBy_NullAdopterAndEmptyStatus() {
        reportService.getReportsCatShelterByAdopterAndStatus(null, Collections.emptyList());
        Mockito.verify(reportCatShelterRepository, times(1)).findAll();
        Mockito.verifyNoMoreInteractions(reportDogShelterRepository, reportCatShelterRepository, adopterService);
    }

    @Test
    public void getReportsCatShelterBy_NotNullAdopterAndEmptyStatus() {
        when(adopterService.findCatAdopterById(CAT_ADOPTER_PRESENT_ID)).thenReturn(catAdopter);
        reportService.getReportsCatShelterByAdopterAndStatus(CAT_ADOPTER_PRESENT_ID, Collections.emptyList());

        Mockito.verify(adopterService, times(1)).findCatAdopterById(CAT_ADOPTER_PRESENT_ID);
        Mockito.verify(reportCatShelterRepository, times(1)).findByCatAdopter(catAdopter);
        Mockito.verifyNoMoreInteractions(reportDogShelterRepository, reportCatShelterRepository, adopterService);
    }

    @Test
    public void getReportsCatShelterBy_NotNullAdopterAndFillStatus() {
        when(adopterService.findCatAdopterById(CAT_ADOPTER_PRESENT_ID)).thenReturn(catAdopter);
        Collection<ReportStatus> statuses = List.of(ReportStatus.REPORT_NEW, ReportStatus.REPORT_WARNING);
        reportService.getReportsCatShelterByAdopterAndStatus(CAT_ADOPTER_PRESENT_ID, statuses);

        Mockito.verify(adopterService, times(1)).findCatAdopterById(CAT_ADOPTER_PRESENT_ID);
        Mockito.verify(reportCatShelterRepository, times(1)).
                findByCatAdopterAndReportStatusIn(catAdopter, statuses);
        Mockito.verifyNoMoreInteractions(reportDogShelterRepository, reportCatShelterRepository, adopterService);
    }

    @Test
    public void getReportsCatShelterBy_NullAdopterAndFillStatus() {
        Collection<ReportStatus> statuses = List.of(ReportStatus.REPORT_NEW, ReportStatus.REPORT_WARNING);
        reportService.getReportsCatShelterByAdopterAndStatus(null, statuses);

        Mockito.verify(reportCatShelterRepository, times(1)).findByReportStatusIn(statuses);
        Mockito.verifyNoMoreInteractions(reportDogShelterRepository, reportCatShelterRepository, adopterService);
    }

    @Test
    public void editDogShelterStatusReport_Accepted() {
        when(reportDogShelterRepository.findById(DOG_SHELTER_REPORT_ID)).thenReturn(Optional.of(reportDogShelter));
        when(reportDogShelterRepository.save(reportDogShelter)).thenReturn(reportDogShelter);
        reportService.editDogShelterStatusReport(DOG_SHELTER_REPORT_ID, ReportStatus.REPORT_ACCEPTED);

        Mockito.verify(reportDogShelterRepository, times(1)).findById(DOG_SHELTER_REPORT_ID);
        Mockito.verify(reportDogShelterRepository, times(1)).save(reportDogShelter);
        assertEquals(reportDogShelter.getReportStatus(), ReportStatus.REPORT_ACCEPTED);
        Mockito.verifyNoMoreInteractions(reportDogShelterRepository, reportCatShelterRepository,
                adopterService, notificationTaskService);
    }

    @Test
    public void editDogShelterStatusReport_Warning() throws CloneNotSupportedException {
        when(reportDogShelterRepository.findById(DOG_SHELTER_REPORT_ID)).thenReturn(Optional.of(reportDogShelter));
        when(reportDogShelterRepository.save(reportDogShelter)).thenReturn(reportDogShelter);
        reportService.editDogShelterStatusReport(DOG_SHELTER_REPORT_ID, ReportStatus.REPORT_WARNING);

        Mockito.verify(reportDogShelterRepository, times(1)).findById(DOG_SHELTER_REPORT_ID);
        Mockito.verify(reportDogShelterRepository, times(1)).save(reportDogShelter);
        Mockito.verify(notificationTaskService, times(1)).reportWarningNotification(
                reportDogShelter.getAdopter(), ShelterType.DOG_SHELTER);
        assertEquals(reportDogShelter.getReportStatus(), ReportStatus.REPORT_WARNING);
        Mockito.verifyNoMoreInteractions(reportDogShelterRepository, reportCatShelterRepository,
                adopterService, notificationTaskService);
    }

    @Test
    public void editCatShelterStatusReport_Warning() {
        when(reportCatShelterRepository.findById(CAT_SHELTER_REPORT_ID)).thenReturn(Optional.of(reportCatShelter));
        when(reportCatShelterRepository.save(reportCatShelter)).thenReturn(reportCatShelter);
        reportService.editCatShelterStatusReport(CAT_SHELTER_REPORT_ID, ReportStatus.REPORT_WARNING);

        Mockito.verify(reportCatShelterRepository, times(1)).findById(CAT_SHELTER_REPORT_ID);
        Mockito.verify(reportCatShelterRepository, times(1)).save(reportCatShelter);
        assertEquals(reportCatShelter.getReportStatus(), ReportStatus.REPORT_ACCEPTED);
        Mockito.verifyNoMoreInteractions(reportDogShelterRepository, reportCatShelterRepository,
                adopterService, notificationTaskService);
    }

    @Test
    public void editCatShelterStatusReport_Accepted() {
        when(reportCatShelterRepository.findById(CAT_SHELTER_REPORT_ID)).thenReturn(Optional.of(reportCatShelter));
        when(reportCatShelterRepository.save(reportCatShelter)).thenReturn(reportCatShelter);
        reportService.editCatShelterStatusReport(CAT_SHELTER_REPORT_ID, ReportStatus.REPORT_WARNING);

        Mockito.verify(reportCatShelterRepository, times(1)).findById(CAT_SHELTER_REPORT_ID);
        Mockito.verify(reportCatShelterRepository, times(1)).save(reportCatShelter);
        Mockito.verify(notificationTaskService, times(1)).reportWarningNotification(
                reportCatShelter.getAdopter(), ShelterType.CAT_SHELTER);
        assertEquals(reportCatShelter.getReportStatus(), ReportStatus.REPORT_WARNING);
        Mockito.verifyNoMoreInteractions(reportDogShelterRepository, reportCatShelterRepository,
                adopterService, notificationTaskService);
    }

    @Test
    public void saveReport_DogShelter() throws CloneNotSupportedException {
        dogAdopter.setAdopterStatus(AdopterStatus.WAITING_REPORT);
        ReportDogShelter reportDogShelterBeforeSaved = (ReportDogShelter) reportDogShelter.clone();
        reportDogShelterBeforeSaved.setReportId(null);
        ReportAnimalDTO reportAnimalDTO = reportDogShelterBeforeSaved.toDTO();
        when(adopterService.findDogAdopterByChatIdAndStatuses(
                reportAnimalDTO.getChatId(),
                List.of(AdopterStatus.PROBATION_ACTIVE, AdopterStatus.WAITING_REPORT))
        ).thenReturn(dogAdopter);
        when(reportDogShelterRepository.save(reportDogShelterBeforeSaved)).thenReturn(reportDogShelter);

        reportService.saveReport(reportAnimalDTO);
        Mockito.verify(adopterService, times(1)).
                findDogAdopterByChatIdAndStatuses(reportAnimalDTO.getChatId(), List.of(AdopterStatus.PROBATION_ACTIVE, AdopterStatus.WAITING_REPORT));
        Mockito.verify(reportDogShelterRepository, times(1)).save(reportDogShelterBeforeSaved);
        Mockito.verifyNoMoreInteractions(reportDogShelterRepository, reportCatShelterRepository,
                adopterService, notificationTaskService);
        assertEquals(dogAdopter.getAdopterStatus(), AdopterStatus.PROBATION_ACTIVE);
    }

    @Test
    public void saveReport_CatShelter() throws CloneNotSupportedException {
        catAdopter.setAdopterStatus(AdopterStatus.WAITING_REPORT);
        ReportCatShelter reportCatShelterBeforeSaved = (ReportCatShelter) reportCatShelter.clone();
        reportCatShelterBeforeSaved.setReportId(null);
        ReportAnimalDTO reportAnimalDTO = reportCatShelterBeforeSaved.toDTO();
        when(adopterService.findCatAdopterByChatIdAndStatuses(
                reportAnimalDTO.getChatId(),
                List.of(AdopterStatus.PROBATION_ACTIVE, AdopterStatus.WAITING_REPORT))
        ).thenReturn(catAdopter);
        when(reportCatShelterRepository.save(reportCatShelterBeforeSaved)).thenReturn(reportCatShelter);

        reportService.saveReport(reportAnimalDTO);
        Mockito.verify(adopterService, times(1)).
                findCatAdopterByChatIdAndStatuses(reportAnimalDTO.getChatId(), List.of(AdopterStatus.PROBATION_ACTIVE, AdopterStatus.WAITING_REPORT));
        Mockito.verify(reportCatShelterRepository, times(1)).save(reportCatShelterBeforeSaved);
        Mockito.verifyNoMoreInteractions(reportDogShelterRepository, reportCatShelterRepository,
                adopterService, notificationTaskService);
        assertEquals(dogAdopter.getAdopterStatus(), AdopterStatus.PROBATION_ACTIVE);
    }
}