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

    public AdopterService(CatAdopterRepository catAdopterRepository, DogAdopterRepository dogAdopterRepository, UserShelterService userShelterService) {
        this.catAdopterRepository = catAdopterRepository;
        this.dogAdopterRepository = dogAdopterRepository;
        this.userShelterService = userShelterService;
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

    public AnimalAdopterDTO processProbationStatusForDogAdopter(Integer adopterId, PROBATION_STATUS probationStatus) {
        DogAdopter dogAdopter = findDogAdopterById(adopterId);
        processProbationStatusForAdopter(dogAdopter, probationStatus);
        dogAdopter = dogAdopterRepository.save(dogAdopter);
        return dogAdopter.toDTO();
    }

    public AnimalAdopterDTO processProbationStatusForCatAdopter(Integer adopterId, PROBATION_STATUS probationStatus) {
        CatAdopter catAdopter = findCatAdopterById(adopterId);
        processProbationStatusForAdopter(catAdopter, probationStatus);
        catAdopter = catAdopterRepository.save(catAdopter);
        return catAdopter.toDTO();
    }

    private static void processProbationStatusForAdopter(Adopter adopter, PROBATION_STATUS probationStatus) {
        switch (probationStatus) {
            case PROBATION_SUCCESS:
                adopter.setAdopterStatus(AdopterStatus.PROBATION_PASSED);
                break;
            case PROBATION_ADD_14:
                LocalDateTime endProbationDate = adopter.getEndProbationDate().plusDays(PROBATION_ADD_14_DAYS);
                adopter.setAdopterStatus(AdopterStatus.PROBATION_ACTIVE);
                adopter.setEndProbationDate(endProbationDate);
                break;
            case PROBATION_ADD_30:
                endProbationDate = adopter.getEndProbationDate().plusDays(PROBATION_ADD_30_DAYS);
                adopter.setAdopterStatus(AdopterStatus.PROBATION_ACTIVE);
                adopter.setEndProbationDate(endProbationDate);
                break;
            case PROBATION_REJECT:
                adopter.setAdopterStatus(AdopterStatus.PROBATION_REJECT);
                break;
        }
    }

   /* public Integer getAdopterIdByUserId(Integer userId) {
        CatAdopter catAdopter = catAdopterRepository.findByUserUserId(userId);
        DogAdopter dogAdopter = dogAdopterRepository.findByUserUserId(userId);
        if (catAdopter != null) {
            return catAdopter.getAdopterId();
        } else if (dogAdopter != null) {
            return dogAdopter.getAdopterId();
        }
        return null;
    }*/
}
