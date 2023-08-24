package pro.sky.animalshelter.service;

import org.springframework.stereotype.Service;
import pro.sky.animalshelter.exception.AdopterNotFoundException;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.CatAdopterRepository;
import pro.sky.animalshelter.repository.DogAdopterRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class AdopterService {
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
