package pro.sky.animalshelter.repository;

import org.springframework.stereotype.Repository;
import pro.sky.animalshelter.model.UserCatShelter;

@Repository
public interface UserCatShelterRepository extends UserShelterRepository<UserCatShelter> {

//    List<UserCatShelter> findAllByShelter(Shelter shelter);
//
//    UserCatShelter findByTelegramId(Long telegramId);
//
//    UserCatShelter findByChatId(Long chatId);
}
