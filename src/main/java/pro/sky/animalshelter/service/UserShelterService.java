package pro.sky.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.UserCatShelterRepository;
import pro.sky.animalshelter.repository.UserDogShelterRepository;

@Service
public class UserShelterService {

    private final UserDogShelterRepository userDogShelterRepository;
    private final UserCatShelterRepository userCatShelterRepository;
    private final ShelterService shelterService;

    @Autowired
    public UserShelterService(UserDogShelterRepository userDogShelterRepository, UserCatShelterRepository userCatShelterRepository, ShelterService shelterService) {
        this.userDogShelterRepository = userDogShelterRepository;
        this.userCatShelterRepository = userCatShelterRepository;
        this.shelterService = shelterService;
    }

    public void saveUserContacts(ShelterType shelterType, Long telegramId, String firstName, String lastName, String userContacts) {
        Shelter shelter = shelterService.findShelterByShelterType(shelterType);
        if (shelterType == ShelterType.DOG_SHELTER) {
            UserDogShelter userDogShelter = new UserDogShelter(telegramId, firstName, lastName, userContacts, shelter);
            userDogShelterRepository.save(userDogShelter);
        } else if (shelterType == ShelterType.CAT_SHELTER) {
            UserCatShelter userCatShelter = new UserCatShelter(telegramId, firstName, lastName, userContacts, shelter);
            userCatShelterRepository.save(userCatShelter);
        }
    }
}
