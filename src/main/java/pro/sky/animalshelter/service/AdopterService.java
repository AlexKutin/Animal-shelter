package pro.sky.animalshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.animalshelter.dto.AnimalAdopterDTO;
import pro.sky.animalshelter.exception.AdopterNotFoundException;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.CatAdopterRepository;
import pro.sky.animalshelter.repository.DogAdopterRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class AdopterService {
    public static final int PROBATION_ADD_14_DAYS = 14;
    public static final int PROBATION_ADD_30_DAYS = 30;




    private final CatAdopterRepository catAdopterRepository;
    private final DogAdopterRepository dogAdopterRepository;
    private final UserShelterService userShelterService;
    private final NotificationTaskService notificationTaskService;

    public AdopterService(CatAdopterRepository catAdopterRepository, DogAdopterRepository dogAdopterRepository,
                          UserShelterService userShelterService, NotificationTaskService notificationTaskService) {
        this.catAdopterRepository = catAdopterRepository;
        this.dogAdopterRepository = dogAdopterRepository;
        this.userShelterService = userShelterService;
        this.notificationTaskService = notificationTaskService;
    }

    public CatAdopter findCatAdopterById(Integer adopterId) {
        return catAdopterRepository.findById(adopterId)
                .orElseThrow(() -> new AdopterNotFoundException(
                        String.format("Adopter with id = %d not found in Cat Shelter database", adopterId)));
    }

    public DogAdopter findDogAdopterById(Integer adopterId) {
        return dogAdopterRepository.findById(adopterId)
                .orElseThrow(() -> new AdopterNotFoundException(
                        String.format("Adopter with id = %d not found in Dog Shelter database", adopterId)));
    }

    public DogAdopter findDogAdopterByChatIdAndStatus(Long chatId, AdopterStatus adopterStatus) {
        UserDogShelter userDogShelter = userShelterService.findUserDogShelterByChatId(chatId);
        return Optional.ofNullable(dogAdopterRepository.findDogAdopterByUserAndAdopterStatus(userDogShelter, adopterStatus))
                .orElseThrow(() -> new AdopterNotFoundException(
                        String.format("Adopter with chatId = %d and status = %s not found in Dog Shelter database", chatId, adopterStatus)));
    }

    public CatAdopter findCatAdopterByChatIdAndStatuses(Long chatId, Collection<AdopterStatus> adopterStatuses) {
        UserCatShelter userCatShelter = userShelterService.findUserCatShelterByChatId(chatId);
        return Optional.ofNullable(catAdopterRepository.findCatAdopterByUserAndAdopterStatusIn(userCatShelter, adopterStatuses))
                .orElseThrow(() -> new AdopterNotFoundException(
                        String.format("Adopter with chatId = %d and status = %s not found in Cat Shelter database", chatId, adopterStatuses)));
    }

    public Integer getAdopterIdByChatId(Long chatId, ShelterType chooseShelterType) {
        if (chooseShelterType == ShelterType.DOG_SHELTER) {
            DogAdopter dogAdopter = dogAdopterRepository.findAdopterIdByChatId(chatId);
            if (dogAdopter != null) {
                return dogAdopter.getAdopterId();
            }
        } else if (chooseShelterType == ShelterType.CAT_SHELTER) {
            CatAdopter catAdopter = catAdopterRepository.findAdopterIdByChatId(chatId);
            if (catAdopter != null) {
                return catAdopter.getAdopterId();
            }
        }
        return 0;
    }

    public AnimalAdopterDTO processProbationStatusForDogAdopter(Integer adopterId, PROBATION_STATUS probationStatus) {
        DogAdopter dogAdopter = findDogAdopterById(adopterId);
        processProbationStatusForAdopter(dogAdopter, probationStatus, ShelterType.DOG_SHELTER);
        dogAdopter = dogAdopterRepository.save(dogAdopter);
        return dogAdopter.toDTO();
    }

    public AnimalAdopterDTO processProbationStatusForCatAdopter(Integer adopterId, PROBATION_STATUS probationStatus) {
        CatAdopter catAdopter = findCatAdopterById(adopterId);
        processProbationStatusForAdopter(catAdopter, probationStatus, ShelterType.CAT_SHELTER);
        catAdopter = catAdopterRepository.save(catAdopter);
        return catAdopter.toDTO();
    }

    private void processProbationStatusForAdopter(Adopter adopter, PROBATION_STATUS probationStatus, ShelterType shelterType) {
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