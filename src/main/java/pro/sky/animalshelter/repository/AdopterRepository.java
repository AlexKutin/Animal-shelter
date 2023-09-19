package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pro.sky.animalshelter.model.*;

import java.util.Collection;
import java.util.List;

@NoRepositoryBean
public interface AdopterRepository<T extends Adopter> extends JpaRepository<T, Integer> {
    List<T> findAllByAdopterStatusOrderByUser(AdopterStatus adopterStatus);

    T findAdopterIdByChatId(Long chatId);

    T findByUserAndAdopterStatusIn(UserShelter userShelter, Collection<AdopterStatus> adopterStatuses);

    T findByChatIdAndAdopterStatusIn(Long chatId, Collection<AdopterStatus> adopterStatuses);

}
