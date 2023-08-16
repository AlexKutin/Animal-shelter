package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.UserCatShelter;

import java.util.List;

@Repository
public interface UserCatShelterRepository extends JpaRepository<UserCatShelter, Integer> {

    List<UserCatShelter> findAllByShelter(Shelter shelter);

    UserCatShelter findUserCatShelterByTelegramId(Long telegramId);
}
