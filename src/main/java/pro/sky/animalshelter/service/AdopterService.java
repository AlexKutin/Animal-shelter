package pro.sky.animalshelter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.animalshelter.dto.AnimalAdopterDTO;
import pro.sky.animalshelter.exception.AdopterNotFoundException;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.CatAdopterRepository;
import pro.sky.animalshelter.repository.DogAdopterRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static pro.sky.animalshelter.Constants.TextConstants.*;

@Service
@Transactional
public class AdopterService {
    private final Logger logger = LoggerFactory.getLogger(AdopterService.class);

    public static final int PROBATION_ADD_14_DAYS = 14;
    public static final int PROBATION_ADD_30_DAYS = 30;

    private final CatAdopterRepository catAdopterRepository;
    private final DogAdopterRepository dogAdopterRepository;
    private final NotificationTaskService notificationTaskService;
    private final VolunteerService volunteerService;

    public AdopterService(CatAdopterRepository catAdopterRepository, DogAdopterRepository dogAdopterRepository,
                          NotificationTaskService notificationTaskService, VolunteerService volunteerService) {
        this.catAdopterRepository = catAdopterRepository;
        this.dogAdopterRepository = dogAdopterRepository;
        this.notificationTaskService = notificationTaskService;
        this.volunteerService = volunteerService;
    }

    public CatAdopter findCatAdopterById(Integer adopterId) {
        return catAdopterRepository.findById(adopterId)
                .orElseThrow(() -> new AdopterNotFoundException(
                        String.format(CAT_ADOPTER_BY_ID_NOT_FOUND, adopterId)));
    }

    public DogAdopter findDogAdopterById(Integer adopterId) {
        return dogAdopterRepository.findById(adopterId)
                .orElseThrow(() -> new AdopterNotFoundException(
                        String.format(DOG_ADOPTER_BY_ID_NOT_FOUND, adopterId)));
    }

    public DogAdopter findDogAdopterByChatIdAndStatuses(Long chatId, Collection<AdopterStatus> adopterStatuses) {
        return Optional.ofNullable(dogAdopterRepository.findByChatIdAndAdopterStatusIn(chatId, adopterStatuses))
                .orElseThrow(() -> new AdopterNotFoundException(
                        String.format(DOG_ADOPTER_BY_CHAT_ID_AND_STATUS_NOT_FOUND, chatId, adopterStatuses)));
    }

    public CatAdopter findCatAdopterByChatIdAndStatuses(Long chatId, Collection<AdopterStatus> adopterStatuses) {
        return Optional.ofNullable(catAdopterRepository.findByChatIdAndAdopterStatusIn(chatId, adopterStatuses))
                .orElseThrow(() -> new AdopterNotFoundException(
                        String.format(CAT_ADOPTER_BY_CHAT_ID_AND_STATUS_NOT_FOUND, chatId, adopterStatuses)));
    }

    public Integer getAdopterIdByChatId(Long chatId, ShelterType chooseShelterType) {
        if (chooseShelterType == ShelterType.DOG_SHELTER) {
            DogAdopter dogAdopter = dogAdopterRepository.findByChatIdAndAdopterStatusIn(chatId,
                    List.of(AdopterStatus.PROBATION_ACTIVE, AdopterStatus.WAITING_REPORT));
            return Optional.ofNullable(dogAdopter)
                    .orElseThrow(() -> new AdopterNotFoundException(
                            String.format(DOG_ADOPTER_BY_CHAT_ID_NOT_FOUND, chatId)))
                    .getAdopterId();
        } else if (chooseShelterType == ShelterType.CAT_SHELTER) {
            CatAdopter catAdopter = catAdopterRepository.findByChatIdAndAdopterStatusIn(chatId,
                    List.of(AdopterStatus.PROBATION_ACTIVE, AdopterStatus.WAITING_REPORT));
            return Optional.ofNullable(catAdopter)
                    .orElseThrow(() -> new AdopterNotFoundException(
                            String.format(CAT_ADOPTER_BY_CHAT_ID_NOT_FOUND, chatId)))
                    .getAdopterId();
        }
        logger.error("Shelter type {} not supported. The checking reports not been performed", chooseShelterType);
        throw new ShelterNotFoundException(String.format("Shelter type: %s not supported yet", chooseShelterType));
    }

    public AnimalAdopterDTO processProbationStatusForDogAdopter(Integer adopterId, ProbationStatus probationStatus) {
        DogAdopter dogAdopter = findDogAdopterById(adopterId);
        processProbationStatusForAdopter(dogAdopter, probationStatus, ShelterType.DOG_SHELTER);
        dogAdopter = dogAdopterRepository.save(dogAdopter);
        return dogAdopter.toDTO();
    }

