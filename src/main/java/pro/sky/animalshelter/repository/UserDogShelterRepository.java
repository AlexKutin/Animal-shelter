package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.UserDogShelter;

import java.util.List;

@Repository
public interface UserDogShelterRepository extends JpaRepository<UserDogShelter, Integer> {

    List<UserDogShelter> findAllByShelter(Shelter shelter);

    UserDogShelter findUserDogShelterByTelegramId(Long telegramId);

    UserDogShelter findUserDogShelterByChatId(Long chatId);

}
