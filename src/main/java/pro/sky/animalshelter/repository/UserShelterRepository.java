package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pro.sky.animalshelter.model.Shelter;
import pro.sky.animalshelter.model.UserShelter;

import java.util.List;

@NoRepositoryBean
public interface UserShelterRepository<T extends UserShelter> extends JpaRepository<T, Integer> {

    List<T> findAllByShelter(Shelter shelter);

    T findByTelegramId(Long telegramId);

    T findByChatId(Long chatId);
}
