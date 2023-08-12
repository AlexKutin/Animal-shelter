package pro.sky.animalshelter.service;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import pro.sky.animalshelter.model.UserCatShelter;
import pro.sky.animalshelter.repository.UserCatShelterRepository;

@Service
public class UserCatShelterService {

    private final UserCatShelterRepository userCatShelterRepository;

    @Autowired
    public UserCatShelterService(UserCatShelterRepository userCatShelterRepository) {
        this.userCatShelterRepository = userCatShelterRepository;
    }

    public void saveUserContacts(Long telegramId, String userContacts) {
        UserCatShelter userCatShelter = new UserCatShelter();
        userCatShelter.setTelegramId(telegramId);
        userCatShelter.setUserContacts(userContacts);

        userCatShelterRepository.save(userCatShelter);
    }
}