    public AnimalAdopterDTO processProbationStatusForCatAdopter(Integer adopterId, ProbationStatus probationStatus) {
        CatAdopter catAdopter = findCatAdopterById(adopterId);
        processProbationStatusForAdopter(catAdopter, probationStatus, ShelterType.CAT_SHELTER);
        catAdopter = catAdopterRepository.save(catAdopter);
        return catAdopter.toDTO();
    }

    public void checkSkipReportAndCreateWarning(ShelterType shelterType, int skippedDays, final TypeWarning typeWarning) {
        logger.info("Проверка своевременно отправленных отчетов усыновителей приюта: {}", shelterType);
        List<Integer> adopterIds;
        if (shelterType == ShelterType.DOG_SHELTER) {
            adopterIds = dogAdopterRepository.getAdoptersChatIdWhoNotSendReport(
                    "PROBATION_ACTIVE", "WAITING_REPORT", skippedDays);
            adopterIds.forEach((adopterId) -> {
                DogAdopter dogAdopter = findDogAdopterById(adopterId);
                if (typeWarning == TypeWarning.WARNING_ADOPTER) {
                    dogAdopter.setAdopterStatus(AdopterStatus.WAITING_REPORT);
                    dogAdopterRepository.save(dogAdopter);
                    notificationTaskService.reportSkippedWarningAdopterNotification(dogAdopter, shelterType);
                } else if (typeWarning == TypeWarning.WARNING_VOLUNTEER) {
                    createNotificationForVolunteers(dogAdopter, skippedDays, shelterType);
                }
            });
            logger.info("Обнаружно {} усыновителя несвоевременно представивиших отчеты о животных", adopterIds.size());
        } else if (shelterType == ShelterType.CAT_SHELTER) {
            adopterIds = catAdopterRepository.getAdoptersChatIdWhoNotSendReport(
                    "PROBATION_ACTIVE", "WAITING_REPORT", skippedDays);
            adopterIds.forEach((adopterId) -> {
                CatAdopter catAdopter = findCatAdopterById(adopterId);
                if (typeWarning == TypeWarning.WARNING_ADOPTER) {
                    notificationTaskService.reportSkippedWarningAdopterNotification(catAdopter, shelterType);
                    catAdopter.setAdopterStatus(AdopterStatus.WAITING_REPORT);
                    catAdopterRepository.save(catAdopter);
                } else if (typeWarning == TypeWarning.WARNING_VOLUNTEER) {
                    createNotificationForVolunteers(catAdopter, skippedDays, shelterType);
                }
            });
            logger.info("Обнаружно {} усыновителя несвоевременно представивиших отчеты о животных", adopterIds.size());
        } else {
            logger.error("Shelter type {} not supported. The checking reports not been performed", shelterType);
            throw new ShelterNotFoundException(String.format("Shelter type: %s not supported yet", shelterType));
        }
    }

    private void createNotificationForVolunteers(Adopter adopter, int skippedDays, ShelterType shelterType) {
        List<Long> volunteersChatIds = volunteerService.findAvailableVolunteerChatId(shelterType);
        volunteersChatIds.forEach(volunteerChatId -> notificationTaskService.reportSkippedWarningVolunteerNotification(
                volunteerChatId, adopter, skippedDays, shelterType));
    }

    private void processProbationStatusForAdopter(Adopter adopter, ProbationStatus probationStatus, ShelterType shelterType) {
        switch (probationStatus) {
            case PROBATION_ACTIVE:
                adopter.setAdopterStatus(AdopterStatus.PROBATION_ACTIVE);
                notificationTaskService.probationActiveNotification(adopter, shelterType);
                break;
            case PROBATION_SUCCESS:
                adopter.setAdopterStatus(AdopterStatus.PROBATION_PASSED);
                notificationTaskService.probationSuccessNotification(adopter, shelterType);
                break;
            case PROBATION_ADD_14:
                LocalDateTime endProbationDate = adopter.getEndProbationDate().plusDays(PROBATION_ADD_14_DAYS);
                adopter.setAdopterStatus(AdopterStatus.PROBATION_ACTIVE);
                adopter.setEndProbationDate(endProbationDate);
                notificationTaskService.probationAdd14DaysNotification(adopter, shelterType);
                break;
            case PROBATION_ADD_30:
                endProbationDate = adopter.getEndProbationDate().plusDays(PROBATION_ADD_30_DAYS);
                adopter.setAdopterStatus(AdopterStatus.PROBATION_ACTIVE);
                adopter.setEndProbationDate(endProbationDate);
                notificationTaskService.probationAdd30DaysNotification(adopter, shelterType);
                break;
            case PROBATION_REJECT:
                adopter.setAdopterStatus(AdopterStatus.PROBATION_REJECT);
                notificationTaskService.probationRejectNotification(adopter, shelterType);
                break;
        }
    }

}