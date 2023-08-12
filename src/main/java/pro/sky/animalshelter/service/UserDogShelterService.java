package pro.sky.animalshelter.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import pro.sky.animalshelter.model.UserDogShelter;
import pro.sky.animalshelter.repository.UserDogShelterRepository;

@Service
public class UserDogShelterService {

    private final UserDogShelterRepository userDogShelterRepository;

    @Autowired
    public UserDogShelterService(UserDogShelterRepository userDogShelterRepository) {
        this.userDogShelterRepository = userDogShelterRepository;
    }

    public void saveUserContacts(Long telegramId, String userContacts) {
        UserDogShelter userDogShelter = new UserDogShelter();
        userDogShelter.setTelegramId(telegramId);
        userDogShelter.setUserContacts(userContacts);
        userDogShelterRepository.save(userDogShelter);
    }
}
